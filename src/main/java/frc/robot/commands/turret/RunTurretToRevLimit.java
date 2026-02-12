package frc.robot.commands.turret;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.constants.Constants;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

/**
 * Runs the turret to the reverse (left) limit switch and resets the encoder to zero.
 */
public class RunTurretToRevLimit extends LoggableCommand {
    private final TurretSubsystem turret;
    private boolean finished = false;
    private final Timer timer = new Timer();

    public RunTurretToRevLimit(TurretSubsystem turret) {
        this.turret = turret;
        addRequirements(turret);
    }

    @Override
    public void initialize() {
        finished = false;
        timer.restart();
    }

    @Override
    public void execute() {
        turret.runReverse();
        if (turret.isAtReverseLimit()) {
            turret.resetEncoderToZero();
            finished = true;
        }
    }

    @Override
    public void end(boolean interrupted) {
        turret.stopMotors();
    }

    @Override
    public boolean isFinished() {
        return finished || timer.hasElapsed(Constants.TURRET_TIMEOUT);
    }
}