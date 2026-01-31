// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.pneumatics;

import frc.robot.commands.pneumatics.PneumaticsOff;
import frc.robot.commands.pneumatics.PneumaticsOn;
import frc.robot.commands.pneumatics.PneumaticsToggle;
import frc.robot.subsystems.PneumaticsSubsystem;
import frc.robot.utils.logging.commands.LoggableParallelCommandGroup;

public class DoubleToggle extends LoggableParallelCommandGroup {

  public DoubleToggle(PneumaticsSubsystem pneumaticsBottom, PneumaticsSubsystem pneumaticsTop) {
    super(
        new PneumaticsToggle(pneumaticsTop),
        new PneumaticsToggle(pneumaticsBottom));
  }
}