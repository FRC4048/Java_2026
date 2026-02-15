package frc.robot.commands.turret;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.constants.Constants;
import frc.robot.constants.GameConstants;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

/**
 * Runs the turret to the home angle, facing forward
 */
public class TurretGoHome extends LoggableCommand {
    private final TurretSubsystem turret;

    public TurretGoHome(TurretSubsystem turret) {
        this.turret = turret;
        addRequirements(turret);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        new SetTurretAngle(turret, GameConstants.TURRET_HOME_ANGLE);
    }

    @Override
    public void end(boolean interrupted) {
        turret.stopMotors();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}