package frc.robot.utils.logging.io.pidmotor;

import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

/**
 * Value container helper class for configuring a PidMotor.
 */
public class SparkMaxPidConfig {
    public static final ClosedLoopSlot DEFAULT_SLOT = ClosedLoopSlot.kSlot0;
    public static final double DEFAULT_P = 0.01;
    public static final double DEFAULT_I = 0;
    public static final double DEFAULT_D = 0.0;
    public static final double DEFAULT_IZONE = 0.0;
    public static final double DEFAULT_KV = 0.0;
    public static final double DEFAULT_KA = 0.0;
    public static final double DEFAULT_KS = 0.0;
    public static final double MAX_VELOCITY = 5000;
    public static final double MAX_ACCELERATION = 10000;
    public static final double ALLOWED_ERROR = 1.0;
    public static final IdleMode DEFAULT_IDLE_MODE = IdleMode.kBrake;

    private ClosedLoopSlot closedLoopSlot;
    private double p = DEFAULT_P;
    private double i = DEFAULT_I;
    private double d = DEFAULT_D;
    private double iZone = DEFAULT_IZONE;
    private double kv = DEFAULT_KV;
    private double ka = DEFAULT_KA;
    private double ks = DEFAULT_KS;
    private int currentLimit = 20;
    private IdleMode mode = DEFAULT_IDLE_MODE;
    /**
     * This is the cruise velocity for the MAX_MOTION config.
     */
    private double maxVelocity = MAX_VELOCITY;
    private double maxAccel = MAX_ACCELERATION;
    private double allowedError = ALLOWED_ERROR;
    private boolean usesMaxMotion;

    public SparkMaxPidConfig(boolean usesMaxMotion) {
        this(usesMaxMotion, DEFAULT_SLOT);
    }

    public SparkMaxPidConfig(boolean usesMaxMotion, ClosedLoopSlot closedLoopSlot) {
        this.usesMaxMotion = usesMaxMotion;
        this.closedLoopSlot = closedLoopSlot;
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

    public double getKV() {
        return kv;
    }

    public SparkMaxPidConfig setKV(double kv) {
        this.kv = kv;
        return this;
    }
    public double getKA() {
        return ka;
    }

    public SparkMaxPidConfig setKA(double ka) {
        this.ka = ka;
        return this;
    }
    public double getKS() {
        return ks;
    }

    public SparkMaxPidConfig setKS(double ks) {
        this.ks = ks;
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

    public SparkMaxPidConfig setPid(double p, double i, double d, double kv, double ka, double ks) {
        setPid(p, i, d)
        .setKV(kv)
        .setKA(ka)
        .setKS(ks);
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

    public ClosedLoopSlot getClosedLoopSlot() {
        return closedLoopSlot;
    }

    public SparkMaxPidConfig setIdleMode(IdleMode mode){
        this.mode = mode;
        return this;
    }

    public IdleMode getIdleMode(){
        return mode;
    }
}

