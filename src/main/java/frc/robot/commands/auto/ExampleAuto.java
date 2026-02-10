package frc.robot.commands.auto;


import choreo.auto.AutoFactory;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.commands.LoggableParallelCommandGroup;
import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;

public class ExampleAuto extends LoggableSequentialCommandGroup{
    public ExampleAuto(SwerveSubsystem subsystem, AutoFactory auto, IntakeSubsystem intake) {
        LoggableParallelCommandGroup pathOne = new LoggableParallelCommandGroup(
            auto.trajectoryCmd("ExamplePathOne"),
            new PrintCommand("Started ExamplePathOne")
        );
        LoggableSequentialCommandGroup pathOneEnd = new LoggableSequentialCommandGroup(
            pathOne,
            new PrintCommand("Ending ExamplePathOne")
        );
        LoggableParallelCommandGroup pathTwo = new LoggableParallelCommandGroup(
            auto.trajectoryCmd("ExamplePathTwo"),
            new PrintCommand("Started ExamplePathTwo")
        );
        LoggableSequentialCommandGroup pathTwoEnd = new LoggableSequentialCommandGroup(
            pathTwo,
            new PrintCommand("Ending ExamplePathTwo")
        );
        addCommands(
            auto.resetOdometry("ExamplePathOne"),
            pathOneEnd,
            auto.resetOdometry("ExamplePathTwo"),
            pathTwoEnd
        );
    }
}