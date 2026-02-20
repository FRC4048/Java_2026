package frc.robot.subsystems.swervedrive.vision.truster;

import edu.wpi.first.math.geometry.Pose2d;


public interface VisionTransformer {
  Pose2d getVisionPose(VisionMeasurement measurement);
}
