package frc.robot.commands.feeder;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.constants.Constants;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.utils.logging.TimeoutLogger;
import frc.robot.utils.logging.commands.LoggableCommand;

public class SpinFeeder  extends LoggableCommand {
    
    private final FeederSubsystem subsystem;
    private final Timer timer;
  
    public SpinFeeder(FeederSubsystem subsystem) {
        this.subsystem = subsystem;
        timer = new Timer();
        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
        timer.restart();
    }  

    @Override
    public void execute() {
       subsystem.setSpeed(Constants.FEEDER_SPEED);
    }

    @Override
    public boolean isFinished() {
        if (timer.hasElapsed(Constants.FEEDER_TIMEOUT)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void end(boolean interrupted) {
        subsystem.stopMotors();
    }

}
