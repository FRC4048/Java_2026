package frc.robot.commands.autocommands;


import choreo.auto.AutoFactory;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.commands.LoggableParallelCommandGroup;
import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;

public class TestAuto extends LoggableSequentialCommandGroup{
    public TestAuto(SwerveSubsystem subsystem, AutoFactory auto, IntakeSubsystem intake) {
        addCommands(
            auto.resetOdometry("ExamplePath"),
            auto.trajectoryCmd("ExamplePath"), 
            new PrintCommand("Moving"));
        
    }
}