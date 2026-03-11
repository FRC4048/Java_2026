package frc.robot.commands.climber;

import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;
import frc.robot.utils.logging.commands.LoggableParallelCommandGroup;
import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;

public class ClimbSequence extends LoggableSequentialCommandGroup {
    public ClimbSequence(ClimberSubsystem climberSubsystem, SwerveSubsystem subsystem) {
        super(
            new ClimberUp(climberSubsystem)
            
        );


    }
    
    
}
