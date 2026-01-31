// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.drive;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;
/**
 * Drives the robot 
 * @param speedX Speed in the x direction in meters per second
 * @param speedY Speed in the Y direction in meters per second
 * @param fieldRelative True for field-relative, false for robot-relative.
 * @param time Amount of time before the command ends
 */
public class DriveDirectionTime extends LoggableCommand {

  private final SwerveSubsystem drivebase;
  private final double speedX;
  private final double speedY;
  private final boolean fieldRelative;
  private final double time;
  private Timer timer;
  public DriveDirectionTime(SwerveSubsystem drivebase, double speedX, double speedY, boolean fieldRelative, double time) {
    timer = new Timer();
    this.time = time;
    this.drivebase = drivebase;
    this.speedX = speedX;
    this.speedY = speedY;
    this.fieldRelative = fieldRelative;
    addRequirements(drivebase);
  }


  @Override
  public void initialize() {
    timer.restart();
  }

 
  @Override
  public void execute() {
    drivebase.drive(new Translation2d(speedX,speedY),0, fieldRelative);

  }

  @Override
  public void end(boolean interrupted) {
    drivebase.drive(new Translation2d(0,0),0, fieldRelative);
  }


  @Override
  public boolean isFinished() {
    return timer.hasElapsed(time);
  }
}
