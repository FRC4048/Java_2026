package frc.robot.subsystems;

import java.util.ArrayList;

import org.littletonrobotics.junction.Logger;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.MjpegServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoSource;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StringArrayPublisher;
import edu.wpi.first.util.PixelFormat;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CameraThread {

  private static final int INPUT_WIDTH = 640 / 4;
  private static final int INPUT_HEIGHT = 480 / 4;
  private static final int OUTPUT_WIDTH = INPUT_HEIGHT;
  private static final int OUTPUT_HEIGHT = INPUT_WIDTH;

  private static final String LOGGING_PREFIX = "DriverCam";
  private static final String OUTPUT_SERVER_NAME = "serve_" + LOGGING_PREFIX;
  private static final int OUTPUT_PORT = 1182;
  private static final int CAMERA_INDEX = 0;
  private static final int FRAME_ERROR_RETRY_MS = 20;

  // Leaving this commented out now, may bring back double lines
  // private static final double TOP_Y = 45;
  // private static final double BOTTOM_Y = 72;

  private static final double HORIZ_LINE = 85.0;

  public CameraThread() {
    CameraRunner runner = new CameraRunner();
    Thread cameraThread = new Thread(runner, "CameraThread");
    cameraThread.setDaemon(true);
    cameraThread.start();
  }

  private class CameraRunner implements Runnable {
    @Override
    public void run() {
      try {
        processImage();
      } catch (Throwable e) {
        e.printStackTrace();
        Logger.recordOutput(LOGGING_PREFIX + "/fatalError", e.toString());
        return;
      }
    }

    private void processImage() {
      UsbCamera camera = CameraServer.startAutomaticCapture("USB Camera " + CAMERA_INDEX, CAMERA_INDEX);
      camera.setConnectionStrategy(VideoSource.ConnectionStrategy.kKeepOpen);
      camera.setResolution(INPUT_WIDTH, INPUT_HEIGHT);

      // Get a CvSink. This will capture Mats from the Camera
      // Setup a CvSource. This will send images back to the dashboard
      CvSink cvSink = CameraServer.getVideo(camera);
      CvSource outputStream =
          new CvSource(LOGGING_PREFIX, PixelFormat.kBGR, OUTPUT_WIDTH, OUTPUT_HEIGHT, 30);
      CameraServer.addCamera(outputStream);

      MjpegServer outputServer = CameraServer.addServer(OUTPUT_SERVER_NAME, OUTPUT_PORT);
      outputServer.setSource(outputStream);
      Logger.recordOutput(LOGGING_PREFIX + "/streamPort", outputServer.getPort());
      int teamNumber = RobotController.getTeamNumber();
      String[] streamUrls = buildStreamUrls(teamNumber, outputServer.getPort());
      NetworkTable cameraPublisherTable =
          NetworkTableInstance.getDefault().getTable("CameraPublisher").getSubTable(LOGGING_PREFIX);
      StringArrayPublisher streamsPublisher = cameraPublisherTable.getStringArrayTopic("streams").publish();
      streamsPublisher.set(streamUrls);
      SmartDashboard.putString(LOGGING_PREFIX + "URL", streamUrls[0]);
      Logger.recordOutput(LOGGING_PREFIX + "/streamUrl", streamUrls[0]);
      Logger.recordOutput(LOGGING_PREFIX + "/streams", streamUrls);

      // Mats are very expensive. Let's reuse this Mat.
      Mat cameraMat = new Mat();

      int errorCount = 0;

      Mat rotatedMat = new Mat();

      while (!Thread.currentThread().isInterrupted()) {

        long startTime = System.currentTimeMillis();
        // Tell the CvSink to grab a frame from the camera and put it
        // in the source mat.  If there is an error notify the output.
        if (cvSink.grabFrame(cameraMat) == 0) {
          errorCount++;
          Logger.recordOutput(LOGGING_PREFIX + "/errorCount", errorCount);
          Logger.recordOutput(LOGGING_PREFIX + "/lastError", cvSink.getError());
          // Send the output the error.
          outputStream.notifyError(cvSink.getError());
          try {
            Thread.sleep(FRAME_ERROR_RETRY_MS);
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            break;
          }
          // skip the rest of the current iteration
          continue;
        }

        long mark1 = System.currentTimeMillis();
        Logger.recordOutput(LOGGING_PREFIX + "/mark1", (mark1 - startTime));

        drawLines(cameraMat);

        Core.flip(cameraMat, cameraMat, 0);
        Core.transpose(cameraMat, rotatedMat);

        // Give the output stream a new image to display
        outputStream.putFrame(rotatedMat);
        long endTime = System.currentTimeMillis();
        Logger.recordOutput(LOGGING_PREFIX + "/frameProcessingTimeMS", (endTime - startTime));
      }
    }

    private void drawLines(Mat mat) {

      Imgproc.line(
          mat,
          new Point(HORIZ_LINE, 0),
          new Point(HORIZ_LINE, INPUT_HEIGHT),
          new Scalar(20, 97, 255));
      Imgproc.line(
          mat,
          new Point(0, INPUT_HEIGHT / 2.0),
          new Point(INPUT_WIDTH, INPUT_HEIGHT / 2.0),
          new Scalar(20, 97, 255));

      // Leaving this commented out right now, may bring back two lines
      // Imgproc.line(mat, new Point(0, TOP_Y), new Point(INPUT_WIDTH, TOP_Y), new Scalar(0, 255,
      // 0));
      // Imgproc.line(mat, new Point(0, BOTTOM_Y), new Point(INPUT_WIDTH, BOTTOM_Y), new Scalar(0,
      // 255, 0));
    }

    private String[] buildStreamUrls(int teamNumber, int port) {
      ArrayList<String> urls = new ArrayList<>();
      if (teamNumber > 0) {
        String teamIpUrl =
            "http://10."
                + (teamNumber / 100)
                + "."
                + (teamNumber % 100)
                + ".2:"
                + port
                + "/?action=stream";
        urls.add(teamIpUrl);
        urls.add("mjpg:" + teamIpUrl);

        String mdnsUrl = "http://roborio-" + teamNumber + "-frc.local:" + port + "/?action=stream";
        urls.add(mdnsUrl);
      }

      String localUrl = "http://localhost:" + port + "/?action=stream";
      urls.add(localUrl);
      urls.add("mjpg:" + localUrl);
      return urls.toArray(new String[0]);
    }
  }
}
