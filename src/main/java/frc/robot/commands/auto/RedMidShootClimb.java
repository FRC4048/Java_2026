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

public class RedMidShootClimb extends LoggableSequentialCommandGroup{
    public RedMidShootClimb(SwerveSubsystem drivetrain, AutoFactory auto, ShooterSubsystem shooter, 
    ShootingState shootstate, ClimberSubsystem climber, HopperSubsystem hopper, FeederSubsystem feeder) {
        super(
                new SetShootingState(shootstate, ShootState.SHOOTING_HUB),
                LoggableCommandWrapper.wrap(auto.resetOdometry("MidToTower")),
                LoggableCommandWrapper.wrap(auto.trajectoryCmd("MidToTower")/*.withTimeout(n)*/), 
                new LoggableParallelCommandGroup(
                    new SpinShooter(shooter, Constants.SHOOTER_SPEED),
                    new SpinHopper(hopper),
                    new SpinFeeder(feeder)
                ),
                LoggableCommandWrapper.wrap(auto.resetOdometry("RedMidClimb")),
                new ClimberUp(climber),
                LoggableCommandWrapper.wrap(auto.trajectoryCmd("RedMidClimb")/*.withTimeout(n)*/), 
                new ClimberDown(climber)
        );
    }
}
