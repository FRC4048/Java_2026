package frc.robot.subsystems.swervedrive.vision.truster;

import edu.wpi.first.math.geometry.Pose2d;

/**
 * @param measurement estimated robot position calculated from apriltag tag what tag produced the
 *     position
 * @param distanceFromTag distance estimated robot pose was from the tag
 * @param timeOfMeasurement difference between the time the measurement was received by the robot
 *     program and when it was sent over the network
 */
public record VisionMeasurement(
    Pose2d measurement, double distanceFromTag, double timeOfMeasurement) {}
