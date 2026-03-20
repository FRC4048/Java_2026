package frc.robot.utils.simulation;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import org.littletonrobotics.junction.Logger;
import swervelib.SwerveDrive;

import java.util.concurrent.ConcurrentLinkedDeque;

//For now use this only for simulation
public class RawOdometry {
    private final SwerveDriveOdometry rawOdometry;
    private final SwerveDrive swerveDrive;
    private final ConcurrentLinkedDeque<PoseErrorRecord> poseError = new ConcurrentLinkedDeque<>();
    private record PoseErrorRecord(double timestamp, double error) {}
    public RawOdometry(SwerveDrive swerveDrive, Pose2d startingPose) {
        rawOdometry = new SwerveDriveOdometry(
                swerveDrive.kinematics,
                swerveDrive.getOdometryHeading(),
                swerveDrive.getModulePositions(),
                startingPose);
        this.swerveDrive = swerveDrive;
    }
    public void periodic() {
        rawOdometry.update(
                swerveDrive.getOdometryHeading(),
                swerveDrive.getModulePositions());
        double currentTime = Logger.getTimestamp() / 1000000.0;
        double oneSecondAgo = currentTime - 1.0;
        poseError.removeIf(record -> record.timestamp < oneSecondAgo);
        poseError.add(new PoseErrorRecord(currentTime, getError()));
        Logger.recordOutput("AveragePoseError", getAverageError());
        rawOdometry.update(
                swerveDrive.getOdometryHeading(),
                swerveDrive.getModulePositions());
    }
    public void resetOdom(Pose2d initialHolonomicPose) {
        SwerveModulePosition[] modules = new SwerveModulePosition[4];
        for (int i=0; i<4; i++) {
            modules[i] = new SwerveModulePosition();
        }
        rawOdometry.resetPosition(initialHolonomicPose.getRotation(), modules, initialHolonomicPose);
    }
    public Pose2d getOdom() {
        return rawOdometry.getPoseMeters();
    }
    public double getError() {
        return swerveDrive.getPose().getTranslation().getDistance(swerveDrive.getSimulationDriveTrainPose().get().getTranslation());
    }
    public double getAverageError(){
        return poseError.stream().mapToDouble(record -> record.error).average().orElse(0);
    }
}
