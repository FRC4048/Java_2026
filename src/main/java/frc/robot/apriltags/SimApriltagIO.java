package frc.robot.apriltags;

import edu.wpi.first.math.Vector;
import edu.wpi.first.math.geometry.*;
import edu.wpi.first.math.numbers.N3;
import frc.robot.constants.Constants;
import frc.robot.subsystems.ApriltagSubsystem;
import frc.robot.subsystems.swervedrive.vision.estimation.PoseEstimator;
import frc.robot.subsystems.swervedrive.vision.truster.BasicVisionFilter;
import frc.robot.subsystems.swervedrive.vision.truster.VisionMeasurement;
import frc.robot.subsystems.swervedrive.vision.truster.VisionTruster;
import frc.robot.utils.Apriltag;
import frc.robot.utils.logging.io.BaseIoImpl;

import java.util.Optional;
import java.util.Queue;
import java.util.Random;
import java.util.function.Supplier;

import frc.robot.utils.math.ObjectUtils;
import org.littletonrobotics.junction.Logger;

public class SimApriltagIO extends TCPApriltagIo {
    private final Random random = new Random();
    private final VisionTruster truster;
    private final Supplier<Optional<Pose2d>> robotPoseSupplier;
    public SimApriltagIO(String name, ApriltagInputs inputs, SimTCPServer server, VisionTruster truster, Supplier<Optional<Pose2d>> robotPoseSupplier) {
        super(name, inputs, server);
        this.truster = truster;
        this.robotPoseSupplier = robotPoseSupplier;
    }
    public void simReadings() {
        if (robotPoseSupplier.get().isPresent()) {
            for (Apriltag tag : Apriltag.values()) {
                Pose3d cameraPos = new Pose3d(robotPoseSupplier.get().get()).transformBy(Constants.ROBOT_TO_CAMERA);
                if (ObjectUtils.canSee(tag.getPose(), cameraPos, Constants.HORIZONTAL_FOV, Constants.VERTICAL_FOV)) {
                    Pose3d adjPose = tag.getPose().relativeTo(cameraPos);
                    double cosIncidenceAngle = (-adjPose.getX() * Math.cos(adjPose.getRotation().getZ()) - adjPose.getY() * Math.sin(adjPose.getRotation().getZ())) / (adjPose.getTranslation().getNorm());
                    double distance = tag.getTranslation().getDistance(cameraPos.getTranslation());
                    if (cosIncidenceAngle!=0 && distance / cosIncidenceAngle < Constants.MAX_VISION_DISTANCE_SIMULATION) {
                        VisionMeasurement measurement = new VisionMeasurement(new Pose2d(), distance, 0);
                        Vector<N3> stdDevs = truster.calculateTrust(measurement);
                        Pose2d pose = robotPoseSupplier.get().get();
                        double readingX = pose.getX() + random.nextGaussian() * stdDevs.get(0);
                        double readingY = pose.getY() + random.nextGaussian() * stdDevs.get(1);
                        double readingYaw = pose.getRotation().getDegrees() + random.nextGaussian() * stdDevs.get(2);
                        Pose2d readingPos = new Pose2d(readingX, readingY, Rotation2d.fromDegrees(readingYaw));
                        distance = readingPos.getTranslation().getDistance(tag.getPose().toPose2d().getTranslation());
                        if (BasicVisionFilter.inBounds(readingPos)) {
                            addReading(new ApriltagReading(readingX, readingY, readingYaw,
                                    distance, tag.number(), Constants.AVERAGE_CAM_LATENCY + random.nextGaussian() * Constants.AVERAGE_CAM_LATENCY_STD_DEV, Logger.getTimestamp() / 1000.0));
                        }
                    }
                }
            }
        }
    }
    @Override
    public void periodic() {
        super.periodic();
        simReadings();
    }
}