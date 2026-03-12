package frc.robot.commands.auto;

import frc.robot.commands.angler.RunAnglerToReverseLimit;
import frc.robot.commands.shooter.SetShootingState;
import frc.robot.commands.turret.SetTurretAngle;
import frc.robot.constants.enums.ShootingState;
import frc.robot.constants.enums.ShootingState.ShootState;
import frc.robot.subsystems.AnglerSubsystem;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.utils.logging.commands.LoggableParallelCommandGroup;
import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;

public class ResetMechanisms extends LoggableSequentialCommandGroup{
    public ResetMechanisms(ShootingState shootstate, TurretSubsystem turret, AnglerSubsystem angler) {
        super(  
                new LoggableParallelCommandGroup(
                    new SetTurretAngle(turret, 0),  
                    new RunAnglerToReverseLimit(angler)
                ),
        );
    }
}
