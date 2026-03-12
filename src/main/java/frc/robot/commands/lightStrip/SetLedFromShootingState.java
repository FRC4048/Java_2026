package frc.robot.commands.lightStrip;

import frc.robot.constants.enums.ShootingState;
import frc.robot.subsystems.LightStripSubsystem;
import frc.robot.utils.BlinkinPattern;
import frc.robot.utils.logging.commands.LoggableCommand;

// This code is unused. The light strip is fully controlled through SetLed.

public class SetLedFromShootingState extends LoggableCommand{
    
    private final LightStripSubsystem subsystem;
    private ShootingState shootingState;
  
    public SetLedFromShootingState(LightStripSubsystem subsystem, ShootingState shootingState) {
        this.subsystem = subsystem;
        this.shootingState = shootingState;
        addRequirements(subsystem);
    }

    @Override
    public void initialize() {

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
    }

    @Override
    public void end(boolean interrupted) {
        
    }

}
