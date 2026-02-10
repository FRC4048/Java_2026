package frc.robot.commands.intake;

import javax.lang.model.util.ElementScanner14;

import frc.robot.constants.Constants;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.utils.logging.LoggedTunableNumber;
import frc.robot.utils.logging.commands.LoggableCommand;

public class SpinIntake extends LoggableCommand {
    
    private final IntakeSubsystem subsystem;
    private final LoggedTunableNumber speed;
  
    public SpinIntake(IntakeSubsystem subsystem) {
        this.subsystem = subsystem;
        addRequirements(subsystem);
        speed = new LoggedTunableNumber("Intake_Speed",10);
    }

    @Override
    public void initialize() {
    }  

    @Override
    public void execute() {
       if (speed.hasChanged(0)) {
        subsystem.setSpeed(speed.get());
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
