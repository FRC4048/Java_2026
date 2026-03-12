package frc.robot.commands.auto.shootclimb;

import choreo.auto.AutoFactory;
import frc.robot.commands.ToggleShooting;
import frc.robot.commands.auto.ResetMechanisms;
import frc.robot.commands.climber.ClimbSequence;
import frc.robot.commands.shooter.SetShootingState;
import frc.robot.constants.enums.ShootingState;
import frc.robot.constants.enums.ShootingState.ShootState;
import frc.robot.subsystems.AnglerSubsystem;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.ControllerSubsystem;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.HopperSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.commands.LoggableCommandWrapper;
import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;

public class OutpostShootClimb extends LoggableSequentialCommandGroup{
    public OutpostShootClimb(
        SwerveSubsystem drivetrain, AutoFactory auto, ShooterSubsystem shooter, ShootingState shootstate, 
        HopperSubsystem hopper, FeederSubsystem feeder, TurretSubsystem turret, AnglerSubsystem angler, 
        ControllerSubsystem controller, ClimberSubsystem climber) {
        super(  
                new ResetMechanisms(shootstate, turret, angler),
                LoggableCommandWrapper.wrap(auto.resetOdometry("OutpostShoot")),
                LoggableCommandWrapper.wrap(auto.trajectoryCmd("OutpostShoot")),
                new ToggleShooting(controller, 10),
                new SetShootingState(shootstate, ShootState.STOPPED),
                new ClimbSequence(climber, drivetrain)
        );
    }
}