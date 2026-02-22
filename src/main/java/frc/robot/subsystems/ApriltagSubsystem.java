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
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.subsystems.swervedrive.vision.estimation.PoseEstimator;
import frc.robot.subsystems.swervedrive.vision.truster.BasicVisionFilter;
import frc.robot.subsystems.swervedrive.vision.truster.VisionMeasurement;
import frc.robot.subsystems.swervedrive.vision.truster.VisionTruster;
import frc.robot.utils.Apriltag;
import frc.robot.utils.logging.io.BaseIoImpl;
import frc.robot.utils.math.ObjectUtils;
import org.littletonrobotics.junction.Logger;

import java.util.Random;
import java.util.function.Supplier;

public class ApriltagSubsystem extends SubsystemBase {

    public static final String LOGGING_NAME = "ApriltagSubsystem";
    private final ApriltagIO io;
    private final PoseEstimator estimator;
    private final SwerveSubsystem drivebase;


    public ApriltagSubsystem(ApriltagIO io, SwerveSubsystem drivebase, VisionTruster truster) {
        this.drivebase = drivebase;
        this.io = io;
        estimator = new PoseEstimator(drivebase.getKinematics(), drivebase, 0, this, truster);
    }

    public static ApriltagIO createRealIo() {
        
        return new TCPApriltagIo(LOGGING_NAME, new ApriltagInputs());
    }

    public static ApriltagIO createMockIo() {
        return new MockApriltagIo(LOGGING_NAME, new ApriltagInputs());
    }

    public static ApriltagIO createSimIo(VisionTruster truster, SwerveSubsystem drivebase) {
        return new SimApriltagIO(LOGGING_NAME, new ApriltagInputs(), new SimTCPServer(0), truster, drivebase::getSimulationPose); // port doesnt matter at all
    }
    // This is used to inject april tag readings manually and will pretty much only be used for simulation.
    public void addSimReading(ApriltagReading reading) {
        io.addReading(reading);
    }
    public ApriltagIO getIO(){
        return io;
    }
    public VisionTruster getVisionTruster() {
        return estimator.getVisionTruster();
    }
    @Override
    public void periodic() {
        estimator.updateVision();
        estimator.updatePosition(drivebase.getOdom());
        io.periodic();
    }
    public Vector<N3> calculateTrust(VisionMeasurement measurement) {
        return estimator.getVisionTruster().calculateTrust(measurement,cameraPos);
    }
}
