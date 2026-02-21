package frc.robot.commands.parallels;

import frc.robot.commands.intake.SpinIntake;
import frc.robot.commands.intakeDeployment.InitialRunDeployment;
import frc.robot.subsystems.IntakeDeployerSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.utils.logging.commands.LoggableParallelCommandGroup;

public class DeployAndSpinIntake extends LoggableParallelCommandGroup{

    public DeployAndSpinIntake(IntakeDeployerSubsystem deployer, IntakeSubsystem intakeSubsystem){

        super(
            new InitialRunDeployment(deployer),
            new SpinIntake(intakeSubsystem)

        );
    }
}
