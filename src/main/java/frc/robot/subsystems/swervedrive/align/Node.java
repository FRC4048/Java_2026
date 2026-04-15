package frc.robot.subsystems.swervedrive.align;

import java.util.function.BooleanSupplier;

import edu.wpi.first.math.geometry.Pose2d;

public record Node(Pose2d pose, BooleanSupplier isActive){}
