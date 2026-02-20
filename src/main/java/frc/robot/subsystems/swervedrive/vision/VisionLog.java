package frc.robot.subsystems.swervedrive.vision;

import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.subsystems.swervedrive.vision.truster.FilterResult;
import frc.robot.subsystems.swervedrive.vision.truster.VisionMeasurement;
import frc.robot.utils.Apriltag;

public record VisionLog(VisionMeasurement measurement, FilterResult result) {}
