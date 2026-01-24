package frc.robot.utils.math;

import edu.wpi.first.math.geometry.Rotation2d;

public class AngleUtils {
  /**
   * This is a simple method that subtracts the given angle from PI/2
   *
   * @param angle to find complement of
   * @return compliment of angle
   */
  public static Rotation2d compliment(Rotation2d angle) {
    return new Rotation2d(Math.PI / 2).minus(angle);
  }

  /**
   * Reduces angle to be between 0 and 2*PI
   *
   * @param angle to reduce
   * @return reduce angle
   */
  public static Rotation2d reduce(Rotation2d angle) {
    return new Rotation2d((angle.getRadians() % (2 * Math.PI)));
  }

  /**
   * Converts the angle to be between -PI/2 and PI/2. The function works by passing the sin of the
   * angle to the arcSin function resulting in an angle clamped between -PI/2 and PI/2 because of
   * the range of the arcSin function
   *
   * @param angle to normalize
   * @return normalized angle
   */
  public static Rotation2d normalize(Rotation2d angle) {
    return new Rotation2d(
        Math.asin(Math.sin(angle.getRadians()))); // can be simplified (reduce(angle+PI) - PI
  }

  public static double normalizeSwerveAngle(double angleInRad) {
    angleInRad %= 2 * Math.PI;
    if (angleInRad < 0) {
      angleInRad += 2 * Math.PI;
    }
    return angleInRad;
  }

  public static double normalizeAbsAngleRadians(double angleInRev) {
    return angleInRev * 2 * Math.PI;
  }
}
