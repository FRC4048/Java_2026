package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.subsystems.ControllerSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class ToggleShooting extends LoggableCommand{

   private final ControllerSubsystem controllerSubsystem;
   private final Timer timer;
   private final double time;

    public ToggleShooting(ControllerSubsystem controllerSubsystem, double time) {
        this.controllerSubsystem = controllerSubsystem;
        timer = new Timer();
        this.time = time;
    }

    @Override
    public void end(boolean interrupted) {
        controllerSubsystem.setActivatedShooting(false);
    }

    @Override
    public void initialize() {
        controllerSubsystem.setActivatedShooting(true);
        timer.restart();
    }

    @Override
    public boolean isFinished() {
        return timer.hasElapsed(time);
    }

    

}
