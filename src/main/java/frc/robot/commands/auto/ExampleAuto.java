package frc.robot.commands.auto;


import choreo.auto.AutoFactory;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;

public class ExampleAuto extends LoggableSequentialCommandGroup{
    public ExampleAuto(SwerveSubsystem subsystem, AutoFactory auto, IntakeSubsystem intake) {
        // binds a command to an event marker called "Print" in choreo
        // TODO: test this 
        auto.bind("Print", new PrintCommand("testing"));

        addCommands(
            auto.resetOdometry("PrintPath"),
            auto.trajectoryCmd("PrintPath")
        );
    }
}