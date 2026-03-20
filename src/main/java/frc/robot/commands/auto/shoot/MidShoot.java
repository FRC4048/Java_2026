package frc.robot.commands.auto.shoot;

import choreo.auto.AutoFactory;
import frc.robot.commands.ToggleShooting;
import frc.robot.commands.auto.ResetMechanisms;
import frc.robot.commands.drive.DriveSwerve;
import frc.robot.commands.shooter.SetShootingState;
import frc.robot.commands.turret.RunTurretToRevLimit;
import frc.robot.commands.turret.SetTurretAngle;
import frc.robot.constants.enums.DriveDirection;
import frc.robot.constants.enums.ShootingState;
import frc.robot.constants.enums.ShootingState.ShootState;
import frc.robot.subsystems.AnglerSubsystem;
import frc.robot.subsystems.ControllerSubsystem;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.HopperSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;

public class MidShoot extends LoggableSequentialCommandGroup{
    public MidShoot(
        SwerveSubsystem drivetrain, AutoFactory auto, ShooterSubsystem shooter, ShootingState shootstate, 
        HopperSubsystem hopper, FeederSubsystem feeder, TurretSubsystem turret, AnglerSubsystem angler, 
        ControllerSubsystem controller) {
        super(  
                new ResetMechanisms(shootstate, turret, angler),
                new RunTurretToRevLimit(turret),
                new SetShootingState(shootstate, ShootState.SHOOTING_HUB),
                new DriveSwerve(drivetrain, DriveDirection.BACKWARD, 3, 0.5)
        );
    }
}