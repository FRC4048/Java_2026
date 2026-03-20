package frc.robot.commands.auto.disturbance;

import choreo.auto.AutoFactory;
import frc.robot.commands.ToggleShooting;
import frc.robot.commands.auto.ResetMechanisms;
import frc.robot.commands.turret.SetTurretAngle;
import frc.robot.constants.enums.ShootingState;
import frc.robot.subsystems.AnglerSubsystem;
import frc.robot.subsystems.ControllerSubsystem;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.commands.LoggableCommandWrapper;
import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;

public class OutpostDisturbance extends LoggableSequentialCommandGroup{
    public OutpostDisturbance(SwerveSubsystem subsystem, AutoFactory auto, ShootingState shootstate, TurretSubsystem turret,
    AnglerSubsystem angler, ControllerSubsystem controller) {
        super(
            LoggableCommandWrapper.wrap(auto.resetOdometry("Disturbance")),
            LoggableCommandWrapper.wrap(auto.trajectoryCmd("Disturbance")),
            LoggableCommandWrapper.wrap(auto.resetOdometry("BackFromTheDisturbance")),
            LoggableCommandWrapper.wrap(auto.trajectoryCmd("BackFromTheDisturbance")),
            new ResetMechanisms(shootstate, turret, angler),
            new SetTurretAngle(turret, 0),
            new ToggleShooting(controller, 10)
        );
    }
}