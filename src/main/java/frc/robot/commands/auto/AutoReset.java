package frc.robot.commands.auto;

import frc.robot.commands.angler.RunAnglerToReverseLimit;
import frc.robot.commands.shooter.SetShootingState;
import frc.robot.commands.turret.RunTurretToRevLimit;
import frc.robot.commands.turret.SetTurretAngle;
import frc.robot.constants.enums.ShootingState;
import frc.robot.constants.enums.ShootingState.ShootState;
import frc.robot.subsystems.AnglerSubsystem;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.utils.logging.commands.LoggableParallelCommandGroup;
import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;

public class AutoReset extends LoggableSequentialCommandGroup{
    public AutoReset(ShootingState shootstate, TurretSubsystem turret, AnglerSubsystem angler) {
        super(  
                new LoggableParallelCommandGroup(
                    new RunTurretToRevLimit(turret),  
                    new RunAnglerToReverseLimit(angler)
                )
        );
    }
}
