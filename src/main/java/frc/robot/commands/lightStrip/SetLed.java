package frc.robot.commands.lightStrip;

import frc.robot.constants.enums.ShootingState;
import frc.robot.constants.enums.Trench;
import frc.robot.constants.enums.ShootingState.ShootState;
import frc.robot.subsystems.LightStripSubsystem;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.BlinkinPattern;
import frc.robot.utils.logging.commands.LoggableCommand;

public class SetLed extends LoggableCommand{

    private final LightStripSubsystem lightStrip;
    private ShootingState shootingState;
  
    public SetLed(LightStripSubsystem lightStrip, ShootingState shootingState) {

        this.lightStrip = lightStrip;
        this.shootingState = shootingState;
        addRequirements(lightStrip);

    }

    @Override
    public void initialize() {

    }  

    @Override
    public void execute() {
        switch(shootingState.getShootState()){
            case STOPPED -> lightStrip.setPattern(BlinkinPattern.BLACK);
            case SHUTTLING -> lightStrip.setPattern(BlinkinPattern.RAINBOW_RAINBOW_PALETTE);
            case SHOOTING_HUB -> lightStrip.setPattern(BlinkinPattern.GREEN);
            case FIXED -> lightStrip.setPattern(BlinkinPattern.AQUA);
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        
    }
    
}
