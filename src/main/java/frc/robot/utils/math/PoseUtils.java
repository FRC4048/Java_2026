package frc.robot.utils.math;

import edu.wpi.first.math.geometry.*;

public class PoseUtils {
  /**
   * Estimates a future position based on the current position and velocities
   *
   * @param pose current pose
   * @param vx x velocity
   * @param vy y velocity
   * @param time time over which velocity is applied
   * @return Estimated position
   */
  public static Pose2d getFieldEstimatedFuturePose(Pose2d pose, double vx, double vy, double time) {
    return getFieldEstimatedFuturePose(pose, vx, vy, 0.0, time);
  }

  /**
   * Estimates a future position based on the current position and velocities
   *
   * @param pose current pose
   * @param vx x velocity
   * @param vy y velocity
   * @param time time over which velocity is applied
   * @return Estimated position
   */
  public static Transform2d getFieldEstimatedFuturePose(
      Transform2d pose, double vx, double vy, double time) {
    return getFieldEstimatedFuturePose(pose, vx, vy, 0.0, time);
  }

  /**
   * Estimates a future position based on the current position and velocities
   *
   * @param pose current pose
   * @param vx x velocity
   * @param vy y velocity
   * @param time time over which velocity is applied
   * @return Estimated position
   */
  public static Translation2d getFieldEstimatedFuturePose(
      Translation2d pose, double vx, double vy, double time) {
    return pose.plus(new Translation2d(vx * time, vy * time));
  }

  /**
   * Estimates a future position based on the current position and velocities
   *
   * @param pose current pose
   * @param vx x velocity
   * @param vy y velocity
   * @param vz z velocity
   * @param time time over which velocity is applied
   * @return Estimated position
   */
  public static Pose3d getFieldEstimatedFuturePose(
      Pose3d pose, double vx, double vy, double vz, double time) {
    return getFieldEstimatedFuturePose(pose, vx, vy, vz, 0.0, 0.0, 0.0, time);
  }

  /**
   * Estimates a future position based on the current position and velocities
   *
   * @param pose current pose
   * @param vx x velocity
   * @param vy y velocity
   * @param vz z velocity
   * @param time time over which velocity is applied
   * @return Estimated position
   */
  public static Transform3d getFieldEstimatedFuturePose(
      Transform3d pose, double vx, double vy, double vz, double time) {
    return getFieldEstimatedFuturePose(pose, vx, vy, vz, 0.0, 0.0, 0.0, time);
  }

  /**
   * Estimates a future position based on the current position and velocities
   *
   * @param pose current pose
   * @param vx x velocity
   * @param vy y velocity
   * @param vz z velocity
   * @param time time over which velocity is applied
   * @return Estimated position
   */
  public static Translation3d getFieldEstimatedFuturePose(
      Translation3d pose, double vx, double vy, double vz, double time) {
    return pose.plus(new Translation3d(vx * time, vy * time, vz * time));
  }

  /**
   * Estimates a future position based on the current position and velocities
   *
   * @param pose current pose
   * @param vx x velocity
   * @param vy y velocity
   * @param vYaw yaw velocity
   * @param time time over which velocity is applied
   * @return Estimated position
   */
  public static Pose2d getFieldEstimatedFuturePose(
      Pose2d pose, double vx, double vy, double vYaw, double time) {
    return pose.transformBy(new Transform2d(vx * time, vy * time, new Rotation2d(vYaw * time)));
  }

  /**
   * Estimates a future position based on the current position and velocities
   *
   * @param pose current pose
   * @param vx x velocity
   * @param vy y velocity
   * @param vYaw yaw velocity
   * @param time time over which velocity is applied
   * @return Estimated position
   */
  public static Transform2d getFieldEstimatedFuturePose(
      Transform2d pose, double vx, double vy, double vYaw, double time) {
    return pose.plus(new Transform2d(vx * time, vy * time, Rotation2d.fromDegrees(vYaw * time)));
  }

  /**
   * Estimates a future position based on the current position and velocities
   *
   * @param pose current pose
   * @param vx x velocity
   * @param vy y velocity
   * @param vz z velocity
   * @param vYaw yaw velocity
   * @param vRoll roll velocity
   * @param vPitch pitch velocity
   * @param time time over which velocity is applied
   * @return Estimated position
   */
  public static Pose3d getFieldEstimatedFuturePose(
      Pose3d pose,
      double vx,
      double vy,
      double vz,
      double vYaw,
      double vRoll,
      double vPitch,
      double time) {
    return pose.transformBy(
        new Transform3d(
            vx * time,
            vy * time,
            vz * time,
            new Rotation3d(vRoll * time, vPitch * time, vYaw * time)));
  }

  /**
   * Estimates a future position based on the current position and velocities
   *
   * @param pose current pose
   * @param vx x velocity
   * @param vy y velocity
   * @param vz z velocity
   * @param vYaw yaw velocity
   * @param vRoll roll velocity
   * @param vPitch pitch velocity
   * @param time time over which velocity is applied
   * @return Estimated position
   */
  public static Transform3d getFieldEstimatedFuturePose(
      Transform3d pose,
      double vx,
      double vy,
      double vz,
      double vYaw,
      double vRoll,
      double vPitch,
      double time) {
    return pose.plus(
        new Transform3d(
            vx * time,
            vy * time,
            vz * time,
            new Rotation3d(vRoll * time, vPitch * time, vYaw * time)));
  }

  /**
   * @param pose to modify
   * @param z to add
   * @param pitch to add
   * @param roll to add
   * @return 3D pose that inherits 2D attributes
   */
  public static Pose3d addDimension(Pose2d pose, double z, double pitch, double roll) {
    return new Pose3d(
        addDimension(pose.getTranslation(), z), addDimension(pose.getRotation(), pitch, roll));
  }

  /**
   * @param pose to modify
   * @param z to add
   * @return 3D pose that inherits 2D attributes
   */
  public static Translation3d addDimension(Translation2d pose, double z) {
    return new Translation3d(pose.getX(), pose.getY(), z);
  }

  /**
   * @param rotation to modify
   * @param pitch to add
   * @param roll to add
   * @return 3D rotation that inherits yaw of 2D rotation
   */
  public static Rotation3d addDimension(Rotation2d rotation, double pitch, double roll) {
    return new Rotation3d(roll, pitch, rotation.getRadians());
  }

  public static double slope(Translation2d startPose, Translation2d endPose) {
    return (endPose.getY() - startPose.getY()) / (endPose.getX() - startPose.getX());
  }

  public static double getDistance(Pose2d pose1, Pose2d pose2) {
    return Math.sqrt(
        Math.pow(pose1.getX() - pose2.getX(), 2) + Math.pow(pose1.getY() - pose2.getY(), 2));
  }
}
