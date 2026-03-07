package frc.robot.commands.sequences;

import frc.robot.commands.angler.StowAngler;
import frc.robot.commands.climber.ClimberDown;
import frc.robot.commands.intakeDeployment.SetDeploymentState;
import frc.robot.commands.shooter.SetShootingState;
import frc.robot.commands.shooter.StopShooter;
import frc.robot.commands.turret.RunTurretToRevLimit;
import frc.robot.constants.enums.DeploymentState;
import frc.robot.constants.enums.ShootingState;
import frc.robot.constants.enums.ShootingState.ShootState;
import frc.robot.subsystems.AnglerSubsystem;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.HopperSubsystem;
import frc.robot.subsystems.IntakeDeployerSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.utils.logging.commands.LoggableParallelCommandGroup;

public class ResetAll extends LoggableParallelCommandGroup {

    public ResetAll(AnglerSubsystem anglerSubsystem, ClimberSubsystem climberSubsystem,
            FeederSubsystem feederSubsystem, HopperSubsystem hopperSubsystem,
            IntakeDeployerSubsystem intakeDeployerSubsystem, IntakeSubsystem intakeSubsystem,
            ShooterSubsystem shooterSubsystem, TurretSubsystem turretSubsystem, ShootingState shootState) {
        super(
                new SetDeploymentState(intakeDeployerSubsystem, DeploymentState.UP),
                new StowAngler(anglerSubsystem),
                new ClimberDown(climberSubsystem),
                new SetShootingState(shootState, ShootState.STOPPED),
                new RunTurretToRevLimit(turretSubsystem));
    }
}
