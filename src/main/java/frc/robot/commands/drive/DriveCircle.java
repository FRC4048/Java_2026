package frc.robot.commands.drive;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class DriveCircle extends LoggableCommand {
    private final SwerveSubsystem drivebase;
    private final double radiusMeters;
    private final double angularVelocityRadPerSec;
    private final double time;
    private final Timer timer;

    /**
     * Drives the robot in a circle while keeping the front facing the center.
     *
     * @param drivebase     The swerve subsystem.
     * @param radiusMeters  The distance from the center of rotation (in meters).
     * @param angularVelocityRadPerSec Speed of rotation (Radians/Sec). Positive is CCW, Negative is CW.
     * @param time          Duration to run the command (seconds).
     */
    public DriveCircle(SwerveSubsystem drivebase, double radiusMeters, double angularVelocityRadPerSec, double time) {
        this.drivebase = drivebase;
        this.radiusMeters = radiusMeters;
        this.angularVelocityRadPerSec = angularVelocityRadPerSec;
        this.time = time;
        this.timer = new Timer();
        addRequirements(drivebase);
    }

    @Override
    public void initialize() {
        timer.restart();
    }

    @Override
    public void execute() {
        double tangentialSpeed = radiusMeters * angularVelocityRadPerSec;
        double yVelocity = -tangentialSpeed;
        drivebase.drive(new Translation2d(0, yVelocity), angularVelocityRadPerSec, false);
    }

    @Override
    public boolean isFinished() {
        return timer.hasElapsed(time);
    }

    @Override
    public void end(boolean interrupted) {
        drivebase.drive(new Translation2d(), 0, false);
    }
}