package frc.robot.apriltags;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation3d;
import org.littletonrobotics.junction.inputs.LoggableInputs;
import org.littletonrobotics.junction.LogTable;

public class ApriltagInputs implements LoggableInputs {
    public double[] timestamp = new double[0];
    public double[] serverTime = new double[0];
    public double[] posX = new double[0];
    public double[] posY = new double[0];
    public double[] poseYaw = new double[0];
    public double[] distanceToTag = new double[0];
    public double[] cameraToTagAngle = new double[0];
    public double[] stdDev = new double[0];
    public int[] apriltagNumber = new int[0];
    public Translation3d[] apriltagPoseArray = new Translation3d[0];
    public Pose2d[] visionPoseArray = new Pose2d[0];



    @Override
    public void toLog(LogTable table) {
        table.put("timestamp", timestamp);
        table.put("serverTime", serverTime);
        table.put("posX", posX);
        table.put("posY", posY);
        table.put("poseYaw", poseYaw);
        table.put("distanceToTag", distanceToTag);
        table.put("cameraToTagAngle", cameraToTagAngle);
        table.put("apriltagNumber", apriltagNumber);
        table.put("stdDev", stdDev);
        table.put("aprilTagPositions", apriltagPoseArray);
        table.put("visionPoseArray", visionPoseArray);
    }

    @Override
    public void fromLog(LogTable table) {
        timestamp = table.get("timestamp", timestamp);
        serverTime = table.get("serverTime", serverTime);
        posX = table.get("posX", posX);
        posY = table.get("posY", posY);
        poseYaw = table.get("poseYaw", poseYaw);
        distanceToTag = table.get("distanceToTag", distanceToTag);
        cameraToTagAngle = table.get("cameraToTagAngle", cameraToTagAngle);
        apriltagNumber = table.get("apriltagNumber", apriltagNumber);
        apriltagPoseArray = table.get("aprilTagPositions", apriltagPoseArray);
        stdDev = table.get("stdDev", stdDev);
        visionPoseArray = table.get("visionPoseArray", visionPoseArray);
    }
}
