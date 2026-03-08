package frc.robot.commands.shooter;

import frc.robot.subsystems.ControllerSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class DefaultShooterControl extends LoggableCommand {

    private final ShooterSubsystem shooterSubsystem;
    private final ControllerSubsystem controllerSubsystem;

    public DefaultShooterControl(ShooterSubsystem shooterSubsystem, ControllerSubsystem controllerSubsystem) {
        this.shooterSubsystem = shooterSubsystem;
        this.controllerSubsystem = controllerSubsystem;
        addRequirements(shooterSubsystem);
    }

    @Override
    public void execute() {
        double targetShooterVelocity = controllerSubsystem.getTargetShooterVelocityRpm();
        if(targetShooterVelocity != 0){
            shooterSubsystem.setPidVelocity(targetShooterVelocity);
        }else{
            shooterSubsystem.stopMotors();
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
