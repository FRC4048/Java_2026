package frc.robot;

import choreo.trajectory.SwerveSample;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;


public class Drive extends SubsystemBase{
    private final PIDController xController = new PIDController(.1, 0.0, 0.0);
    private final PIDController yController = new PIDController(.1, 0.0, 0.0);
    private final PIDController headingController = new PIDController(.1, 0.0, 0.0);
    private SwerveSubsystem subsystem;
    public Drive(SwerveSubsystem subsystem) {
        headingController.enableContinuousInput(-Math.PI, Math.PI);
        this.subsystem = subsystem;
    }

    public void followTrajectory(SwerveSample sample) {
        Pose2d pose = subsystem.getPose();

        ChassisSpeeds speeds = new ChassisSpeeds(
            sample.vx + xController.calculate(pose.getX(), sample.x),
            sample.vy + yController.calculate(pose.getX(), sample.y),
            sample.omega + headingController.calculate(pose.getRotation().getRadians(), sample.heading)
            );

            subsystem.drive(speeds);

    }
}
