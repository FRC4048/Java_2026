package frc.robot.apriltags;

import edu.wpi.first.wpilibj.Timer;
import java.io.DataInputStream;
import java.io.IOException;

public class TCPApriltagServer extends TCPServer<ApriltagReading> {

  public TCPApriltagServer(int port) {
    super(port);
  }

  /**
   * Format of message: [(double)x, (double)y, (double)yaw, (double)distance,(double)timestamp,
   * (int)apriltagNumber]
   */
  @Override
  protected ApriltagReading extractFromStream(DataInputStream stream) throws IOException {
    double posX = -1;
    double posY = -1;
    double poseYaw = -1;
    double distanceToTag = -1;
    double timestamp = -1;
    int apriltagNumber = -1;
    double now = 0;
    while (posX == -1
        && posY == -1
        && poseYaw == -1
        && distanceToTag == -1
        && apriltagNumber == -1
        && timestamp == -1) {
      posX = stream.readDouble();
      posY = stream.readDouble();
      poseYaw = stream.readDouble();
      distanceToTag = stream.readDouble();
      timestamp = stream.readDouble();
      apriltagNumber = stream.readInt();
      now = Timer.getFPGATimestamp() * 1000;
    }
    return new ApriltagReading(posX, posY, poseYaw, distanceToTag, apriltagNumber, timestamp, now);
  }
}