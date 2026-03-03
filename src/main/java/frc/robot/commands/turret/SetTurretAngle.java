package frc.robot.commands.turret;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.constants.GameConstants;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

/**
 * Runs the turret to the target angle
 */
public class SetTurretAngle extends LoggableCommand {
        private final TurretSubsystem turret;
        private double targetAngle;
        private boolean turretInRange;
        
        public SetTurretAngle(TurretSubsystem turret, double targetAngle) {
            this.turret = turret;
            this.targetAngle = targetAngle;
            this.turretInRange = false;
            addRequirements(turret);
        }
    
        @Override
        public void initialize() {
            if ((targetAngle > GameConstants.TURRET_LEFT_ANGLE + 5) &&
             (targetAngle < GameConstants.TURRET_RIGHT_ANGLE - 5)) {
                turretInRange = true;
            }
        }
    
        @Override
        public void execute() {
            SmartDashboard.putBoolean("WITHIN_SHOOTING_RANGE", turretInRange);
        if (turretInRange) {
            turret.setAngle(targetAngle);
        }
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}