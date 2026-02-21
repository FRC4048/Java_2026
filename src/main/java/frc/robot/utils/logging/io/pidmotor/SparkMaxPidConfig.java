package frc.robot.utils.logging.io.pidmotor;

import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

/**
 * Value container helper class for configuring a PidMotor.
 */
public class SparkMaxPidConfig {
    public static final double DEFAULT_P = 0.01;
    public static final double DEFAULT_I = 0;
    public static final double DEFAULT_D = 0.0;
    public static final double DEFAULT_IZONE = 0.0;
    public static final double DEFAULT_FF = 0.0;
    public static final double MAX_VELOCITY = 5000;
    public static final double MAX_ACCELERATION = 10000;
    public static final double ALLOWED_ERROR = 1.0;
    public static final IdleMode DEFAULT_IDLE_MODE = IdleMode.kBrake;

    private double p = DEFAULT_P;
    private double i = DEFAULT_I;
    private double d = DEFAULT_D;
    private double iZone = DEFAULT_IZONE;
    private double ff = DEFAULT_FF;
    private int currentLimit = 20;
    private IdleMode mode = DEFAULT_MODE;
    /**
     * This is the cruise velocity for the MAX_MOTION config.
     */
    private double maxVelocity = MAX_VELOCITY;
    private double maxAccel = MAX_ACCELERATION;
    private double allowedError = ALLOWED_ERROR;
    private boolean usesMaxMotion;

    public SparkMaxPidConfig(boolean usesMaxMotion) {
        this.usesMaxMotion = usesMaxMotion;
    }

    public double getP() {
        return p;
    }

    public SparkMaxPidConfig setP(double p) {
        this.p = p;
        return this;
    }

    public double getI() {
        return i;
    }

    public SparkMaxPidConfig setI(double i) {
        this.i = i;
        return this;
    }

    public double getD() {
        return d;
    }

    public SparkMaxPidConfig setD(double d) {
        this.d = d;
        return this;
    }

    public double getIZone() {
        return iZone;
    }

    public SparkMaxPidConfig setIZone(double iZone) {
        this.iZone = iZone;
        return this;
    }

    public double getFF() {
        return ff;
    }

    public SparkMaxPidConfig setFF(double ff) {
        this.ff = ff;
        return this;
    }

    public int getCurrentLimit() {
        return currentLimit;
    }

    public SparkMaxPidConfig setCurrentLimit(int currentLimit) {
        this.currentLimit = currentLimit;
        return this;
    }

    public SparkMaxPidConfig setMaxVelocity(double maxVelocity) {
        this.maxVelocity = maxVelocity;
        return this;
    }

    public SparkMaxPidConfig setMaxAccel(double maxAccel) {
        this.maxAccel = maxAccel;
        return this;
    }

    public SparkMaxPidConfig setAllowedError(double allowedError) {
        this.allowedError = allowedError;
        return this;
    }

    public SparkMaxPidConfig setPid(double p, double i, double d) {
        setP(p).setI(i).setD(d);
        return this;
    }

    public SparkMaxPidConfig setPidf(double p, double i, double d, double ff) {
        setPid(p, i, d).setFF(ff);
        return this;
    }

    public SparkMaxPidConfig setTrapezoidConstructions(double maxVelocity, double maxAccel) {
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

    public SparkMaxPidConfig setIdleMode(IdleMode mode){
        this.mode = mode;
        return this;
    }

    public IdleMode getIdleMode(){
        return mode;
    }
}

