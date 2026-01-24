// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.drive;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;

public class Drive extends Command {

  private final SwerveSubsystem drivebase;
  private final Pose2d pose;
  private final boolean fieldRelative;
  private Timer timer;
  public Drive(SwerveSubsystem drivebase, Pose2d pose, boolean fieldRelative) {
    timer = new Timer();
    this.drivebase = drivebase;
    this.pose = pose;
    this.fieldRelative = fieldRelative;
    addRequirements(drivebase);
  }


  @Override
  public void initialize() {
    timer.restart();
  }

 
  @Override
  public void execute() {
    drivebase.drive(pose.getTranslation(),pose.getRotation().getRadians(), fieldRelative);

  }

  @Override
  public void end(boolean interrupted) {

  }


  @Override
  public boolean isFinished() {
   if (timer.hasElapsed(Constants.DRIVE_TO_POSE_TIMEOUT)) {
      return true;
    } else{
      return false;
    }
  }
}
