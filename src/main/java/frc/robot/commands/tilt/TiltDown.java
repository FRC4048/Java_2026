package frc.robot.commands.tilt;
//added a comment
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Constants;
import frc.robot.subsystems.TiltSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;
// The command makes thing tilt down

// This command tilts the thing down
public class TiltDown extends LoggableCommand {
    private final TiltSubsystem subsystem;
    private final Timer timer;
    public TiltDown(TiltSubsystem subsystem) {
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
        subsystem.setSpeed(-1 * Constants.TILT_SPEED);
    }

    @Override
    public void end(boolean interrupted) {
        subsystem.stopMotors();
    }

    @Override
    public boolean isFinished() {
        return (subsystem.isAtBottom() || timer.hasElapsed(Constants.TILT_TIMEOUT));
    }
}
