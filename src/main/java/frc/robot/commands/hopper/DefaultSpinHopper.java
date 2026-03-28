package frc.robot.commands.hopper;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.RobotMode;
import frc.robot.constants.Constants;
import frc.robot.subsystems.ControllerSubsystem;
import frc.robot.subsystems.HopperSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class DefaultSpinHopper extends LoggableCommand {

    private final HopperSubsystem subsystem;
    private final Timer timer;
    private final ControllerSubsystem controllerSubsystem;

    public DefaultSpinHopper(HopperSubsystem subsystem, ControllerSubsystem controllerSubsystem) {
        this.subsystem = subsystem;
        this.controllerSubsystem = controllerSubsystem;
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
            subsystem.setSpeed(Robot.getMode() == RobotMode.AUTONOMOUS ? Constants.AUTO_HOPPER_SPEED : Constants.HOPPER_SPEED);
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
