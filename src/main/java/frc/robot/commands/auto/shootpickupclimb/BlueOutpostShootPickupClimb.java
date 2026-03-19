package frc.robot.commands.auto.shootpickupclimb;

import choreo.auto.AutoFactory;
import frc.robot.commands.ToggleShooting;
import frc.robot.commands.auto.ResetMechanisms;
import frc.robot.commands.climber.OutpostClimbSequence;
import frc.robot.commands.climber.ClimberUp;
import frc.robot.commands.intakeDeployment.ToggleDeployment;
import frc.robot.commands.turret.SetTurretAngle;
import frc.robot.constants.enums.ShootingState;
import frc.robot.subsystems.AnglerSubsystem;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.ControllerSubsystem;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.HopperSubsystem;
import frc.robot.subsystems.IntakeDeployerSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.commands.LoggableCommandWrapper;
import frc.robot.utils.logging.commands.LoggableParallelCommandGroup;
import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;

public class BlueOutpostShootPickupClimb extends LoggableSequentialCommandGroup{
    public BlueOutpostShootPickupClimb(
        SwerveSubsystem drivetrain, AutoFactory auto, ShooterSubsystem shooter, ShootingState shootstate, 
        HopperSubsystem hopper, FeederSubsystem feeder, TurretSubsystem turret, AnglerSubsystem angler, 
        ControllerSubsystem controller, IntakeDeployerSubsystem intake, ClimberSubsystem climber) {
        super(  
                //shoot
                new ResetMechanisms(shootstate, turret, angler),
                new SetTurretAngle(turret, 0),
                LoggableCommandWrapper.wrap(auto.resetOdometry("BlueOutpost_Shoot")),
                LoggableCommandWrapper.wrap(auto.trajectoryCmd("BlueOutpost_Shoot").withTimeout(1.3)),
                new ToggleShooting(controller, 5),

                //pickup and shoot
                new LoggableParallelCommandGroup(
                    new ToggleDeployment(intake),
                    new SetTurretAngle(turret, 40),
                    new ToggleShooting(controller, 0),
                    LoggableCommandWrapper.wrap(auto.resetOdometry("BlueOutpost_Pickup"))
                ),
                LoggableCommandWrapper.wrap(auto.trajectoryCmd("BlueOutpost_Pickup").withTimeout(1.9)),
                new ToggleDeployment(intake),

                //climb

                LoggableCommandWrapper.wrap(auto.resetOdometry("BlueOutpost_Climb")),
                new LoggableParallelCommandGroup(
                    LoggableCommandWrapper.wrap(auto.trajectoryCmd("BlueOutpost_Climb").withTimeout(1.3)),
                    new ClimberUp(climber)
                ),
                new OutpostClimbSequence(climber, drivetrain)
        );
    }
}