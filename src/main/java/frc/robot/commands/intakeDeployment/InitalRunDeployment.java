// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.intakeDeployment;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.constants.Constants;
import frc.robot.subsystems.IntakeDeployerSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class InitalRunDeployment extends LoggableCommand {
  private final IntakeDeployerSubsystem subsystem;
  private final Timer timer;
  public InitalRunDeployment(IntakeDeployerSubsystem subsystem) {
    timer = new Timer();
    this.subsystem =  subsystem;
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    switch(subsystem.getDeploymentState()){
      case UP -> subsystem.setSpeed(Constants.INTAKE_DEPLOYER_SPEED);
      case DOWN -> subsystem.setSpeed(Constants.INTAKE_RETRACTION_SPEED);
      case STOPPED -> subsystem.stopMotors();
      default -> subsystem.stopMotors();
  }
}

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return  timer.hasElapsed(2);
  }
}
