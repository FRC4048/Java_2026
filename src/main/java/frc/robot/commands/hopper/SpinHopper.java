package frc.robot.commands.hopper;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.constants.Constants;
import frc.robot.subsystems.HopperSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class SpinHopper extends LoggableCommand{
    
    public final HopperSubsystem subsystem;
    public final Timer timer;
    

    public SpinHopper(HopperSubsystem subsystem){
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
        subsystem.setSpeed(Constants.HOPPER_SPEED);
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
