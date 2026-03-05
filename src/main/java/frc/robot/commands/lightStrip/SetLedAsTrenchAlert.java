package frc.robot.commands.lightStrip;

import frc.robot.subsystems.LightStripSubsystem;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class SetLedAsTrenchAlert extends LoggableCommand{

    private final LightStripSubsystem lightStrip;
    private final SwerveSubsystem drivebase;
    private double robotX;
    private double robotY;
  
    public SetLedAsTrenchAlert(LightStripSubsystem lightStrip, SwerveSubsystem drivebase) {

        this.lightStrip = lightStrip;
        this.drivebase = drivebase;

    }

    @Override
    public void initialize() {

    }  

    @Override
    public void execute() {

        robotX = drivebase.getPose().getX();
        robotY = drivebase.getPose().getY();

    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        
    }
    
}
