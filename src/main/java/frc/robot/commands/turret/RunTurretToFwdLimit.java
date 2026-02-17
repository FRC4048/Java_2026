package frc.robot.commands.turret;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.constants.Constants;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

/**
 * Runs the turret to the forward (right) limit switch
 */
public class RunTurretToFwdLimit extends LoggableCommand {
    private final TurretSubsystem turret;
    private final Timer timer = new Timer();

    public RunTurretToFwdLimit(TurretSubsystem turret) {
        this.turret = turret;
        addRequirements(turret);
    }

    @Override
    public void initialize() {
        timer.restart();
    }

    @Override
    public void execute() {
        turret.runForward();
    }

    @Override
    public void end(boolean interrupted) {
        turret.stopMotors();
    }

    @Override
    public boolean isFinished() {
        return turret.isAtForwardLimit() || timer.hasElapsed(Constants.TURRET_TIMEOUT);
    }
}