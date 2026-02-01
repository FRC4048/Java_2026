package frc.robot.commands.autocommands;


import choreo.auto.AutoFactory;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.commands.LoggableParallelCommandGroup;
import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;

public class ExampleAuto extends LoggableSequentialCommandGroup{
    public ExampleAuto(SwerveSubsystem subsystem, AutoFactory auto, IntakeSubsystem intake) {
        addCommands(
            /*
            before generating the path in choreo make sure that in the path.chor (ie. settings file) the maxv 
            is realistic for your swerve drives. Otherwise the path won't generate. (~3.81 for Swerve MK4i)
            */
            auto.resetOdometry("ExamplePath"),
            auto.trajectoryCmd("ExamplePath"),
            new PrintCommand("Moving"));
        
    }
}