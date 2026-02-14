package frc.robot.commands.auto;


import choreo.auto.AutoFactory;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;

public class ExampleAuto extends LoggableSequentialCommandGroup{
    public ExampleAuto(SwerveSubsystem subsystem, AutoFactory auto, IntakeSubsystem intake) {
        addCommands(
            auto.resetOdometry("ExamplePathOne"),
            auto.trajectoryCmd("ExamplePathOne"),
            auto.trajectoryCmd("ExamplePathTwo")
        );
    }
}