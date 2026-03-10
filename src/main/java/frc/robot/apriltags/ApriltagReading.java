package frc.robot.apriltags;

public record ApriltagReading(
        double posX,
        double posY,
        double poseYaw,
        double distanceToTag,
        double cameraToTagAngle,
        int apriltagNumber,
        double latency,
        double stdDev,
        double measurementTime) {}