package frc.robot.subsystems;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.apriltags.*;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.subsystems.swervedrive.vision.estimation.PoseEstimator;
import frc.robot.utils.logging.io.BaseIoImpl;

public class ApriltagSubsystem extends SubsystemBase {

    public static final String LOGGING_NAME = "ApriltagSubsystem";
    private final ApriltagIO io;
    private final PoseEstimator estimator;
    private final SwerveSubsystem drivebase;

    public ApriltagSubsystem(ApriltagIO io, SwerveSubsystem drivebase) {
        this.drivebase = drivebase;
        this.io = io;
        drivebase.setVariance(VecBuilder.fill(10,10,10));
       estimator = new PoseEstimator(drivebase.getKinematics(), drivebase, 0, this);
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

    @Override
    public void periodic() {
        estimator.updateVision();
        estimator.updatePosition(drivebase.getOdom());
        io.periodic();
    }
}
