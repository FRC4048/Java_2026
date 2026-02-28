package frc.robot.commands.sequences;

import frc.robot.commands.intake.SpinIntake;
import frc.robot.commands.intake.StopIntake;
import frc.robot.commands.intakeDeployment.SetDeploymentState;
import frc.robot.commands.parallels.DeployAndSpinIntake;
import frc.robot.constants.enums.DeploymentState;
import frc.robot.subsystems.IntakeDeployerSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;

public class IntakeStoppedSequence extends LoggableSequentialCommandGroup{
    public IntakeStoppedSequence(IntakeDeployerSubsystem intakeDeployerSubsystem, IntakeSubsystem intakeSubsystem){
        super(
            new SetDeploymentState(intakeDeployerSubsystem, DeploymentState.STOPPED),
            new StopIntake(intakeSubsystem)
        );
    }
}
