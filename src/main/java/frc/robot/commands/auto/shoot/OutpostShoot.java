package frc.robot.commands.auto.shoot;

import choreo.auto.AutoFactory;
import frc.robot.commands.auto.AutoReset;
import frc.robot.commands.auto.AutoShoot;
import frc.robot.commands.drive.DriveSwerve;
import frc.robot.commands.intakeDeployment.ToggleDeployment;
import frc.robot.commands.shooter.SetShootingState;
import frc.robot.commands.turret.RunTurretToRevLimit;
import frc.robot.constants.enums.DriveDirection;
import frc.robot.constants.enums.ShootingState;
import frc.robot.constants.enums.ShootingState.ShootState;
import frc.robot.subsystems.AnglerSubsystem;
import frc.robot.subsystems.ControllerSubsystem;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.HopperSubsystem;
import frc.robot.subsystems.IntakeDeployerSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.commands.LoggableParallelCommandGroup;
import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;

public class OutpostShoot extends LoggableSequentialCommandGroup{
    public OutpostShoot(
        SwerveSubsystem drivetrain, AutoFactory auto, ShooterSubsystem shooter, ShootingState shootstate, 
        HopperSubsystem hopper, FeederSubsystem feeder, TurretSubsystem turret, AnglerSubsystem angler,
        ControllerSubsystem controller, IntakeDeployerSubsystem intakeDeployer) {
        super(  
                new LoggableParallelCommandGroup(
                    new AutoReset(shootstate, turret, angler),
                    new ToggleDeployment(intakeDeployer, controller) //initial fuel falls in
                ),
                new LoggableParallelCommandGroup(
                    new SetShootingState(shootstate, ShootState.SHOOTING_HUB),
                    new DriveSwerve(drivetrain, DriveDirection.BACKWARD, 2, 0.5)
                ),
                new AutoShoot(hopper, feeder, 3),
                new LoggableParallelCommandGroup(
                    new ToggleDeployment(intakeDeployer, controller),
                    new AutoShoot(hopper, feeder, 2)
                ),
                new SetShootingState(shootstate, ShootState.STOPPED)
        );
    }
}