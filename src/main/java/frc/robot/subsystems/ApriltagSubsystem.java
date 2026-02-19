package frc.robot.subsystems;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.Vector;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.apriltags.*;
import frc.robot.constants.Constants;
import frc.robot.constants.GameConstants;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.subsystems.swervedrive.vision.estimation.PoseEstimator;
import frc.robot.subsystems.swervedrive.vision.truster.BasicVisionFilter;
import frc.robot.subsystems.swervedrive.vision.truster.VisionMeasurement;
import frc.robot.utils.Apriltag;
import frc.robot.utils.logging.io.BaseIoImpl;
import org.littletonrobotics.junction.Logger;

import java.util.function.Supplier;
import java.util.Random;

public class ApriltagSubsystem extends SubsystemBase {

    public static final String LOGGING_NAME = "ApriltagSubsystem";
    private final ApriltagIO io;
    private final PoseEstimator estimator;
    private final SwerveSubsystem drivebase;
    private final Supplier<Pose2d> robotPoseSupplier;
    private final Random random = new Random();

    public ApriltagSubsystem(ApriltagIO io, SwerveSubsystem drivebase) {
        this.drivebase = drivebase;
        this.io = io;
       estimator = new PoseEstimator(drivebase.getKinematics(), drivebase, 0, this);
       robotPoseSupplier = drivebase::getSimulationPose;
    }

    public static ApriltagIO createRealIo() {
        
        return new TCPApriltagIo(LOGGING_NAME, new ApriltagInputs());
    }

    public static ApriltagIO createMockIo() {
        return new MockApriltagIo(LOGGING_NAME, new ApriltagInputs());
    }

    public static ApriltagIO createSimIo() {
        return new SimApriltagIO(LOGGING_NAME, new ApriltagInputs(), new SimTCPServer(0)); // port doesnt matter at all
    }
    // This is used to inject april tag readings manually and will pretty much only be used for simulation.
    public void addSimReading(ApriltagReading reading) {
        io.addReading(reading);
    }
    public ApriltagIO getIO(){
        return io;
    }
    public Pose2d getSimPose() {
        return drivebase.getSimulationPose();
    }

    @Override
    public void periodic() {
        estimator.updatePosition(drivebase.getOdom());
        estimator.updateVision();
        io.periodic();
        if (Constants.currentMode == Constants.Mode.SIM) {
            simReadings();
        }
    }
    public void simReadings() {
        for (Apriltag tag: Apriltag.values()) {
            Pose3d cameraPos = new Pose3d(robotPoseSupplier.get()).transformBy(Constants.ROBOT_TO_CAMERA);
            if (tag.canSee(cameraPos,Constants.HORIZONTAL_FOV, Constants.VERTICAL_FOV)) {
                Pose3d adjPose = tag.getPose().relativeTo(cameraPos);
                double cosIncidenceAngle = (-adjPose.getX()*Math.cos(adjPose.getRotation().getZ())-adjPose.getY()*Math.sin(adjPose.getRotation().getZ()))/(adjPose.getTranslation().getNorm());
                double distance = tag.getTranslation().getDistance(cameraPos.getTranslation());
                if (distance/cosIncidenceAngle < Constants.MAX_VISION_DISTANCE.get()) {
                    VisionMeasurement measurement = new VisionMeasurement(new Pose2d(), tag.number(),distance,0);
                    Vector<N3> stdDevs = estimator.getVisionTruster().calculateTrust(measurement, cameraPos);
                    double readingX = robotPoseSupplier.get().getX()+ random.nextGaussian()*stdDevs.get(0);
                    double readingY = robotPoseSupplier.get().getY()+ random.nextGaussian()*stdDevs.get(1);
                    double readingYaw = robotPoseSupplier.get().getRotation().getDegrees()+ random.nextGaussian()*stdDevs.get(2);
                    Pose2d readingPos = new Pose2d(readingX,readingY,Rotation2d.fromDegrees(readingYaw));
                    distance = readingPos.getTranslation().getDistance(tag.getPose().toPose2d().getTranslation());
                    if(BasicVisionFilter.inBounds(readingPos)) {
                        io.addReading(new ApriltagReading(readingX, readingY, readingYaw,
                                distance, tag.number(), Constants.AVERAGE_CAM_LATENCY + random.nextGaussian() * Constants.AVERAGE_CAM_LATENCY_STD_DEV, Logger.getTimestamp() / 1000.0));
                    }
                }
            }
        }
    }
}
