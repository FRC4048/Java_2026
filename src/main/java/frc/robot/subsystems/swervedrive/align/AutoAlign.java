// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.swervedrive.align;

import java.util.function.Supplier;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;

public class AutoAlign extends Command {
  private Pose2d targetPose;
  private Pose2d InitlalTargetPose;
  private SwerveSubsystem drivebase;
  private AutoAlignGenerator generator;
  private int i = 0;
  public AutoAlign(Pose2d targetPose, SwerveSubsystem drivebase, AutoAlignGenerator generator) {
    this.drivebase = drivebase;
    this.targetPose = targetPose;
    this.generator = generator;
    this.InitlalTargetPose = targetPose;
  }

  @Override
  public void initialize() {
    i =0;
    generator.generatePath(targetPose);
  }

  @Override
  public void execute() {
    Pose2d robotPose = drivebase.getPose();
    Pose2d targetPose = generator.getTargetPath().get(i);
    Logger.recordOutput("targetPoseAlign", targetPose);
    double speedX = (robotPose.getX()-targetPose.getX())*-1;
    double speedY = (robotPose.getY()-targetPose.getY())*-1;
    speedY =  Math.abs(speedY) < 0.1 ? speedY*3 : speedY;
    speedX =  Math.abs(speedX) < 0.1 ? speedX*3  : speedX;
    if(targetPose.getTranslation().getDistance(drivebase.getPose().getTranslation()) > 0.1){
      drivebase.driveFieldOriented(new ChassisSpeeds(speedX,speedY,0));
    }else{
      i++;
    }
  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return InitlalTargetPose.getTranslation().getDistance(drivebase.getPose().getTranslation()) < 0.1;
  }
}
