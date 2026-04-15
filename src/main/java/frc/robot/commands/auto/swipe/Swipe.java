package frc.robot.commands.auto.swipe;

import choreo.auto.AutoFactory;
import frc.robot.commands.auto.AutoReset;
import frc.robot.commands.auto.AutoShoot;
import frc.robot.commands.drive.DriveSwerve;
import frc.robot.commands.intakeDeployment.ForceSetDeployment;
import frc.robot.commands.intakeDeployment.SetDeploymentState;
import frc.robot.commands.intakeDeployment.ToggleDeployment;
import frc.robot.commands.shooter.SetShootingState;
import frc.robot.constants.enums.DeploymentState;
import frc.robot.constants.enums.DriveDirection;
import frc.robot.constants.enums.ShootingState;
import frc.robot.constants.enums.ShootingState.ShootState;
import frc.robot.subsystems.*;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.commands.LoggableCommandWrapper;
import frc.robot.utils.logging.commands.LoggableParallelCommandGroup;
import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;

public class Swipe extends LoggableSequentialCommandGroup {

    public Swipe(
            SwerveSubsystem drivetrain, AutoFactory auto, ShooterSubsystem shooter, ShootingState shootstate,
            HopperSubsystem hopper, FeederSubsystem feeder, TurretSubsystem turret, AnglerSubsystem angler,
            ControllerSubsystem controller, IntakeDeployerSubsystem intake, boolean onRed) {
        super(  
            new ForceSetDeployment(intake, DeploymentState.DOWN),
            new LoggableParallelCommandGroup(
                new ToggleDeployment(intake, controller),
                new AutoReset(shootstate, turret, angler),
                new LoggableSequentialCommandGroup(
                    LoggableCommandWrapper.wrap(auto.resetOdometry("swipe")),
                    LoggableCommandWrapper.wrap(auto.trajectoryCmd("swipe"))
                )
            ),
            new SetShootingState(shootstate, ShootState.AUTO_AIM),
            new LoggableParallelCommandGroup(
                new AutoShoot(hopper, feeder, 3),
                new ToggleDeployment(intake, controller)
            ),
            new LoggableParallelCommandGroup(
                new LoggableSequentialCommandGroup(
                    LoggableCommandWrapper.wrap(auto.resetOdometry("swipeDeeper")),
                    LoggableCommandWrapper.wrap(auto.trajectoryCmd("swipeDeeper"))
                ),
                new ToggleDeployment(intake, controller)
            ),
            new LoggableParallelCommandGroup(
                new AutoShoot(hopper, feeder, 5),
                new DriveSwerve(drivetrain, onRed ? DriveDirection.BACKWARD: DriveDirection.FORWARD , 1.5, 0.4)
            )
        );
    }
}
