package frc.robot.commands.turret;

import frc.robot.subsystems.TurretSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class ResetTurretEncoder extends LoggableCommand {

  private final TurretSubsystem subsystem;

  public ResetTurretEncoder(TurretSubsystem subsystem) {
    this.subsystem = subsystem;
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {
    subsystem.resetEncoderToZero();
  }

  @Override
  public boolean isFinished() {
    return true;
  }
}
