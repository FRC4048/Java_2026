package frc.robot.commands.auto;

import choreo.auto.AutoFactory;
import frc.robot.commands.climber.ClimberDown;
import frc.robot.commands.climber.ClimberUp;
import frc.robot.commands.shooter.SetShootingState;
import frc.robot.commands.shooter.SpinShooter;
import frc.robot.constants.Constants;
import frc.robot.constants.enums.ShootingState;
import frc.robot.constants.enums.ShootingState.ShootState;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.commands.LoggableCommandWrapper;
import frc.robot.utils.logging.commands.LoggableParallelCommandGroup;
import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;

public class MidShootClimb extends LoggableSequentialCommandGroup{
    public MidShootClimb(SwerveSubsystem subsystem, AutoFactory auto, ShooterSubsystem shooter, ShootingState shootstate, ClimberSubsystem climber) {
        super(
                new SetShootingState(shootstate, ShootState.SHOOTING_HUB),
                LoggableCommandWrapper.wrap(auto.resetOdometry("LeftToTower")),
                new SpinShooter(shooter, Constants.SHOOTER_SPEED), //Should be a shoot command
                LoggableCommandWrapper.wrap(auto.resetOdometry("TowerToClimb")),
                new LoggableParallelCommandGroup(
                    LoggableCommandWrapper.wrap(auto.trajectoryCmd("TowerToClimb")),
                    new ClimberUp(climber)
                ),
                new ClimberDown(climber)
        );
    }
}
