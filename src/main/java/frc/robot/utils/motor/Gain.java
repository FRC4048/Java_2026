package frc.robot.utils.motor;

public class Gain {
  private final double v;
  private final double s;

  public Gain(double v, double s) {
    this.v = v;
    this.s = s;
  }

  public double getV() {
    return v;
  }

  public double getS() {
    return s;
  }

  public static Gain of(double v, double s) {
    return new Gain(v, s);
  }
}
