package frc.robot.commands.auto;


import choreo.auto.AutoFactory;
import frc.robot.commands.PrintCommand;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.commands.LoggableCommandWrapper;
import frc.robot.utils.logging.commands.LoggableParallelCommandGroup;
import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;

public class ExampleAuto extends LoggableSequentialCommandGroup{
    public     ExampleAuto(SwerveSubsystem subsystem, AutoFactory auto) {
        addCommands(
                auto.resetOdometry("ExamplePathOne"),

                new LoggableParallelCommandGroup(
                        LoggableCommandWrapper.wrap(auto.trajectoryCmd("ExamplePathOne")),
                        new PrintCommand("Started ExamplePathOne")
                ),
                new PrintCommand("Finished ExamplePathOne"),

                new LoggableParallelCommandGroup(
                        LoggableCommandWrapper.wrap(auto.trajectoryCmd("ExamplePathTwo")),
                        new PrintCommand("Started ExamplePathTwo")
                ),
                new PrintCommand("Finished ExamplePathTwo")
        );
    }
}