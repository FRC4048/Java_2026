package frc.robot.commands.shooter;

import frc.robot.constants.Constants;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class SpinShooter extends LoggableCommand{
    
    private final ShooterSubsystem subsystem;
  
    public SpinShooter(ShooterSubsystem subsystem) {
        this.subsystem = subsystem;
        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
    }  

    @Override
    public void execute() {
       
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
        subsystem.stopMotors();
    }

}
