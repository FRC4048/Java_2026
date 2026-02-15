package frc.robot.commands.climber;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.constants.Constants;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class ClimberUp extends LoggableCommand{
    
    public final ClimberSubsystem subsystem;
    public final Timer timer;
    

    public ClimberUp(ClimberSubsystem subsystem){
        timer = new Timer();
        this.subsystem = subsystem;
        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
      timer.restart();
    }

    @Override
    public void execute() {
            subsystem.setSpeed(1 * Constants.CLIMBER_SPEED_UP);
    }

    @Override
    public void end(boolean interrupted) {
        subsystem.stopMotors();
    }

    @Override
    public boolean isFinished() {
        return timer.hasElapsed(Constants.CLIMBER_TIMEOUT) || subsystem.forwardSwitchPressed();
    }

    
}

