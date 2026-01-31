package frc.robot.commands.intake;

import javax.lang.model.util.ElementScanner14;

import frc.robot.Constants;
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
        if (subsystem.isDeployed()) {
            subsystem.setSpeed(Constants.INTAKE_SPEED);
        } else {
            subsystem.setSpeed(0);
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
