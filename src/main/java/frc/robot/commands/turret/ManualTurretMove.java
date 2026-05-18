package frc.robot.commands.turret;

import java.util.function.DoubleSupplier;

import frc.robot.constants.Constants;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class ManualTurretMove extends LoggableCommand {
  private final TurretSubsystem subsystem;
  private final DoubleSupplier input;


  public ManualTurretMove(TurretSubsystem subsystem, DoubleSupplier input) {
    this.subsystem = subsystem;
    this.input = input;
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
    subsystem.manualMove(-input.getAsDouble()*Constants.SCALING_FACTOR);

  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return false;
  }
}
