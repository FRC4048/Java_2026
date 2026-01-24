package frc.robot.commands.tilt;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Constants;
import frc.robot.subsystems.TiltSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;
// This command makes thing tilt up

// This command tilts the thing up.
public class TiltUp extends LoggableCommand {
    private final TiltSubsystem subsystem;
    private final Timer timer;

    public TiltUp(TiltSubsystem subsystem) {
        timer = new Timer();
        this.subsystem = subsystem;
        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
        timer.restart();
    }

    @Override
    public void execute() {
        subsystem.setSpeed(Constants.TILT_SPEED);
    }

    @Override
    public void end(boolean interrupted) {
        subsystem.stopMotors();
    }

    @Override
    public boolean isFinished() {
        return (subsystem.isAtTop() || timer.hasElapsed(Constants.TILT_TIMEOUT));
    }
}
