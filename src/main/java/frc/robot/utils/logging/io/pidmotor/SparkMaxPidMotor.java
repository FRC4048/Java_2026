package frc.robot.utils.logging.io.pidmotor;

import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.*;
import com.revrobotics.spark.config.LimitSwitchConfig;
import com.revrobotics.spark.config.MAXMotionConfig.MAXMotionPositionMode;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import frc.robot.constants.Constants;

/**
 * A Wrapper utility to encapsulate the NEO motor with PID capability. This is simply a wrapper with
 * some convenient defaults and initializations that make programming the PID of the NEO easier.
 *
 * <p>
 * TODO: This does not yet support the external absolute encoder that may be needed
 * <p>
 * TODO: This does not yet support velocity PID or other advanced features
 */
public class SparkMaxPidMotor {
    public static final double RAMP_RATE = 0;

    // The neo motor controller
    private final SparkMax neoMotor;
    private final SparkMaxPidConfig pidConfig; // if using arbff we need to keep track of pidConfig
    // The built-in relative encoder
    private final RelativeEncoder encoder;
    // The built-in PID controller
    private final SparkClosedLoopController pidController;

    // The desired motor setpoint (for position, velocity, etc.)
    private double setPoint = 0.0;

    /**
     * Constructor using reasonable default values
     *
     * @param id the CAN ID for the controller
     */
    public SparkMaxPidMotor(int id, boolean usesMaxMotion) {
        this(id, new SparkMaxPidConfig(usesMaxMotion));
    }

    public SparkMaxPidMotor(int id, SparkMaxPidConfig pidConfig) {
        neoMotor = new SparkMax(id, SparkLowLevel.MotorType.kBrushless);
        this.pidConfig = pidConfig;
        encoder = neoMotor.getEncoder();

        pidController = neoMotor.getClosedLoopController();
        SparkMaxConfig config = new SparkMaxConfig();
        config
                .smartCurrentLimit(pidConfig.getCurrentLimit())
                .closedLoopRampRate(RAMP_RATE)
                .idleMode(IdleMode.kBrake);
        config
                .closedLoop
                .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
                .pid(pidConfig.getP(), pidConfig.getI(), pidConfig.getD())
                .iZone(pidConfig.getIZone())
                .outputRange(-1, 1)
                .feedForward.kV(pidConfig.getFF());

        if (pidConfig.getUsesMaxMotion()) {
            config
                    .closedLoop
                    .maxMotion
                    .cruiseVelocity(pidConfig.getMaxVelocity())
                    .maxAcceleration(pidConfig.getMaxAccel())
                    .positionMode(MAXMotionPositionMode.kMAXMotionTrapezoidal)
                    .allowedProfileError(pidConfig.getAllowedError());
        }

        config.limitSwitch
        .forwardLimitSwitchType(LimitSwitchConfig.Type.kNormallyOpen)
        .forwardLimitSwitchTriggerBehavior(LimitSwitchConfig.Behavior.kStopMovingMotor) 
        .reverseLimitSwitchType(LimitSwitchConfig.Type.kNormallyOpen)
        .reverseLimitSwitchTriggerBehavior(LimitSwitchConfig.Behavior.kStopMovingMotor);

        neoMotor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }
    public void idleMode(IdleMode mode){
        SparkMaxConfig config = new SparkMaxConfig();
        config.idleMode(mode);
        neoMotor.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
    }
    /**
     * Reconfigure the PID fully using some of the values from motor params.
     * This method uses the PID, iZone, FF, maxVelocity, maxAcceleration and allowedError to reconfigure
     * teh PidMotor.
     *
     * @param params the params to use
     */
    public void configurePID(SparkMaxPidConfig params) {
        SparkMaxConfig config = new SparkMaxConfig();
        config
                .closedLoop
                .pid(params.getP(), params.getI(), params.getD())
                .iZone(params.getIZone())
                .feedForward.kV(params.getFF());
        if (params.getUsesMaxMotion()) {
            config
                    .closedLoop
                    .maxMotion
                    .cruiseVelocity(pidConfig.getMaxVelocity())
                    .maxAcceleration(params.getMaxAccel())
                    .allowedProfileError(pidConfig.getAllowedError());
        }
        neoMotor.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
    }

    /**
     * Set the desired position using the relative encoder as a reference.
     *
     * @param position the desired motor position
     */
    public void setPidPosition(double position) {
        SparkBase.ControlType type = pidConfig.getUsesMaxMotion() ? SparkBase.ControlType.kMAXMotionPositionControl : SparkBase.ControlType.kPosition;
        pidController.setSetpoint(position, type);
        this.setPoint = position;
    }

    /**
     * Set the desired velocity (in RPM) using the relative encoder as a reference.
     *
     * @param velocity the desired motor velocity
     */
    public void setPidVelocity(double velocity) {
        SparkBase.ControlType type = pidConfig.getUsesMaxMotion() ? SparkBase.ControlType.kMAXMotionVelocityControl : SparkBase.ControlType.kVelocity;
        pidController.setSetpoint(velocity, type);
        this.setPoint = velocity;
    }

    public double getPidSetPoint() {
        return setPoint;
    }

    public void setPid(double pidP, double pidI, double pidD) {
        SparkMaxConfig config = new SparkMaxConfig();
        config.closedLoop.pid(pidP, pidI, pidD);
        neoMotor.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
    }

    public void setPid(double pidP, double pidI, double pidD, double iZone, double pidFF) {
        // TODO: Maybe use apply on the existing config to just get the new values
        SparkMaxConfig config = new SparkMaxConfig();
        config.closedLoop.pid(pidP, pidI, pidD).iZone(iZone).feedForward.kV(pidFF);
        neoMotor.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
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
}
