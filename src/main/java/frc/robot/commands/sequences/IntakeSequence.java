package frc.robot.commands.sequences;

import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;
import frc.robot.commands.intake.SpinIntake;
import frc.robot.commands.intake.StopIntake;
import frc.robot.commands.intakeDeployment.InitialRunDeployment;
import frc.robot.subsystems.IntakeDeployerSubsystem;
import frc.robot.subsystems.IntakeSubsystem;

public class IntakeSequence extends LoggableSequentialCommandGroup{
    public IntakeSequence(IntakeDeployerSubsystem intakeDeployerSubsystem, IntakeSubsystem intakeSubsystem){
        super(
            new InitialRunDeployment(intakeDeployerSubsystem),
            new SpinIntake(intakeSubsystem, intakeDeployerSubsystem)
        );
    }
}
