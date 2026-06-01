package frc.robot.commands.auto;

import frc.robot.commands.angler.RunAnglerToReverseLimit;
import frc.robot.commands.turret.RunTurretToRevLimit;
import frc.robot.constants.enums.ShootingState;
import frc.robot.subsystems.AnglerSubsystem;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.utils.logging.commands.LoggableParallelCommandGroup;

public class AutoReset extends LoggableParallelCommandGroup{
    public AutoReset(ShootingState shootstate, TurretSubsystem turret) {
        super(  
            new RunTurretToRevLimit(turret)  
            //new RunAnglerToReverseLimit(angler)
        );
    }
}
