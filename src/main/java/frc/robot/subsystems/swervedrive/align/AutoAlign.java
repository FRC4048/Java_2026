// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.swervedrive.align;

import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;

public class AutoAlign extends Command {
  private Supplier<Pose2d> targetPose;
  private SwerveSubsystem drivebase;
  public AutoAlign(Supplier<Pose2d> targetPose, SwerveSubsystem drivebase) {
    this.drivebase = drivebase;
    this.targetPose = targetPose;
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    Pose2d robotPose = drivebase.getPose();
    Pose2d targetPose = this.targetPose.get();
    drivebase.driveFieldOriented(new ChassisSpeeds((robotPose.getX()-targetPose.getX())*-1,(robotPose.getY()-targetPose.getY())*-1,0));
  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return targetPose.get().getTranslation().getDistance(drivebase.getPose().getTranslation()) < 0.1;
  }
}
