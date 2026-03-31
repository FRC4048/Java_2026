// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.drive;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.constants.enums.DriveDirection;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class DriveSwerve extends LoggableCommand {

  private final SwerveSubsystem drivebase;
  private final double time;
  private final DriveDirection dir;
  private final double speed;
  private Timer timer;

  public DriveSwerve(SwerveSubsystem drivebase, DriveDirection dir, double time, double speed) {
    timer = new Timer();
    this.time = time;
    this.speed = speed;
    this.dir = dir;
    this.drivebase = drivebase;
    addRequirements(drivebase);
  }

  @Override
  public void initialize() {
    timer.restart();
  }

  @Override
  public void execute() {
    switch (dir) {
      case BACKWARD -> drivebase.drive(new Translation2d(speed, 0), 0, true);
      case FORWARD -> drivebase.drive(new Translation2d(-speed, 0), 0, true);
      case LEFT -> drivebase.drive(new Translation2d(0, -speed), 0, true);
      case RIGHT -> drivebase.drive(new Translation2d(0, speed), 0, true);
    }
  }

  @Override
  public void end(boolean interrupted) {
    drivebase.drive(new Translation2d(), 0, false);
  }

  @Override
  public boolean isFinished() {
    return timer.hasElapsed(time);
  }
}
