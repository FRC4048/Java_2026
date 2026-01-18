package frc.robot.utils.diag;

import com.revrobotics.spark.SparkMax;

/**
 * Diagnostics class for digital switch connected directly to the talon SRX it is a DiagBoolean
 * subclass.
 */
public class DiagSparkMaxSwitch extends DiagBoolean {
  public DiagSparkMaxSwitch(String title, String name) {
    super(title, name);
  }

  public enum Direction {
    FORWARD,
    REVERSE
  };

  private SparkMax canSparkMax;
  private Direction direction;

  /*
   * Constructor
   *
   * @param name      -the name of the unit. Will be used on the Shuffleboard
   * @param talonSRX  -the talon SRX to read the switch value from
   */

  public DiagSparkMaxSwitch(String title, String name, SparkMax canSparkMax, Direction direction) {
    super(title, name);
    this.canSparkMax = canSparkMax;
    this.direction = direction;
  }

  @Override
  protected boolean getValue() {
    switch (direction) {
      case FORWARD:
        return canSparkMax.getForwardLimitSwitch().isPressed();
      case REVERSE:
        return canSparkMax.getReverseLimitSwitch().isPressed();
      default:
        return false;
    }
  }
}
