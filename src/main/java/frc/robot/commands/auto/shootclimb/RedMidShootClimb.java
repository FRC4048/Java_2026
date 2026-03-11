package frc.robot.commands.auto.shootclimb;

import choreo.auto.AutoFactory;
import frc.robot.commands.ToggleShooting;
import frc.robot.commands.angler.RunAnglerToReverseLimit;
import frc.robot.commands.climber.ClimberDown;
import frc.robot.commands.climber.ClimberUp;
import frc.robot.commands.shooter.SetShootingState;
import frc.robot.commands.turret.SetTurretAngle;
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
import frc.robot.utils.logging.commands.LoggableParallelCommandGroup;
import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;
import frc.robot.utils.logging.commands.LoggableWaitCommand;

public class RedMidShootClimb extends LoggableSequentialCommandGroup{
    public RedMidShootClimb(
        SwerveSubsystem drivetrain, AutoFactory auto, ShooterSubsystem shooter, ShootingState shootstate, 
        HopperSubsystem hopper, FeederSubsystem feeder, TurretSubsystem turret, AnglerSubsystem angler, 
        ClimberSubsystem climber, ControllerSubsystem controller) {
        super(  
                new LoggableParallelCommandGroup(
                    new ClimberDown(climber),
                    new SetTurretAngle(turret, 0),  
                    new RunAnglerToReverseLimit(angler)
                ),
                new SetShootingState(shootstate, ShootState.FIXED_2), //or some other shoot state
                LoggableCommandWrapper.wrap(auto.resetOdometry("RedMidShoot")),
                LoggableCommandWrapper.wrap(auto.trajectoryCmd("RedMidShoot")),
                new ToggleShooting(controller, 3),
                new LoggableWaitCommand(3),
                LoggableCommandWrapper.wrap(auto.resetOdometry("RedMidClimb")),
                new LoggableParallelCommandGroup(
                    LoggableCommandWrapper.wrap(auto.trajectoryCmd("RedMidClimb")),
                    new ClimberUp(climber)
                )
                //new ClimberSequence()   
        );
    }
}
