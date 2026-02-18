package frc.robot.commands.drive;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class SpinInPlace extends LoggableCommand {
    private final SwerveSubsystem drivebase;
    private final double angularVelocity;

    private final boolean fieldRelative;
    private final double time;
    private Timer timer;

    public SpinInPlace(SwerveSubsystem drivebase, double angularVelocity, boolean fieldRelative, double time) {
        this.drivebase = drivebase;
        this.angularVelocity = angularVelocity;
        this.fieldRelative = fieldRelative;
        this.time = time;
        timer = new Timer();
        addRequirements(drivebase);
    }

    @Override
    public void initialize() {
        timer.restart();
    }

    @Override
    public void execute() {
        drivebase.drive(new Translation2d(), angularVelocity, fieldRelative);
    }

    @Override
    public boolean isFinished() {
        return timer.hasElapsed(time);
    }

    @Override
    public void end(boolean interrupted) {
        drivebase.drive(new Translation2d(), 0, fieldRelative);
    }
}