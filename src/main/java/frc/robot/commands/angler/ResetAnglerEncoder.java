package frc.robot.commands.angler;

import frc.robot.subsystems.AnglerSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class ResetAnglerEncoder extends LoggableCommand {

  private final AnglerSubsystem subsystem;

  public ResetAnglerEncoder(AnglerSubsystem subsystem) {
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
