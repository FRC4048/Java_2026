package frc.robot.subsystems.swervedrive.vision.truster;

import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.utils.Apriltag;

/**
 * @param measurement estimated robot position (meters) calculated from apriltag tag what tag produced the
 *     position
 * @param distanceFromTag distance (meters) estimated robot pose was from the tag
 * @param stdDevFromRio standard deviation provided by the AprilTag source over TCP
 * @param timeOfMeasurement time when the pose was measured (seconds)
 */
public record VisionMeasurement(
    Pose2d measurement, double distanceFromTag, double stdDev, double timeOfMeasurement) {}
