package frc.robot.commands.auto.shootpickup;

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
import frc.robot.subsystems.*;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.commands.LoggableCommandWrapper;
import frc.robot.utils.logging.commands.LoggableParallelCommandGroup;
import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;
import frc.robot.utils.logging.commands.LoggableWaitCommand;

public class DepotShootPickup extends LoggableSequentialCommandGroup {
    public DepotShootPickup(
            SwerveSubsystem drivetrain, AutoFactory auto, ShooterSubsystem shooter, ShootingState shootstate,
            HopperSubsystem hopper, FeederSubsystem feeder, TurretSubsystem turret, AnglerSubsystem angler,
            ControllerSubsystem controller, IntakeDeployerSubsystem intake) {
        super(
                //shoot
                new AutoReset(shootstate, turret),
                new LoggableParallelCommandGroup(
                    LoggableCommandWrapper.wrap(auto.resetOdometry("Depot_ToDepot")),
                    LoggableCommandWrapper.wrap(auto.trajectoryCmd("Depot_ToDepot").withTimeout(2)),
                    new SetShootingState(shootstate, ShootState.SHOOTING_HUB)
                ),
                new ToggleDeployment(intake),
                new DriveSwerve(drivetrain, DriveDirection.BACKWARD, 5, 0.2),
                new LoggableWaitCommand(3),
                new ToggleDeployment(intake)
        );
    }
}
