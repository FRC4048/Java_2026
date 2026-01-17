package frc.robot.apriltags;

import edu.wpi.first.math.geometry.*;
import frc.robot.constants.Constants;
import frc.robot.utils.Apriltag;
import frc.robot.utils.logging.io.BaseIoImpl;
import java.util.Queue;
import org.littletonrobotics.junction.Logger;

public class TCPApriltag extends BaseIoImpl<ApriltagInputs> implements ApriltagIO{
  private final TCPApriltagServer server;

  public TCPApriltag(String name, ApriltagInputs inputs) {
    super(name, inputs);
    server = new TCPApriltagServer(Constants.TCP_SERVER_PORT);
    server.start();
  }

  @Override
  protected void updateInputs(ApriltagInputs inputs) {
    Queue<ApriltagReading> queue = server.flush();
    int queueSize = queue.size();
    Logger.recordOutput("VisionMeasurementsThisTick", queueSize);
    inputs.posX = new double[queueSize];
    inputs.posY = new double[queueSize];
    inputs.poseYaw = new double[queueSize];
    inputs.distanceToTag = new double[queueSize];
    inputs.apriltagNumber = new int[queueSize];
    inputs.serverTime = new double[queueSize];
    inputs.timestamp = new double[queueSize];

    Translation3d[] apriltagPoseArray = new Translation3d[queueSize];
    Pose2d[] visionPoseArray = new Pose2d[queueSize];

    for (int i = 0; i < queueSize; i++) {
      ApriltagReading measurement = queue.poll();
      inputs.posX[i] = measurement.posX();
      inputs.posY[i] = measurement.posY();
      inputs.poseYaw[i] = measurement.poseYaw();
      inputs.distanceToTag[i] = measurement.distanceToTag();
      inputs.apriltagNumber[i] = measurement.apriltagNumber();
      inputs.timestamp[i] = measurement.latency();
      inputs.serverTime[i] = measurement.measurementTime();

      Apriltag apriltag = Apriltag.of(measurement.apriltagNumber());
      apriltagPoseArray[i] =
          apriltag == null ? new Translation3d(0, 0, 0) : apriltag.getTranslation();
      visionPoseArray[i] =
          new Pose2d(
              measurement.posX(),
              measurement.posY(),
              Rotation2d.fromDegrees(measurement.poseYaw()));
    }
    Logger.recordOutput("Apriltag/TagPoses", apriltagPoseArray);
    Logger.recordOutput("Apriltag/VisionPoses", visionPoseArray);
  }
}