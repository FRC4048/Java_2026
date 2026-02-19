package frc.robot.subsystems.swervedrive;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import swervelib.SwerveDrive;
import swervelib.SwerveModule;
import swervelib.parser.SwerveControllerConfiguration;
import swervelib.parser.SwerveDriveConfiguration;
import swervelib.telemetry.SwerveDriveTelemetry;
import swervelib.telemetry.SwerveDriveTelemetry.TelemetryVerbosity;
import swervelib.simulation.ironmaple.simulation.SimulatedArena;

public class SwerveDriveCustom extends SwerveDrive {

    // 1. Create a NEW estimator (Shadowing the parent)
    public final SwerveDrivePoseEstimator customPoseEstimator;

    // 2. We need a new lock because the parent's odometryLock is private
    private final Lock customLock = new ReentrantLock();

    public SwerveDriveCustom(SwerveDriveConfiguration config, SwerveControllerConfiguration controllerConfig,
                             double maxSpeedMPS, Pose2d startingPose,
                             Matrix<N3, N1> stateStdDevs, Matrix<N3, N1> visionMeasurementStdDev) {

        super(config, controllerConfig, maxSpeedMPS, startingPose);

        // Initialize our custom one with your specific StdDevs
        this.customPoseEstimator = new SwerveDrivePoseEstimator(
                kinematics,
                getYaw(),
                getModulePositions(),
                startingPose,
                stateStdDevs,
                visionMeasurementStdDev);
    }

    // --- 3. OVERRIDE ALL METHODS THAT USE THE ESTIMATOR ---

    @Override
    public Pose2d getPose() {
        customLock.lock();
        try {
            return customPoseEstimator.getEstimatedPosition();
        } finally {
            customLock.unlock();
        }
    }

    @Override
    public Rotation2d getOdometryHeading() {
        return getPose().getRotation();
    }

    @Override
    public void resetOdometry(Pose2d pose) {
        customLock.lock();
        try {
            customPoseEstimator.resetPosition(getYaw(), getModulePositions(), pose);
            // Handle simulation if necessary
            getMapleSimDrive().ifPresent(sim -> sim.setSimulationWorldPose(pose));
        } finally {
            customLock.unlock();
        }
    }

    public void resetSimPose(Pose2d pose) {
        getMapleSimDrive().ifPresent(sim -> sim.setSimulationWorldPose(pose));
    }

    @Override
    public void updateOdometry() {
        SwerveDriveTelemetry.startOdomCycle();
        customLock.lock();
        try {
            // Update our custom estimator
            customPoseEstimator.update(getYaw(), getModulePositions());

            // --- The following is copied from SwerveDrive.java to maintain library functionality ---
            if (SwerveDriveTelemetry.isSimulation) {
                try {
                    SimulatedArena.getInstance().simulationPeriodic();
                } catch (Exception e) {
                    DriverStation.reportError("MapleSim error", false);
                }
            }

            if (SwerveDriveTelemetry.verbosity.ordinal() >= TelemetryVerbosity.POSE.ordinal()) {
                if (SwerveDriveTelemetry.isSimulation) {
                    field.setRobotPose(getMapleSimDrive().get().getSimulatedDriveTrainPose());
                } else {
                    field.setRobotPose(customPoseEstimator.getEstimatedPosition());
                }
            }
            // (Note: You can omit some telemetry logic here if you don't need high-verbosity debugging)

        } finally {
            customLock.unlock();
        }
        SwerveDriveTelemetry.endOdomCycle();
    }

    @Override
    public void addVisionMeasurement(Pose2d robotPose, double timestamp, Matrix<N3, N1> visionMeasurementStdDevs) {
        customLock.lock();
        try {
            customPoseEstimator.addVisionMeasurement(robotPose, timestamp, visionMeasurementStdDevs);
        } finally {
            customLock.unlock();
        }
    }

    @Override
    public void addVisionMeasurement(Pose2d robotPose, double timestamp) {
        customLock.lock();
        try {
            customPoseEstimator.addVisionMeasurement(robotPose, timestamp);
        } finally {
            customLock.unlock();
        }
    }

    @Override
    public void setVisionMeasurementStdDevs(Matrix<N3, N1> visionMeasurementStdDevs) {
        customLock.lock();
        try {
            customPoseEstimator.setVisionMeasurementStdDevs(visionMeasurementStdDevs);
        } finally {
            customLock.unlock();
        }
    }
}