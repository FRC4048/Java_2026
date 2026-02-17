package frc.robot.commands.auto;

import choreo.auto.AutoFactory;
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
import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;

public class LeftShootandClimb extends LoggableSequentialCommandGroup{
    public LeftShootandClimb(SwerveSubsystem subsystem, AutoFactory auto, ShooterSubsystem shooter, ShootingState shootstate, ClimberSubsystem climber) {
        super(
                new SetShootingState(shootstate, ShootState.SHOOTING_HUB),
                LoggableCommandWrapper.wrap(auto.resetOdometry("LeftToTower")),
                LoggableCommandWrapper.wrap((auto.trajectoryCmd("LeftToTower"))),
                new SpinShooter(shooter, Constants.SHOOTER_SPEED),
                new ClimberUp(climber)
        );
    }
}
