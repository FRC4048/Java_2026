// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.intakeDeployment;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.constants.enums.DeploymentState;
import frc.robot.subsystems.IntakeDeployerSubsystem;

public class SetDeploymentState extends Command {
  private final IntakeDeployerSubsystem subsystem;
  private final DeploymentState state;
  public SetDeploymentState(IntakeDeployerSubsystem subsystem, DeploymentState state) {
    this.subsystem = subsystem;
    this.state = state;
  }

  @Override
  public void initialize() {
    subsystem.setDeploymentState(state);
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
