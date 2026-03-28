// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.angler;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.AnglerSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class SetAnglerToAngle extends LoggableCommand {
  private final AnglerSubsystem angler;
  private final double targetAngle;
  public SetAnglerToAngle(AnglerSubsystem angler, double targetAngle) {
    this.angler = angler;
    this.targetAngle = targetAngle;
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    angler.setAngle(targetAngle);
    
  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return false;
  }
}
