package frc.robot.commands.climber;

import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class ResetClimberEncoder extends LoggableCommand {

  private final ClimberSubsystem subsystem;

  public ResetClimberEncoder(ClimberSubsystem subsystem) {
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
