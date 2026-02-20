package frc.robot.commands.climber;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.constants.Constants;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.utils.logging.TimeoutLogger;
import frc.robot.utils.logging.commands.LoggableCommand;

public class ClimberUp extends LoggableCommand {
    
    private final TimeoutLogger timeoutCounter;
    public final ClimberSubsystem subsystem;
    public final Timer timer;

    public ClimberUp(ClimberSubsystem subsystem) {
        timeoutCounter = new TimeoutLogger(getName());
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
        subsystem.setSpeed(1 * Constants.CLIMBER_SPEED_UP);
    }

    @Override
    public void end(boolean interrupted) {
        if (timer.hasElapsed(Constants.CLIMBER_TIMEOUT)) {
            timeoutCounter.increaseTimeoutCount();
        }
        subsystem.stopMotors();
    }

    @Override
    public boolean isFinished() {
        return timer.hasElapsed(Constants.CLIMBER_TIMEOUT) || subsystem.forwardSwitchPressed();
    }

}
