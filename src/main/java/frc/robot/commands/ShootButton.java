package frc.robot.commands;

import frc.robot.subsystems.ControllerSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class ShootButton extends LoggableCommand{

   private final ControllerSubsystem controllerSubsystem;

    public ShootButton(ControllerSubsystem controllerSubsystem) {
        this.controllerSubsystem = controllerSubsystem;
    }

    @Override
    public void end(boolean interrupted) {
        controllerSubsystem.setActivatedShooting(false); 
    }

    @Override
    public void initialize() {
        controllerSubsystem.setActivatedShooting(true);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    

}
