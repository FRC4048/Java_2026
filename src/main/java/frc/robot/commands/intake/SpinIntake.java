package frc.robot.commands.intake;

import javax.lang.model.util.ElementScanner14;

import frc.robot.constants.Constants;
import frc.robot.constants.enums.DeploymentState;
import frc.robot.subsystems.IntakeDeployerSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class SpinIntake extends LoggableCommand {
    
    private final IntakeSubsystem subsystem;
    private final IntakeDeployerSubsystem deployer;
  
    public SpinIntake(IntakeSubsystem subsystem, IntakeDeployerSubsystem deployer) {
        this.subsystem = subsystem;
        addRequirements(subsystem);
        this.deployer = deployer;
    }

    @Override
    public void initialize() {
    }  

    @Override
    public void execute() {
        if (deployer.deploymentState == DeploymentState.DOWN) {
            subsystem.setSpeed(Constants.INTAKE_SPEED);
        } else {
            subsystem.setSpeed(0);
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
