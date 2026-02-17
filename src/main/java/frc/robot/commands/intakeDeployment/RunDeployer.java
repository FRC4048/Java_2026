// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.intakeDeployment;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.constants.enums.DeploymentState;
import frc.robot.subsystems.IntakeDeployerSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class RunDeployer extends LoggableCommand {
  private final IntakeDeployerSubsystem subsystem;
  public RunDeployer(IntakeDeployerSubsystem subsystem) {
    this.subsystem = subsystem;
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    switch(subsystem.getDeploymentState()){
      case UP -> subsystem.setSpeed(0.1 * Constants.INTAKE_DEPLOYER_SPEED);
      case DOWN -> subsystem.setSpeed(0.1 * Constants.INTAKE_RETRACTION_SPEED);
      case STOPPED -> subsystem.stopMotors();
      default -> subsystem.stopMotors();
    }
  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return false;
  }
}
