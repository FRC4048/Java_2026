package frc.robot.commands.hopper;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.constants.Constants;
import frc.robot.constants.enums.ShootingState;
import frc.robot.constants.enums.ShootingState.ShootState;
import frc.robot.subsystems.ControllerSubsystem;
import frc.robot.subsystems.HopperSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class DefaultSpinHopper extends LoggableCommand {

    private final HopperSubsystem subsystem;
    private final Timer timer;
    private final ControllerSubsystem controllerSubsystem;
    private final ShootingState state;

    public DefaultSpinHopper(HopperSubsystem subsystem, ControllerSubsystem controllerSubsystem, ShootingState state) {
        this.subsystem = subsystem;
        this.controllerSubsystem = controllerSubsystem;
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
        if (controllerSubsystem.shouldHopperSpin()) {
            if(state.getShootState() == ShootState.SHUTTLING){
                subsystem.setSpeed(Constants.HOPPER_SHUTTLING_SPEED);
            }else{
                subsystem.setSpeed(Constants.HOPPER_SPEED);
            }
        } else{
            subsystem.stopMotors();
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
