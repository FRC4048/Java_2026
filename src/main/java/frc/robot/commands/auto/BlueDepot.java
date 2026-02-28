package frc.robot.commands.auto;

import choreo.auto.AutoFactory;
import frc.robot.commands.climber.ClimberDown;
import frc.robot.commands.climber.ClimberUp;
import frc.robot.commands.feeder.SpinFeeder;
import frc.robot.commands.shooter.SetShootingState;
import frc.robot.commands.shooter.SpinShooter;
import frc.robot.commands.hopper.SpinHopper;
import frc.robot.constants.Constants;
import frc.robot.constants.enums.ShootingState;
import frc.robot.constants.enums.ShootingState.ShootState;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.HopperSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.commands.LoggableCommandWrapper;
import frc.robot.utils.logging.commands.LoggableParallelCommandGroup;
import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;

public class BlueDepot extends LoggableSequentialCommandGroup{
    public BlueDepot(SwerveSubsystem drivetrain, AutoFactory auto, ShooterSubsystem shooter, 
    ShootingState shootstate, ClimberSubsystem climber, HopperSubsystem hopper, FeederSubsystem feeder) {
        super(
                new SetShootingState(shootstate, ShootState.SHOOTING_HUB),
                LoggableCommandWrapper.wrap(auto.resetOdometry("BlueDepot_OriginToDepot")),
                new LoggableParallelCommandGroup(
                    LoggableCommandWrapper.wrap(auto.trajectoryCmd("BlueDepot_OriginToDepot").withTimeout(1.5)), //1.3s
                    new SpinShooter(shooter, Constants.SHOOTER_SPEED),
                    new SpinHopper(hopper),
                    new SpinFeeder(feeder)
                ), //shoots the 8 preloaded fuel in parallel with the path
                LoggableCommandWrapper.wrap(auto.resetOdometry("BlueDepot_TowardDepot")),
                new LoggableParallelCommandGroup(
                    LoggableCommandWrapper.wrap(auto.trajectoryCmd("BlueLeftClimb").withTimeout(3)), //2.7s
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
