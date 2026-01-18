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
        subsystem.setSpeed(Constants.INTAKE_SPEED);
    }

    @Override
    public boolean isFinished() {
        if (subsystem.isDeployed()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void end(boolean interrupted) {
        subsystem.stopMotors();
    }
}
