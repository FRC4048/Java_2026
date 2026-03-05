package frc.robot.commands.parallels;

import frc.robot.commands.lightStrip.SetLedFromShootingState;
import frc.robot.commands.shooter.SetShootingState;
import frc.robot.constants.enums.ShootingState;
import frc.robot.constants.enums.ShootingState.ShootState;
import frc.robot.subsystems.LightStripSubsystem;
import frc.robot.utils.logging.commands.LoggableParallelCommandGroup;

public class SetShootingStateAndLight extends LoggableParallelCommandGroup{

    public SetShootingStateAndLight(ShootingState shootState, LightStripSubsystem lightStrip, ShootState newState) {

        super(
            new SetShootingState(shootState, newState),
            new SetLedFromShootingState(lightStrip, shootState)
        );

    }
    
}
