package frc.robot.commands.intake;

import frc.robot.constants.Constants;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class SpinIntake extends LoggableCommand {
    
    private final IntakeSubsystem subsystem;
  
    public SpinIntake(IntakeSubsystem subsystem) {
        this.subsystem = subsystem;
        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
    }  

    @Override
    public void execute() {
        subsystem.setSpeed(Constants.INTAKE_SPEED);
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
