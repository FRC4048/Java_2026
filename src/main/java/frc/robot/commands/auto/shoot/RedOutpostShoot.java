package frc.robot.commands.auto.shoot;

import choreo.auto.AutoFactory;
import frc.robot.commands.ToggleShooting;
import frc.robot.commands.angler.RunAnglerToReverseLimit;
import frc.robot.commands.shooter.SetShootingState;
import frc.robot.commands.turret.SetTurretAngle;
import frc.robot.constants.enums.ShootingState;
import frc.robot.constants.enums.ShootingState.ShootState;
import frc.robot.subsystems.AnglerSubsystem;
import frc.robot.subsystems.ControllerSubsystem;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.HopperSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.commands.LoggableCommandWrapper;
import frc.robot.utils.logging.commands.LoggableParallelCommandGroup;
import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;

public class RedOutpostShoot extends LoggableSequentialCommandGroup{
    public RedOutpostShoot(
        SwerveSubsystem drivetrain, AutoFactory auto, ShooterSubsystem shooter, ShootingState shootstate, 
        HopperSubsystem hopper, FeederSubsystem feeder, TurretSubsystem turret, AnglerSubsystem angler, 
        ControllerSubsystem controller) {
        super(  
                new LoggableParallelCommandGroup(
                    new SetTurretAngle(turret, 0),  
                    new RunAnglerToReverseLimit(angler)
                ),
                new SetShootingState(shootstate, ShootState.FIXED_2), //or some other shoot state
                LoggableCommandWrapper.wrap(auto.resetOdometry("RedOutpostShoot")),
                LoggableCommandWrapper.wrap(auto.trajectoryCmd("RedOutpostShoot")),
                new ToggleShooting(controller, 3)
        );
    }
}