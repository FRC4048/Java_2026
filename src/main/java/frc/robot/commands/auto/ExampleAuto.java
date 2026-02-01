package frc.robot.commands.auto;


import choreo.auto.AutoFactory;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.commands.LoggableParallelCommandGroup;
import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;

public class ExampleAuto extends LoggableSequentialCommandGroup{
    public ExampleAuto(SwerveSubsystem subsystem, AutoFactory auto, IntakeSubsystem intake) {
        addCommands(
            auto.resetOdometry("ExamplePath"),
            auto.trajectoryCmd("ExamplePath"), 
            new PrintCommand("Moving"));
        
    }
}
