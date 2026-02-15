package frc.robot.commands.turret;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.constants.Constants;
import frc.robot.constants.GameConstants;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

/**
 * Runs the turret to the target angle
 */
public class SetTurretAngle extends LoggableCommand {
    private final TurretSubsystem turret;
    private double targetAngle;

    public SetTurretAngle(TurretSubsystem turret, double targetAngle) {
        this.turret = turret;
        this.targetAngle = targetAngle;
        addRequirements(turret);
    }

    @Override
    public void initialize() {
        turret.setAngle(targetAngle);
    }

    @Override
    public void execute() {
    
    }

    @Override
    public void end(boolean interrupted) {
        turret.stopMotors();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}