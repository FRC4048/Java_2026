package frc.robot.apriltags;

import edu.wpi.first.wpilibj.Timer;
import java.io.DataInputStream;
import java.io.IOException;
import frc.robot.constants.Constants;

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
        double cameraToTagAngle = -1;
        double latency = -1;
        double stdDev = -1;
        int apriltagNumber = -1;
        double now = 0;
        while (posX == -1
                && posY == -1
                && poseYaw == -1
                && distanceToTag == -1
                && cameraToTagAngle == -1
                && apriltagNumber == -1
                && latency == -1
                && stdDev == -1) {
            posX = stream.readDouble();
            posY = stream.readDouble();
            poseYaw = stream.readDouble();
            distanceToTag = stream.readDouble();
            cameraToTagAngle = stream.readDouble();
            latency = stream.readDouble();
            stdDev = stream.readDouble();
            apriltagNumber = stream.readInt();
            now = Timer.getFPGATimestamp() * 1000-Constants.AVERAGE_PIR_LATENCY-latency;
        }
        return new ApriltagReading(posX, posY, poseYaw, distanceToTag, cameraToTagAngle, apriltagNumber, latency, stdDev, now);
    }
}
