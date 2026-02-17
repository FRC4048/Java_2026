package frc.robot.commands.angler;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.constants.Constants;
import frc.robot.subsystems.AnglerSubsystem;
import frc.robot.utils.logging.TimeoutLogger;
import frc.robot.utils.logging.commands.LoggableCommand;

/**
 * Runs the angler to the reverse limit switch and resets the encoder to zero.
 */
public class RunAnglerToReverseLimit extends LoggableCommand {
    private final TimeoutLogger timeoutCounter;
    private final AnglerSubsystem angler;
    private final Timer timer = new Timer();

    public RunAnglerToReverseLimit(AnglerSubsystem angler) {
        timeoutCounter = new TimeoutLogger(getName());
        this.angler = angler;
        addRequirements(angler);
    }

    @Override
    public void initialize() {
        timer.restart();
    }

    @Override
    public void execute() {
        angler.runReverse();
        if (angler.isAtReverseLimit() || timer.hasElapsed(Constants.ANGLER_TIMEOUT)) {
            angler.resetEncoderToZero();
        }
    }

    @Override
    public void end(boolean interrupted) {
        if (timer.hasElapsed(Constants.ANGLER_TIMEOUT)) {
            timeoutCounter.increaseTimeoutCount();
        }
        angler.stopMotors();
    }

    @Override
    public boolean isFinished() {
        return angler.isAtReverseLimit() || timer.hasElapsed(Constants.ANGLER_TIMEOUT);
    }
}
