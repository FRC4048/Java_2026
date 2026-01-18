package frc.robot.utils.motor;

public class NeoPidConfig {
  public static final double DEFAULT_P = 0.01;
  public static final double DEFAULT_I = 0;
  public static final double DEFAULT_D = 0.0;
  public static final double DEFAULT_IZONE = 0.0;
  public static final double DEFAULT_FF = 0.0;
  public static final double MAX_VELOCITY = 5000;
  public static final double MAX_ACCELERATION = 10000;
  public static final double ALLOWED_ERROR = 1.0;

  private double p = DEFAULT_P;
  private double i = DEFAULT_I;
  private double d = DEFAULT_D;
  private double iZone = DEFAULT_IZONE;
  private double ff = DEFAULT_FF;
  private int currentLimit; // TODO: SHould this be set to 40 as a default?
  private double maxVelocity = MAX_VELOCITY;
  private double maxAccel = MAX_ACCELERATION;
  private double allowedError = ALLOWED_ERROR;
  private boolean usesMaxMotion;

  public NeoPidConfig(boolean usesMaxMotion) {
    this.usesMaxMotion = usesMaxMotion;
  }

  public double getP() {
    return p;
  }

  public NeoPidConfig setP(double p) {
    this.p = p;
    return this;
  }

  public double getI() {
    return i;
  }

  public NeoPidConfig setI(double i) {
    this.i = i;
    return this;
  }

  public double getD() {
    return d;
  }

  public NeoPidConfig setD(double d) {
    this.d = d;
    return this;
  }

  public double getIZone() {
    return iZone;
  }

  public NeoPidConfig setIZone(double iZone) {
    this.iZone = iZone;
    return this;
  }

  public double getFF() {
    return ff;
  }

  public NeoPidConfig setFF(double ff) {
    this.ff = ff;
    return this;
  }

  public int getCurrentLimit() {
    return currentLimit;
  }

  public NeoPidConfig setCurrentLimit(int currentLimit) {
    this.currentLimit = currentLimit;
    return this;
  }

  public NeoPidConfig setMaxVelocity(double maxVelocity) {
    this.maxVelocity = maxVelocity;
    return this;
  }

  public NeoPidConfig setMaxAccel(double maxAccel) {
    this.maxAccel = maxAccel;
    return this;
  }

  public NeoPidConfig setAllowedError(double allowedError) {
    this.allowedError = allowedError;
    return this;
  }

  public NeoPidConfig setPid(double p, double i, double d) {
    setP(p).setI(i).setD(d);
    return this;
  }

  public NeoPidConfig setPidf(double p, double i, double d, double ff) {
    setPid(p, i, d).setFF(ff);
    return this;
  }

  public NeoPidConfig setTrapezoidConstructions(double maxVelocity, double maxAccel) {
    setMaxVelocity(maxVelocity).setMaxAccel(maxAccel);
    return this;
  }

  public double getMaxVelocity() {
    return maxVelocity;
  }

  public double getMaxAccel() {
    return maxAccel;
  }

  public double getAllowedError() {
    return allowedError;
  }

  public boolean getUsesMaxMotion() {
    return usesMaxMotion;
  }
}
