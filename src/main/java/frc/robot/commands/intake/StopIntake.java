package frc.robot.commands.intake;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class StopIntake extends LoggableCommand {
    
    private final IntakeSubsystem subsystem;
  
    public StopIntake(IntakeSubsystem subsystem) {
        this.subsystem = subsystem;
        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
    }  

    @Override
    public void execute() {
            subsystem.stopMotors();
        }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
    }
}
