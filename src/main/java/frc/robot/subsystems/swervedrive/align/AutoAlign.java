// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.swervedrive.align;

import java.util.function.Supplier;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class AutoAlign extends LoggableCommand {
  private Supplier<Pose2d> targetPose;
  private SwerveSubsystem drivebase;
  private AutoAlignGenerator generator;
  private int i = 0;
  public boolean finished;

  public AutoAlign(Supplier<Pose2d> targetPose, SwerveSubsystem drivebase, AutoAlignGenerator generator) {
    this.drivebase = drivebase;
    this.targetPose = targetPose;
    this.generator = generator;
  }

  public AutoAlign(Pose2d targetPose, SwerveSubsystem drivebase, AutoAlignGenerator generator) {
    this.drivebase = drivebase;
    this.targetPose = () -> {
      return targetPose;
    };
    this.generator = generator;
  }

  @Override
  public void initialize() {
    finished = false;
    i = 0;
    generator.generatePath(targetPose.get());
  }

  @Override
  public void execute() {
    try {
      Pose2d robotPose = drivebase.getPose();
      Pose2d targetPose = generator.getTargetPath().get(i);
      Logger.recordOutput("targetPoseAlign", targetPose);
      double speedX = (robotPose.getX() - targetPose.getX()) * -1;
      double speedY = (robotPose.getY() - targetPose.getY()) * -1;
      if (targetPose.getTranslation().getDistance(drivebase.getPose().getTranslation()) > 0.05) {
        drivebase.driveFieldOriented(new ChassisSpeeds(speedX, speedY, 0));
      } else {
        i++;
      }
    } catch (Exception e) {
      finished = true;
    }
  }

  @Override
  public void end(boolean interrupted) {
  }

  @Override
  public boolean isFinished() {
    return finished;
  }
}
