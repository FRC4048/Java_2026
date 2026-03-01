package frc.robot.commands.auto;

import choreo.auto.AutoFactory;
import frc.robot.commands.angler.RunAnglerToReverseLimit;
import frc.robot.commands.climber.ClimberDown;
import frc.robot.commands.climber.ClimberUp;
import frc.robot.commands.feeder.SpinFeeder;
import frc.robot.commands.shooter.SetShootingState;
import frc.robot.commands.shooter.SpinShooter;
import frc.robot.commands.turret.SetTurretAngle;
import frc.robot.commands.hopper.SpinHopper;
import frc.robot.constants.Constants;
import frc.robot.constants.enums.ShootingState;
import frc.robot.constants.enums.ShootingState.ShootState;
import frc.robot.subsystems.AnglerSubsystem;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.HopperSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.commands.LoggableCommandWrapper;
import frc.robot.utils.logging.commands.LoggableParallelCommandGroup;
import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;

public class BlueDepotShootReloadClimb extends LoggableSequentialCommandGroup{
    public BlueDepotShootReloadClimb(
        SwerveSubsystem drivetrain, AutoFactory auto, ShooterSubsystem shooter, ShootingState shootstate, 
        ClimberSubsystem climber, HopperSubsystem hopper, FeederSubsystem feeder, TurretSubsystem turret, AnglerSubsystem angler) {
        super(  
                new LoggableParallelCommandGroup( //done before the first pth bacause we are ideally shooting as we move during the BlueDepot_OriginToDepot
                    new SetTurretAngle(turret, 0),
                    new RunAnglerToReverseLimit(angler),
                    new SetShootingState(shootstate, ShootState.SHOOTING_HUB)
                ),
                LoggableCommandWrapper.wrap(auto.resetOdometry("BlueDepot_OriginToDepot")),
                new LoggableParallelCommandGroup(
                    LoggableCommandWrapper.wrap(auto.trajectoryCmd("BlueDepot_OriginToDepot").withTimeout(1.5)), //path = 1.3s
                    new SpinShooter(shooter, Constants.SHOOTER_SPEED),
                    new SpinHopper(hopper),
                    new SpinFeeder(feeder)
                ), //shoots the 8 preloaded fuel in parallel with the path
                LoggableCommandWrapper.wrap(auto.resetOdometry("BlueDepot_TowardDepot")),
                new LoggableParallelCommandGroup(
                    LoggableCommandWrapper.wrap(auto.trajectoryCmd("BlueLeftClimb").withTimeout(3)), //path = 2.7s
                    new SpinShooter(shooter, Constants.SHOOTER_SPEED),
                    new SpinHopper(hopper),
                    new SpinFeeder(feeder)
                ),
                LoggableCommandWrapper.wrap(auto.resetOdometry("BlueDepot_DepotToTower")),
                LoggableCommandWrapper.wrap(auto.trajectoryCmd("BlueDepot_DepotToTower")),
                new ClimberUp(climber),
                LoggableCommandWrapper.wrap(auto.resetOdometry("BlueDepot_TowardTower")),
                LoggableCommandWrapper.wrap(auto.trajectoryCmd("BlueDepot_TowardTower")),
                new ClimberDown(climber)
        );
    }
}
