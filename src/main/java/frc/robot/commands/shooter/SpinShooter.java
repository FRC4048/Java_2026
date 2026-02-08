package frc.robot.commands.shooter;

import frc.robot.constants.Constants;
import frc.robot.constants.ShootingState;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class SpinShooter extends LoggableCommand{
    
    private final ShooterSubsystem subsystem;
    private final ShootingState shootingState;
  
    public SpinShooter(ShooterSubsystem subsystem, ShootingState shootingState) {
        this.subsystem = subsystem;
        this.shootingState = shootingState;
        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
    }  

    @Override
    public void execute() {
       switch (shootingState.getShootState()) {
            
            case STOPPED -> {
                break;
            }

            case FIXED -> {
                subsystem.setPidVelocity(Constants.SHOOTER_SPEED_FIXED);
                break;
            }

            case FIXED_2 -> {
                subsystem.setPidVelocity(Constants.SHOOTER_SPEED_FIXED_2);
                break;
            }

            case SHOOTING_HUB -> {
                break;
            }

            case SHUTTLING -> {
                break;
            }

       }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        subsystem.stopMotors();
    }

}
