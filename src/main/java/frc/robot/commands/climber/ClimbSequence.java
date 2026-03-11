package frc.robot.commands.climber;

import frc.robot.commands.drive.DriveSwerve;
import frc.robot.constants.enums.DriveDirection;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;

public class ClimbSequence extends LoggableSequentialCommandGroup {
    public ClimbSequence(ClimberSubsystem climberSubsystem, SwerveSubsystem subsystem) {
        super(
            new ClimberUp(climberSubsystem),
            new DriveSwerve(subsystem, DriveDirection.BACKWARD, 2, 2),
            new ClimberDown(climberSubsystem)
            
        );


    }
    
    
}
