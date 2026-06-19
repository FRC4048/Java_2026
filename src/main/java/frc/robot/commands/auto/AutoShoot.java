package frc.robot.commands.auto;

import frc.robot.commands.feeder.SpinFeeder;
import frc.robot.commands.hopper.AutoSpinHopper;
import frc.robot.commands.shooter.SetShootingState;
import frc.robot.constants.enums.ShootingState;
import frc.robot.constants.enums.ShootingState.ShootState;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.HopperSubsystem;
import frc.robot.utils.logging.commands.LoggableParallelCommandGroup;
import frc.robot.utils.logging.commands.LoggableRaceCommandGroup;
import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;
import frc.robot.utils.logging.commands.LoggableWaitCommand;

public class AutoShoot extends LoggableSequentialCommandGroup{
    public AutoShoot(HopperSubsystem hopperSubsystem, FeederSubsystem feederSubsystem, ShootingState shootState, double time){
        super(
            new SetShootingState(shootState, ShootState.SHOOTING_HUB),
            new LoggableWaitCommand(2),
            new LoggableRaceCommandGroup(
                new AutoSpinHopper(hopperSubsystem, feederSubsystem),
                new LoggableWaitCommand(time)
            ),
            new SetShootingState(shootState, ShootState.STOPPED)
        );
    }
}