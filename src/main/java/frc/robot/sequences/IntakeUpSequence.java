package frc.robot.sequences;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.intakeDeployment.InitalRunDeployment;
import frc.robot.commands.intakeDeployment.SetDeploymentState;
import frc.robot.constants.enums.DeploymentState;
import frc.robot.subsystems.IntakeDeployerSubsystem;

public class IntakeUpSequence extends SequentialCommandGroup{
    public IntakeUpSequence(IntakeDeployerSubsystem intakeDeployerSubsystem){
        addCommands(
            new SetDeploymentState(intakeDeployerSubsystem, DeploymentState.UP),
            new InitalRunDeployment(intakeDeployerSubsystem)
        );
    }
}
