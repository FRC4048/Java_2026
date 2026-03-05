package frc.robot.commands.lightStrip;

import frc.robot.constants.enums.ShootingState;
import frc.robot.subsystems.LightStripSubsystem;
import frc.robot.utils.BlinkinPattern;
import frc.robot.utils.logging.commands.LoggableCommand;

public class SetLedFromShootingState extends LoggableCommand{
    
    private final LightStripSubsystem subsystem;
    private ShootingState shootingState;
   // private final Timer timer;
  
    public SetLedFromShootingState(LightStripSubsystem subsystem, ShootingState shootingState) {
        this.subsystem = subsystem;
        this.shootingState = shootingState;
        //timer = new Timer();
    }

    @Override
    public void initialize() {
        //timer.restart();
    }  

    @Override
    public void execute() {
        switch (shootingState.getShootState()) {
            case STOPPED:
                subsystem.setPattern(BlinkinPattern.DARK_RED);
                break;
            case FIXED:
                subsystem.setPattern(BlinkinPattern.COLOR_WAVES_OCEAN_PALETTE);
                break;
            case FIXED_2:
                subsystem.setPattern(BlinkinPattern.RED_ORANGE);
                break;
            case SHOOTING_HUB:
                subsystem.setPattern(BlinkinPattern.RAINBOW_RAINBOW_PALETTE);
                break;
            case SHUTTLING:
                subsystem.setPattern(BlinkinPattern.GREEN);
                break;
        }
    }

    @Override
    public boolean isFinished() {
        return true;
        //return timer.hasElapsed(3);
    }

    @Override
    public void end(boolean interrupted) {
        
    }

}
