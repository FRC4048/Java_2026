package frc.robot.utils.math;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.constants.Constants;
import org.littletonrobotics.junction.Logger;

/** Utility class for vectors used in projectile motion */
public class VectorUtils {

  /**
   * The projectile motion converts the given coords to 2d after applying the change in base coords
   * from the arc provided. Because shooter angle depends on position (not point math) and position
   * depends on angle (not point math), the function will approximate to a specified certainty by
   * calling the function at 45 degrees, getting that pose and then looping until the delta angle
   * result is within a specified margin, or you have exceeded the max iterations specified.
   *
   * @param speed the initial velocity of the projectile
   * @param centerArcX the x cord of the arc created by the shooter moving at different angles
   * @param centerArcY the y cord of the arc created by the shooter moving at different angles
   * @param centerArcZ the z cord of the arc created by the shooter moving at different angles
   * @param arcRadius the length of the shooter, which forms an arc when changing angle
   * @param destX the target position in the x direction
   * @param destY the target position in the y direction
   * @param destZ the target position in the z direction
   * @param degreeThreshold threshold before angles are considered equal when iterating over
   *     positions angle relationship created by arc
   * @param maxIterations the max amount of iterations before an angle is returned
   * @param maxFractionalRange double representing what fraction of range of the parabola is
   *     acceptable for hitting your target. <br>
   *     * For example if you have to hit your target from bottom, you can constrain the parabola to
   *     only be acceptable in the first third of the parabola.
   * @return a {@link VelocityVector} with the calculated target angle between 0 and 90 degrees.<br>
   *     <b>Impossible parameters will produce a null result</b>
   */
  @Deprecated
  public static VelocityVector fromArcAndDestAndVel(
      double speed,
      double centerArcX,
      double centerArcY,
      double centerArcZ,
      double arcRadius,
      double destX,
      double destY,
      double destZ,
      double degreeThreshold,
      int maxIterations,
      double maxFractionalRange) {
    double xDist = destX - centerArcX;
    double yDist = destY - centerArcY;
    VelocityVector lastVel = null;
    VelocityVector currVel = null;
    int i = 0;
    do {
      i++;
      double theta = Math.PI / 4;
      if (currVel != null) {
        lastVel = currVel;
        theta = currVel.getAngle().getRadians();
      }
      double rampXYOffset = (Math.cos(theta) * arcRadius);
      double rampZOffset = Math.sin(theta) * arcRadius;
      double xyDist = Math.hypot(xDist, yDist) - rampXYOffset;
      double z1 = centerArcZ + rampZOffset;
      double deltaZ = Math.abs(destZ - z1);
      currVel = fromVelAndDist(speed, xyDist, deltaZ, maxFractionalRange);
    } while (i < maxIterations
        && (lastVel == null
            || currVel == null
            || Math.abs(lastVel.getAngle().getDegrees() - currVel.getAngle().getDegrees())
                > degreeThreshold));
    return currVel;
  }

  /**
   * The projectile motion converts the given coords to 2d after applying the change in base coords
   * from the arc provided. Because shooter angle depends on position (not point math) and position
   * depends on angle (not point math), the function will approximate to a specified certainty by
   * calling the function at 45 degrees, getting that pose and then looping until the delta angle
   * result is within 0.01 degrees, or you have exceeded 7 iterations. <br>
   * To use custom certainty see {@link #fromArcAndDestAndVel(double, double, double, double,
   * double, double, double, double, double, int, double)}
   *
   * @param speed the initial velocity of the projectile
   * @param centerArcX the x cord of the arc created by the shooter moving at different angles
   * @param centerArcY the y cord of the arc created by the shooter moving at different angles
   * @param centerArcZ the z cord of the arc created by the shooter moving at different angles
   * @param arcRadius the length of the shooter, which forms an arc when changing angle
   * @param destX the target position in the x direction
   * @param destY the target position in the y direction
   * @param destZ the target position in the z direction
   * @return a {@link VelocityVector} with the calculated target angle between 0 and 90 degrees.<br>
   *     <b>Impossible parameters will produce a null result</b>
   */
  @Deprecated
  public static VelocityVector fromArcAndDestAndVel(
      double speed,
      double centerArcX,
      double centerArcY,
      double centerArcZ,
      double arcRadius,
      double destX,
      double destY,
      double destZ) {
    return fromArcAndDestAndVel(
        speed,
        centerArcX,
        centerArcY,
        centerArcZ,
        arcRadius,
        destX,
        destY,
        destZ,
        0.01,
        1,
        1.0 / 2);
  }

  /**
   * @param speed speed the projectile is moving at <br>
   *     <b>Must be > 0</b>
   * @param deltaX the X distance between the starting and ending positions <br>
   *     <b>Must be > 0</b>
   * @param deltaY the Y distance between the starting and ending positions <br>
   *     <b>Must be > 0</b>
   * @param maxFractionalRange double representing what fraction of range of the parabola is
   *     acceptable for hitting your target. <br>
   *     For example if you have to hit your target from bottom, you can constrain the parabola to
   *     only be acceptable in the first third of the parabola.
   * @return a {@link VelocityVector} with the calculated target angle between 0 and 90 degrees.<br>
   *     <b>Impossible parameters will produce a null result</b>
   */
  public static VelocityVector fromVelAndDist(
      double speed, double deltaX, double deltaY, double maxFractionalRange) {
    if (speed <= 0 || deltaX <= 0 || deltaY <= 0) return null;
    double tanOfAngle =
        (((deltaY >= 0) ? 1 : -1)
                    * Math.sqrt(
                        Math.pow(deltaX, 2)
                            * ((-1 * Math.pow(Constants.GRAVITY, 2) * Math.pow(deltaX, 2))
                                + (2 * Constants.GRAVITY * Math.pow(speed, 2) * deltaY)
                                + (Math.pow(speed, 4))))
                - (Math.pow(speed, 2) * deltaX))
            / (Constants.GRAVITY * Math.pow(deltaX, 2));
    VelocityVector velocityVector =
        new VelocityVector(speed, new Rotation2d(Math.atan(tanOfAngle)));
    return horizontalRange(velocityVector) * maxFractionalRange > deltaX ? velocityVector : null;
  }

  /**
   * @param velocityVector the initial velocity and angle of the projectile
   * @return the max range of a projectile following a theoretical parabola
   */
  public static double horizontalRange(VelocityVector velocityVector) {
    return ((Math.pow(velocityVector.getVelocity(), 2)
            * Math.sin(velocityVector.getAngle().getRadians() * 2)))
        / (-1 * Constants.GRAVITY);
  }

  /**
   * @param velocityVector the initial velocity and angle of the projectile
   * @return the max height of a projectile following a theoretical parabola
   */
  public static double maximumHeight(VelocityVector velocityVector) {
    return (Math.pow(velocityVector.getVelocity(), 2)
            * Math.pow(Math.sin(velocityVector.getAngle().getRadians()), 2))
        / (2 * Constants.GRAVITY * -1);
  }

  /**
   * The projectile motion converts the given coords to 2d and after applying the drivetrain x
   * velocity, to the x component of the projectile's initial velocity. <br>
   * Because shooter component velocities depends on the angle we shoot at, and the angle we shoot
   * depends on the composition of the component velocities. if we want to incorporate the
   * drivetrains velocity we must use an iterative approach. <br>
   * This function will approximate to a specified certainty by assuming a shot of 45 degrees and
   * splitting the velocity into its components. Then the function adds the drivetrains x velocity
   * to the x component and returns a new shooting angle based the combined vectors. <br>
   * This process repeats until max iterations is reached (returns the current angle) or the degree
   * threshold between iterations is less than the threshold provided
   *
   * @param speed the initial velocity of the projectile
   * @param startX the x cord of the base of the ramp
   * @param startY the y cord of the base of the ramp
   * @param startZ the z cord of the base of the ramp
   * @param driveSpeedX the drivetrain velocity in meters per second
   * @param destX the destination x position
   * @param destY the destination y position
   * @param destZ the destination z position
   * @param degreeThreshold the degree threshold between iteration before function is considered
   *     done
   * @param maxIterations the max iteration before iteration is considered done. Note it does not
   *     require that max iterations was reached if degree threshold predicate is met.
   * @param maxFractionalRange double representing what fraction of range of the parabola is
   *     acceptable for hitting your target. <br>
   *     For example if you have to hit your target from bottom, you can constrain the parabola to
   *     only be acceptable in the first third of the parabola.
   * @return a {@link VelocityVector} with the calculated target angle between 0 and 90 degrees.<br>
   *     <b>Impossible parameters will produce a null result</b>
   */
  // public static VelocityVector fromDestAndCompoundVel(double speed, double startX, double startY,
  // double startZ, double driveSpeedX, double destX, double destY, double destZ,double distOffset
  // ,double degreeThreshold, int maxIterations, double maxFractionalRange) {
  //     double xDist = destX - startX;
  //     double yDist = destY - startY;
  //     double xyDist = Math.hypot(xDist, yDist) + distOffset;
  //     double deltaZ = Math.abs(destZ - startZ);
  //     VelocityVector lastVel = null;
  //     VelocityVector currVel = null;
  //     int i = 0;
  //     do {
  //         i++;
  //         lastVel = currVel;
  //         double theta = lastVel == null ? Math.PI / 4 : lastVel.getAngle().getRadians();
  //         double xySpeed = (Math.cos(theta) * speed) + driveSpeedX;
  //         double zSpeed = Math.sin(theta) * speed;
  //         double appliedSpeed = Math.hypot(xySpeed, zSpeed);
  //         currVel = fromVelAndDist(appliedSpeed, xyDist, deltaZ, maxFractionalRange);
  //     } while (i < maxIterations && (lastVel == null || currVel == null ||
  // Math.abs(lastVel.getAngle().getDegrees() - currVel.getAngle().getDegrees()) >
  // degreeThreshold));
  //     return currVel == null ? new VelocityVector(Constants.SHOOTER_VELOCITY, new Rotation2d()) :
  // currVel;
  // }

  /**
   * The projectile motion converts the given coords to 2d and after applying the drivetrain x
   * velocity, to the x component of the projectile's initial velocity. <br>
   * Because shooter component velocities depends on the angle we shoot at, and the angle we shoot
   * depends on the composition of the component velocities. if we want to incorporate the
   * drivetrains velocity we must use an iterative approach. <br>
   * This function will approximate to a specified certainty by assuming a shot of 45 degrees and
   * splitting the velocity into its components. Then the function adds the drivetrains x velocity
   * to the x component and returns a new shooting angle based the combined vectors. <br>
   * This process repeats until the 10 iterations is reached (returns the current angle) or the
   * degree threshold of 0.01 between iterations is less than the threshold provided
   *
   * @param speed the initial velocity of the projectile
   * @param startX the x cord of the base of the ramp
   * @param startY the y cord of the base of the ramp
   * @param startZ the z cord of the base of the ramp
   * @param driveSpeedX the drivetrain velocity in meters per second
   * @param destX the destination x position
   * @param destY the destination y position
   * @param destZ the destination z position
   * @return a {@link VelocityVector} with the calculated target angle between 0 and 90 degrees.<br>
   *     <b>Impossible parameters will produce a null result</b>
   */
  // public static VelocityVector fromDestAndCompoundVel(double speed, double startX, double startY,
  // double startZ, double driveSpeedX, double destX, double destY, double destZ) {
  //     return fromDestAndCompoundVel(speed, startX, startY, startZ, driveSpeedX, destX, destY,
  // destZ, Constants.RAMP_FROM_CENTER,0.01, 10, 2.0/5);
  // }

  public static boolean isErrorSafe(
      VelocityVector velocityVector,
      Rotation2d angleError,
      double destX,
      double destZ,
      double maxErrorZ) {
    double highY =
        getYAtX(
            new VelocityVector(
                velocityVector.getVelocity(), velocityVector.getAngle().plus(angleError)),
            destX);
    double lowY =
        getYAtX(
            new VelocityVector(
                velocityVector.getVelocity(), velocityVector.getAngle().minus(angleError)),
            destX);
    Logger.recordOutput("VectorUtils/maxDiffY", highY - destZ);
    Logger.recordOutput("VectorUtils/minDiffY", destZ - lowY);
    return highY < destZ + maxErrorZ && lowY > destZ - maxErrorZ;
  }

  public static double getYAtX(VelocityVector velocityVector, double x) {
    double time = getTimeForXDist(velocityVector, x);
    return velocityVector.getYSpeed() * time + (Constants.GRAVITY * 0.5 * Math.pow(time, 2));
  }

  public static double getTimeForXDist(VelocityVector velocityVector, double x) {
    return x / velocityVector.getXSpeed();
  }
}
