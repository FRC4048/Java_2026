package frc.robot.commands.turret;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class ManualTurretMove extends LoggableCommand {
  private final TurretSubsystem subsystem;
  private final CommandXboxController controller;


  public ManualTurretMove(TurretSubsystem subsystem, CommandXboxController controller) {
    this.subsystem = subsystem;
    this.controller = controller;
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    subsystem.manualMove(-controller.getLeftX()/4);

  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return false;
  }
}
