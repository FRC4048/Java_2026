// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.turret;

import java.lang.ModuleLayer.Controller;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ControllerSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ToggleAdjustedPosition extends Command {

  private final ControllerSubsystem subsystem;
  public ToggleAdjustedPosition(ControllerSubsystem subsystem) {
    this.subsystem = subsystem;
  }

  @Override
  public void initialize() {
    subsystem.toggleAdjustedPosition();
  }

  @Override
  public void execute() {}

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return true;
  }
}
