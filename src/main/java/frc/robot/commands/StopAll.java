package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.constants.Constants;
import frc.robot.subsystems.AnglerSubsystem;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.HopperSubsystem;
import frc.robot.subsystems.IntakeDeployerSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.RollerSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;
import frc.robot.utils.logging.commands.LoggableParallelCommandGroup;

public class StopAll extends LoggableParallelCommandGroup {
  private final RollerSubsystem subsystem;
  private final Timer timer;

  public StopAll(AnglerSubsystem anglerSubsystem, ClimberSubsystem climberSubsystem,
      SwerveSubsystem swerveSubsystem, FeederSubsystem feederSubsystem, HopperSubsystem hopperSubsystem,
      IntakeDeployerSubsystem intakeDeployerSubsystem, ShooterSubsystem shooterSubsystem,
      TurretSubsystem turretSubsystem) {
    timer = new Timer();
    this.subsystem = subsystem;
    addRequirements(subsystem);
  }

}