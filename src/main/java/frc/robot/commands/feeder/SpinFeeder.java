package frc.robot.commands.feeder;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.constants.Constants;
import frc.robot.constants.enums.ShootingState;
import frc.robot.constants.enums.ShootingState.ShootState;
import frc.robot.subsystems.ControllerSubsystem;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.utils.logging.TimeoutLogger;
import frc.robot.utils.logging.commands.LoggableCommand;

public class SpinFeeder extends LoggableCommand {
    
    private final FeederSubsystem subsystem;
    private final Timer timer;
    private final ControllerSubsystem controllerSubsystem;
  
    public SpinFeeder(FeederSubsystem subsystem, ControllerSubsystem controllerSubsystem) {
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
        if (controllerSubsystem.shouldFeederSpin()) {
            subsystem.setSpeed(Constants.FEEDER_SPEED);
        } else {
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
