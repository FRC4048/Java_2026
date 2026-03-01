package frc.robot.commands.parallels;

import frc.robot.commands.intake.SpinIntake;
import frc.robot.commands.intakeDeployment.ToggleDeployment;
import frc.robot.subsystems.IntakeDeployerSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.utils.logging.commands.LoggableParallelCommandGroup;

public class DeployAndSpinIntake extends LoggableParallelCommandGroup{

    public DeployAndSpinIntake(IntakeDeployerSubsystem deployer, IntakeSubsystem intakeSubsystem){

        super(
            new ToggleDeployment(deployer),
            new SpinIntake(intakeSubsystem, deployer)

        );
    }
}
