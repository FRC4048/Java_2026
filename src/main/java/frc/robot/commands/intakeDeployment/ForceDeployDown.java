// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.intakeDeployment;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.constants.Constants;
import frc.robot.constants.enums.DeploymentState;
import frc.robot.constants.enums.ShootingState;
import frc.robot.constants.enums.ShootingState.ShootState;
import frc.robot.subsystems.IntakeDeployerSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class ForceDeployDown extends LoggableCommand {
  private final IntakeDeployerSubsystem subsystem;
  private final Timer timer;

  public ForceDeployDown(IntakeDeployerSubsystem subsystem) {
    this.subsystem = subsystem;
    timer = new Timer();
  }

  @Override
  public void initialize() {
    subsystem.setDeploymentState(DeploymentState.DOWN);
    timer.restart();
  }

  @Override
  public void execute() {
    subsystem.setSpeed(Constants.INITIAL_INTAKE_DEPLOYMENT_SPEED);
  }

  @Override
  public void end(boolean interrupted) {
  }

  @Override
  public boolean isFinished() {
    return subsystem.getRevLimitSwitchState() || timer.hasElapsed(Constants.INTAKE_DEPLOYER_TIMEOUT_TIMER);
  }
}
