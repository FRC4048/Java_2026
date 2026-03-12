package frc.robot.commands.auto;

import frc.robot.commands.angler.RunAnglerToReverseLimit;
import frc.robot.commands.turret.RunTurretToRevLimit;
import frc.robot.subsystems.AnglerSubsystem;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.utils.logging.commands.LoggableParallelCommandGroup;
import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;

public class DoNothing extends LoggableSequentialCommandGroup{
    public DoNothing(TurretSubsystem turret, AnglerSubsystem angler) {
            super(
                new LoggableParallelCommandGroup(
                    new RunTurretToRevLimit(turret),
                    new RunAnglerToReverseLimit(angler)
                )

            );
    }
}