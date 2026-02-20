package frc.robot.commands.sequences;
import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;
import frc.robot.commands.intake.StopIntake;
import frc.robot.commands.intakeDeployment.InitalRunDeployment;
import frc.robot.commands.intakeDeployment.SetDeploymentState;
import frc.robot.constants.enums.DeploymentState;
import frc.robot.subsystems.IntakeDeployerSubsystem;
import frc.robot.subsystems.IntakeSubsystem;

public class IntakeUpSequence extends LoggableSequentialCommandGroup{
    public IntakeUpSequence(IntakeDeployerSubsystem intakeDeployerSubsystem, IntakeSubsystem intakeSubsystem){
        super(
            new SetDeploymentState(intakeDeployerSubsystem, DeploymentState.UP),
            new InitalRunDeployment(intakeDeployerSubsystem),
            new StopIntake(intakeSubsystem)
        );
    }
}
