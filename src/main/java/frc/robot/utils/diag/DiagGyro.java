package frc.robot.utils.diag;

import frc.robot.subsystems.gyro.ThreadedGyro;

public class DiagGyro extends DiagDistanceTraveled {
  private final ThreadedGyro gyro;

  public DiagGyro(String title, String name, double requiredTravel, ThreadedGyro gyro) {
    super(title, name, requiredTravel);
    this.gyro = gyro;
  }

  @Override
  protected double getCurrentValue() {
    return gyro.getGyroValue();
  }
}
