package frc.robot.utils.diag;

import frc.robot.utils.logging.input.GyroValues;
import frc.robot.utils.logging.io.gyro.ThreadedGyro;

public class DiagGyro extends DiagDistanceTraveled {
  private final ThreadedGyro gyro;

  public DiagGyro(String title, String name, double requiredTravel, ThreadedGyro gyro) {
    super(title, name, requiredTravel);
    this.gyro = gyro;
  }
//broken currently fix return
  @Override
  protected double getCurrentValue() {
    return 0;
  }
}