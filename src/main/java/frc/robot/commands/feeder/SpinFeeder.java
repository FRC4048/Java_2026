package frc.robot.commands.feeder;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.constants.Constants;
import frc.robot.constants.ShootingState;
import frc.robot.constants.ShootingState.ShootState;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class SpinFeeder  extends LoggableCommand {
    
    private final FeederSubsystem subsystem;
    private final Timer timer;
    private final ShootingState state;
  
    public SpinFeeder(FeederSubsystem subsystem, ShootingState state) {
        this.subsystem = subsystem;
        this.state = state;
        timer = new Timer();
        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
        timer.restart();
    }  

    @Override
    public void execute() {
       if (state.getShootState() != ShootState.STOPPED) {
            subsystem.setSpeed(Constants.FEEDER_SPEED);
        } else {
            subsystem.stopMotors();
        }
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
