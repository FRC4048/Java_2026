package frc.robot.commands.auto.shoot;

import choreo.auto.AutoFactory;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.Robot;
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
import frc.robot.utils.logging.commands.LoggableWaitCommand;

public class ShootBlue extends LoggableSequentialCommandGroup{
    public ShootBlue(
        SwerveSubsystem drivetrain, AutoFactory auto, ShooterSubsystem shooter, ShootingState shootstate, 
        HopperSubsystem hopper, FeederSubsystem feeder, TurretSubsystem turret, 
        ControllerSubsystem controller, IntakeDeployerSubsystem intakeDeployer) {
        super(  
                new AutoReset(shootstate, turret),
                new LoggableWaitCommand(2),
                new SetShootingState(shootstate, ShootState.SHOOTING_HUB),
                new DriveSwerve(drivetrain, DriveDirection.FORWARD, 3, 0.5),
                new ToggleDeployment(intakeDeployer, controller), //initial fuel falls in
                new LoggableWaitCommand(4),
                new ToggleDeployment(intakeDeployer, controller),
                new LoggableWaitCommand(2),
                new SetShootingState(shootstate, ShootState.STOPPED)
        );
    }
}