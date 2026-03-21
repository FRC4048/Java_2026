package frc.robot.commands.auto.neutral;

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

public class OutpostNeutral extends LoggableSequentialCommandGroup {
    public OutpostNeutral(
            SwerveSubsystem drivetrain,
            AutoFactory auto,
            ShootingState shootstate,
            TurretSubsystem turret,
            AnglerSubsystem angler,
            ControllerSubsystem controller) {
        super(
                new ResetMechanisms(shootstate, turret, angler),
                new SetTurretAngle(turret, 0),
                LoggableCommandWrapper.wrap(auto.resetOdometry("Outpost_Neutral_1")),
                LoggableCommandWrapper.wrap(auto.trajectoryCmd("Outpost_Neutral_1")),
                new ToggleShooting(controller, 5),
                LoggableCommandWrapper.wrap(auto.trajectoryCmd("Outpost_Neutral_2")),
                LoggableCommandWrapper.wrap(auto.trajectoryCmd("Outpost_Neutral_3")),
                LoggableCommandWrapper.wrap(auto.trajectoryCmd("Outpost_Neutral_4")));
    }
}
