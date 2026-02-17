package frc.robot.sequences;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.intake.SpinIntake;
import frc.robot.commands.intakeDeployment.InitalRunDeployment;
import frc.robot.commands.intakeDeployment.SetDeploymentState;
import frc.robot.constants.enums.DeploymentState;
import frc.robot.subsystems.IntakeDeployerSubsystem;
import frc.robot.subsystems.IntakeSubsystem;

public class IntakeDownSequence extends SequentialCommandGroup{
    public IntakeDownSequence(IntakeDeployerSubsystem intakeDeployerSubsystem, IntakeSubsystem intakeSubsystem){
        addCommands(
            new SetDeploymentState(intakeDeployerSubsystem, DeploymentState.DOWN),
            new InitalRunDeployment(intakeDeployerSubsystem),
            new SpinIntake(intakeSubsystem)
        );
    }
}
