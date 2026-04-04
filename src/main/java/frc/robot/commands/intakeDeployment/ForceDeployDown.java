// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.intakeDeployment;

import frc.robot.constants.enums.DeploymentState;
import frc.robot.constants.enums.ShootingState;
import frc.robot.constants.enums.ShootingState.ShootState;
import frc.robot.subsystems.IntakeDeployerSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class ForceDeployDown extends LoggableCommand {
  private final IntakeDeployerSubsystem subsystem;
  private final ShootingState state;

  public ForceDeployDown(IntakeDeployerSubsystem subsystem, ShootingState state) {
    this.subsystem = subsystem;
    this.state = state;
  }

  @Override
  public void initialize() {
    if(state.getShootState() == ShootState.SHOOTING_HUB || state.getShootState() == ShootState.SHUTTLING){
    subsystem.setDeploymentState(DeploymentState.DOWN);
    }
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
