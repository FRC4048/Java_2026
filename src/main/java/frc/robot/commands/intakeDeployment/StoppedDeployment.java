// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.intakeDeployment;

import frc.robot.constants.enums.DeploymentState;
import frc.robot.subsystems.IntakeDeployerSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class StoppedDeployment extends LoggableCommand {
  private final IntakeDeployerSubsystem subsystem;

  public StoppedDeployment(IntakeDeployerSubsystem subsystem) {
    this.subsystem = subsystem;
  }

  @Override
  public void initialize() {
    subsystem.setDeploymentState(subsystem.getDeploymentState() == DeploymentState.DOWN ? DeploymentState.STOPPED : DeploymentState.UP);
  }

  @Override
  public void execute() {
  }

  @Override
  public void end(boolean interrupted) {
  }

  @Override
  public boolean isFinished() {
    return true;
  }
}
