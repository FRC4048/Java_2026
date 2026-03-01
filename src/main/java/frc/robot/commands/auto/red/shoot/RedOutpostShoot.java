package frc.robot.commands.auto.red.shoot;

import choreo.auto.AutoFactory;
import frc.robot.commands.angler.RunAnglerToReverseLimit;
import frc.robot.commands.feeder.SpinFeeder;
import frc.robot.commands.shooter.SetShootingState;
import frc.robot.commands.shooter.SpinShooter;
import frc.robot.commands.turret.SetTurretAngle;
import frc.robot.commands.hopper.SpinHopper;
import frc.robot.constants.Constants;
import frc.robot.constants.enums.ShootingState;
import frc.robot.constants.enums.ShootingState.ShootState;
import frc.robot.subsystems.AnglerSubsystem;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.HopperSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.commands.LoggableParallelCommandGroup;
import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;

public class RedOutpostShoot extends LoggableSequentialCommandGroup{
    public RedOutpostShoot(
        SwerveSubsystem drivetrain, AutoFactory auto, ShooterSubsystem shooter, ShootingState shootstate, 
        HopperSubsystem hopper, FeederSubsystem feeder, TurretSubsystem turret, AnglerSubsystem angler) {
        super(  
                new LoggableParallelCommandGroup(
                    new SetTurretAngle(turret, 0), //placeholder - need to test what angle can make it into the hub
                    new RunAnglerToReverseLimit(angler),
                    new SetShootingState(shootstate, ShootState.SHOOTING_HUB)
                ),
                //shoots from starting place, will add path if need be
                new LoggableParallelCommandGroup(
                    new SpinShooter(shooter, Constants.SHOOTER_SPEED),
                    new SpinHopper(hopper),
                    new SpinFeeder(feeder)
                ) //shoots the 8 preloaded fuel
        );
    }
}
