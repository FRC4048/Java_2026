package frc.robot.commands.auto.newauto;

import choreo.auto.AutoFactory;
import frc.robot.commands.auto.AutoReset;
import frc.robot.commands.auto.AutoShoot;
import frc.robot.commands.shooter.SetShootingState;
import frc.robot.constants.enums.ShootingState;
import frc.robot.constants.enums.ShootingState.ShootState;
import frc.robot.subsystems.*;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.commands.LoggableCommandWrapper;
import frc.robot.utils.logging.commands.LoggableParallelCommandGroup;
import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;
import frc.robot.utils.logging.commands.LoggableWaitCommand;

public class FastDepot extends LoggableSequentialCommandGroup {
    public FastDepot(
            SwerveSubsystem drivetrain, AutoFactory auto, ShooterSubsystem shooter, ShootingState shootstate,
            HopperSubsystem hopper, FeederSubsystem feeder, TurretSubsystem turret, AnglerSubsystem angler,
            ControllerSubsystem controller, IntakeDeployerSubsystem intake) {
        super(  
            new LoggableParallelCommandGroup(
                LoggableCommandWrapper.wrap(auto.resetOdometry("Depot_Fast")),
                LoggableCommandWrapper.wrap(auto.trajectoryCmd("Depot_Fast")),
                new AutoReset(shootstate, turret, angler),
                new SetShootingState(shootstate, ShootState.AUTO_AIM)
            ),
            new LoggableWaitCommand(3),
            new AutoShoot(hopper, feeder, 0)
        );
    }
}
