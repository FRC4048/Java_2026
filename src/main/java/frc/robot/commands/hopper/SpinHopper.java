package frc.robot.commands.hopper;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.constants.Constants;
import frc.robot.constants.enums.ShootingState;
import frc.robot.constants.enums.ShootingState.ShootState;
import frc.robot.subsystems.HopperSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class SpinHopper extends LoggableCommand{
    
    public final HopperSubsystem subsystem;
    public final Timer timer;
    public final ShootingState state;

    public SpinHopper(HopperSubsystem subsystem, ShootingState state){
        timer = new Timer();
        this.subsystem = subsystem;
        this.state = state;
        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
      timer.restart();
    }

    @Override
    public void execute() {
        if (state.getShootState() != ShootState.STOPPED) {
            subsystem.setSpeed(Constants.HOPPER_SPEED);
        } else {
            subsystem.stopMotors();
        }
    }

    @Override
    public void end(boolean interrupted) {
        subsystem.stopMotors();
    }

    @Override
    public boolean isFinished() {
        if (timer.hasElapsed(Constants.HOPPER_TIMEOUT)){
            return true;
        }
        else{
            return false;
        }

    }

    
}
