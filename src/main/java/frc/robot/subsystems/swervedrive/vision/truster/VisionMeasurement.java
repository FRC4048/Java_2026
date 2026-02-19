package frc.robot.subsystems.swervedrive.vision.truster;

import edu.wpi.first.math.geometry.Pose2d;

/**
 * @param measurement estimated robot position (meters) calculated from apriltag tag what tag produced the
 *     position
 * @param distanceFromTag distance (meters) estimated robot pose was from the tag
 * @param timeOfMeasurement time when the pose was measured (seconds)
 */
public record VisionMeasurement(
    Pose2d measurement, int tagNumber, double distanceFromTag, double timeOfMeasurement) {}
