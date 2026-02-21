package frc.robot.commands.shooter;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class StopShooter extends LoggableCommand {
    
    private final ShooterSubsystem subsystem;
  
    public StopShooter(ShooterSubsystem shooterSubsystem) {
        this.subsystem = shooterSubsystem;
        addRequirements(shooterSubsystem);
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
