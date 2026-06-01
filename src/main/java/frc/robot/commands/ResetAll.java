package frc.robot.commands;

import frc.robot.commands.angler.StowAngler;
import frc.robot.commands.climber.ClimberDown;
import frc.robot.commands.intakeDeployment.MoveIntakeDeploymentUp;
import frc.robot.commands.shooter.SetShootingState;
import frc.robot.commands.turret.TurretToRev;
import frc.robot.constants.enums.ShootingState;
import frc.robot.constants.enums.ShootingState.ShootState;
import frc.robot.subsystems.AnglerSubsystem;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.IntakeDeployerSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.utils.logging.commands.LoggableParallelCommandGroup;

public class ResetAll extends LoggableParallelCommandGroup {
    public ResetAll(ClimberSubsystem climberSubsystem,
            IntakeDeployerSubsystem intakeDeployerSubsystem, IntakeSubsystem intakeSubsystem,
            ShooterSubsystem shooterSubsystem, TurretSubsystem turretSubsystem, ShootingState shootState) {
        super(
            new MoveIntakeDeploymentUp(intakeDeployerSubsystem),
            new ClimberDown(climberSubsystem),
            new SetShootingState(shootState, ShootState.STOPPED),
            new TurretToRev(turretSubsystem)
        );
    }
}
