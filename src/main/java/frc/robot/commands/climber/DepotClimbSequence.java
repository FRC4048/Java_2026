package frc.robot.commands.climber;

import frc.robot.commands.drive.DriveSwerve;
import frc.robot.constants.enums.DriveDirection;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;

public class DepotClimbSequence extends LoggableSequentialCommandGroup {
    public DepotClimbSequence(ClimberSubsystem climberSubsystem, SwerveSubsystem subsystem) {
        super(
            new ClimberUp(climberSubsystem),
            new DriveSwerve(subsystem, DriveDirection.RIGHT, 2, 2),
            new ClimberDown(climberSubsystem)
            
        );


    }
    
    
}
