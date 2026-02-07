package frc.robot.commands.angler;

import frc.robot.subsystems.AnglerSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

/**
 * Runs the angler to the reverse limit switch and resets the encoder to zero.
 */
public class RunAnglerToReverseLimit extends LoggableCommand {
    private final AnglerSubsystem angler;
    private boolean finished = false;

    public RunAnglerToReverseLimit(AnglerSubsystem angler) {
        this.angler = angler;
        addRequirements(angler);
    }

    @Override
    public void initialize() {
        finished = false;
    }

    @Override
    public void execute() {
        angler.runToReverseLimit();
        if (angler.isAtReverseLimit()) {
            angler.resetEncoderToZero();
            finished = true;
        }
    }

    @Override
    public void end(boolean interrupted) {
        angler.stopMotors();
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}
