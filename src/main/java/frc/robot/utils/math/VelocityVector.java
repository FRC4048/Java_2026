package frc.robot.utils.math;

import edu.wpi.first.math.geometry.Rotation2d;

public class VelocityVector {
  private final Rotation2d angle;
  private final double velocity;

  public VelocityVector(double speed, Rotation2d angle) {
    this.velocity = speed;
    this.angle = angle;
  }

  public double getXSpeed() {
    return Math.cos(angle.getRadians()) * velocity;
  }

  public double getYSpeed() {
    return Math.sin(angle.getRadians()) * velocity;
  }

  public double getVelocity() {
    return velocity;
  }

  public Rotation2d getAngle() {
    return angle;
  }
}
