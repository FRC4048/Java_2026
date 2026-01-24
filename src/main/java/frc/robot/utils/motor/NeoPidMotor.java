package frc.robot.utils.motor;

import static com.revrobotics.spark.SparkBase.PersistMode.kNoPersistParameters;
import static com.revrobotics.spark.SparkBase.PersistMode.kPersistParameters;
import static com.revrobotics.spark.SparkBase.ResetMode.kNoResetSafeParameters;
import static com.revrobotics.spark.SparkBase.ResetMode.kResetSafeParameters;
import static com.revrobotics.spark.config.SparkBaseConfig.IdleMode.kBrake;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.*;
import com.revrobotics.spark.config.ClosedLoopConfig;
import com.revrobotics.spark.config.LimitSwitchConfig;
import com.revrobotics.spark.config.MAXMotionConfig.MAXMotionPositionMode;
import com.revrobotics.spark.config.SparkMaxConfig;

/**
 * A Wrapper utility to encapsulate the NEO motor with PID capability. This is simply a wrapper with
 * some convenient defaults and initializations that make programming the PID of the NEO easier.
 *
 * <p>TODO: This does not yet support the external absolute encoder that may be needed TODO: This
 * does not yet support velocity PID or other advanced features
 */
public class NeoPidMotor {

  public static final double RAMP_RATE = 0;

  // The neo motor controller
  private final SparkMax neoMotor;
  private final NeoPidConfig pidConfig; // if using arbff we need to keep track of pidConfig
  // The built-in relative encoder
  private final RelativeEncoder encoder;
  // The built-in PID controller
  private final SparkClosedLoopController pidController;

  // The desired motor position
  private double setPosition = 0.0;

  /**
   * Constructor using reasonable default values
   *
   * @param id the CAN ID for the controller
   */
  public NeoPidMotor(int id, boolean usesMaxMotion) {
    this(id, new NeoPidConfig(usesMaxMotion));
  }

  public NeoPidMotor(int id, NeoPidConfig pidConfig) {
    neoMotor = new SparkMax(id, SparkLowLevel.MotorType.kBrushless);
    this.pidConfig = pidConfig;
    encoder = neoMotor.getEncoder();

    pidController = neoMotor.getClosedLoopController();
    SparkMaxConfig config = new SparkMaxConfig();
    config
        .smartCurrentLimit(pidConfig.getCurrentLimit())
        .closedLoopRampRate(RAMP_RATE)
        .idleMode(kBrake);
    config
        .closedLoop
        .feedbackSensor(ClosedLoopConfig.FeedbackSensor.kPrimaryEncoder)
        .pid(pidConfig.getP(), pidConfig.getI(), pidConfig.getD())
        .velocityFF(pidConfig.getFF())
        .iZone(pidConfig.getIZone())
        .outputRange(-1, 1);
    if (pidConfig.getUsesMaxMotion()) {
      config
          .closedLoop
          .maxMotion
          .maxVelocity(pidConfig.getMaxVelocity())
          .maxAcceleration(pidConfig.getMaxAccel())
          .positionMode(MAXMotionPositionMode.kMAXMotionTrapezoidal)
          .allowedClosedLoopError(pidConfig.getAllowedError());
    }

    config
        .limitSwitch
        .forwardLimitSwitchEnabled(true)
        .forwardLimitSwitchType(LimitSwitchConfig.Type.kNormallyOpen)
        .reverseLimitSwitchEnabled(true)
        .forwardLimitSwitchType(LimitSwitchConfig.Type.kNormallyOpen);

    neoMotor.configure(config, kResetSafeParameters, kPersistParameters);
  }

  /**
   * Reconfigure the PID fully using all values from motor params
   *
   * @param params
   */
  public void configurePID(NeoPidConfig params) {
    SparkMaxConfig config = new SparkMaxConfig();
    config
        .closedLoop
        .pid(params.getP(), params.getI(), params.getD())
        .velocityFF(params.getFF())
        .iZone(params.getIZone());
    if (params.getUsesMaxMotion()) {
      config
          .closedLoop
          .maxMotion
          .maxVelocity(params.getMaxVelocity())
          .maxAcceleration(params.getMaxAccel())
          .allowedClosedLoopError(params.getAllowedError());
    }
    neoMotor.configure(config, kNoResetSafeParameters, kNoPersistParameters);
  }

  /**
   * Set the desired position using the relative encoder as a reference
   *
   * @param position the desired motor position
   */
  public void setPidPos(double position, SparkBase.ControlType controlType) {
    pidController.setReference(position, controlType);
    this.setPosition = position;
  }

  public double getPidPosition() {
    return setPosition;
  }

  public void setPid(double pidP, double pidI, double pidD) {
    SparkMaxConfig config = new SparkMaxConfig();
    config.closedLoop.pid(pidP, pidI, pidD);
    neoMotor.configure(config, kNoResetSafeParameters, kNoPersistParameters);
  }

  public void setPid(double pidP, double pidI, double pidD, double iZone, double pidFF) {
    SparkMaxConfig config = new SparkMaxConfig();
    config.closedLoop.pid(pidP, pidI, pidD).velocityFF(pidFF).iZone(iZone);
    neoMotor.configure(config, kNoResetSafeParameters, kNoPersistParameters);
  }

  public SparkMax getNeoMotor() {
    return neoMotor;
  }

  public RelativeEncoder getEncoder() {
    return encoder;
  }

  public SparkClosedLoopController getPidController() {
    return pidController;
  }

  public void configure(NeoPidConfig config) {
    SparkMaxConfig sparkMaxConfig = new SparkMaxConfig();
    sparkMaxConfig
        .closedLoop
        .pidf(config.getP(), config.getI(), config.getD(), config.getFF())
        .iZone(config.getIZone())
        .maxMotion
        .maxVelocity(config.getMaxVelocity())
        .maxAcceleration(config.getMaxAccel())
        .allowedClosedLoopError(config.getAllowedError());
    getNeoMotor().configure(sparkMaxConfig, kResetSafeParameters, kNoPersistParameters);
  }
}
