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
import frc.robot.subsystems.AnglerSubsystem;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.HopperSubsystem;
import frc.robot.subsystems.IntakeDeployerSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.commands.LoggableCommandWrapper;
import frc.robot.utils.logging.commands.LoggableParallelCommandGroup;
import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;
import frc.robot.utils.logging.commands.LoggableWaitCommand;

public class OutpostShootPickup extends LoggableSequentialCommandGroup{
    public OutpostShootPickup(
        SwerveSubsystem drivetrain, AutoFactory auto, ShooterSubsystem shooter, ShootingState shootstate, 
        HopperSubsystem hopper, FeederSubsystem feeder, TurretSubsystem turret, AnglerSubsystem angler, 
        IntakeDeployerSubsystem intake) {
        super(  
                new AutoReset(shootstate, turret, angler),
                new RunTurretToRevLimit(turret),
                new LoggableParallelCommandGroup(
                    new SetShootingState(shootstate, ShootState.SHOOTING_HUB),
                    new DriveSwerve(drivetrain, DriveDirection.BACKWARD, 2, 0.5)
                ),
                new AutoShoot(hopper, feeder, 5),

                LoggableCommandWrapper.wrap(auto.resetOdometry("Outpost_ToOutpost")),
                LoggableCommandWrapper.wrap(auto.trajectoryCmd("Outpost_ToDepot").withTimeout(1.9)),

                new LoggableWaitCommand(2),

                LoggableCommandWrapper.wrap(auto.resetOdometry("Outpost_Pickup")),
                new LoggableParallelCommandGroup(
                    LoggableCommandWrapper.wrap(auto.trajectoryCmd("Outpost_Pickup").withTimeout(1.9)),
                    new ToggleDeployment(intake),
                    new AutoShoot(hopper, feeder, 5)
                )
        );
    }
}