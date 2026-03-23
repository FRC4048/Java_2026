package frc.robot.commands.intake;

import frc.robot.constants.Constants;
import frc.robot.constants.enums.DeploymentState;
import frc.robot.subsystems.IntakeDeployerSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class SpinIntake extends LoggableCommand {
    private final IntakeDeployerSubsystem intakeDeployer;
    private final IntakeSubsystem subsystem;

    public SpinIntake(IntakeSubsystem subsystem, IntakeDeployerSubsystem intakeDeployer) {
        this.subsystem = subsystem;
        this.intakeDeployer = intakeDeployer;
        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        if (intakeDeployer.getDeploymentState() == DeploymentState.DOWN) {
            subsystem.setSpeed(Constants.INTAKE_SPEED);
            subsystem.startIntaking();
        }else{
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
