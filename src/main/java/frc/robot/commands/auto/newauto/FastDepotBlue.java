package frc.robot.commands.auto.newauto;

import choreo.auto.AutoFactory;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.auto.AutoReset;
import frc.robot.commands.auto.AutoShoot;
import frc.robot.commands.drive.DriveSwerve;
import frc.robot.commands.intakeDeployment.ToggleDeployment;
import frc.robot.commands.shooter.SetShootingState;
import frc.robot.constants.enums.DriveDirection;
import frc.robot.constants.enums.ShootingState;
import frc.robot.constants.enums.ShootingState.ShootState;
import frc.robot.subsystems.*;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.commands.LoggableCommandWrapper;
import frc.robot.utils.logging.commands.LoggableParallelCommandGroup;
import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;
import frc.robot.utils.logging.commands.LoggableWaitCommand;

public class FastDepotBlue extends LoggableSequentialCommandGroup {
    public FastDepotBlue(
            SwerveSubsystem drivetrain, AutoFactory auto, ShooterSubsystem shooter, ShootingState shootstate,
            HopperSubsystem hopper, FeederSubsystem feeder, TurretSubsystem turret, AnglerSubsystem angler,
            ControllerSubsystem controller, IntakeDeployerSubsystem intake) {
        super(  
            new AutoReset(shootstate, turret, angler),
            new SetShootingState(shootstate, ShootState.AUTO_AIM),
            new LoggableParallelCommandGroup(
                LoggableCommandWrapper.wrap(auto.resetOdometry("Depot_Fast")),
                LoggableCommandWrapper.wrap(auto.trajectoryCmd("Depot_Fast"))
            ),
            new AutoShoot(hopper, feeder, shootstate, 1),
            new ToggleDeployment(intake),
            new LoggableParallelCommandGroup(
                new AutoShoot(hopper, feeder, shootstate, 20),
                new LoggableSequentialCommandGroup(
                    new DriveSwerve(drivetrain, DriveDirection.FORWARD, 5, 0.3),
                    new DriveSwerve(drivetrain, DriveDirection.BACKWARD, 0.5, 0.6),
                    new DriveSwerve(drivetrain, DriveDirection.FORWARD, 6, 0.3),
                    new ToggleDeployment(intake)
                )
            ),
            new SetShootingState(shootstate, ShootState.SHOOTING_HUB)   

        );
    }
}
