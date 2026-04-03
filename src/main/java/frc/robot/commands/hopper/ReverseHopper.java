package frc.robot.commands.hopper;

import frc.robot.constants.Constants;
import frc.robot.subsystems.HopperSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class ReverseHopper extends LoggableCommand{
    
    public final HopperSubsystem subsystem;


    public ReverseHopper(HopperSubsystem subsystem){
        this.subsystem = subsystem;
        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
            subsystem.setSpeed(Constants.HOPPER_REVERSE_SPEED);
    }

    @Override
    public void end(boolean interrupted) {
        subsystem.stopMotors();
    }

    @Override
    public boolean isFinished() {
        return false;

    }

}
