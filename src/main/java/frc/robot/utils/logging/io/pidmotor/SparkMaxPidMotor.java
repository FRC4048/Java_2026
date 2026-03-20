package frc.robot.utils.logging.io.pidmotor;

import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.*;
import com.revrobotics.spark.config.LimitSwitchConfig;
import com.revrobotics.spark.config.MAXMotionConfig.MAXMotionPositionMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A Wrapper utility to encapsulate the NEO motor with PID capability. This is simply a wrapper with
 * some convenient defaults and initializations that make programming the PID of the NEO easier.
 *
 * <p>
 * TODO: This does not yet support the external absolute encoder that may be needed
 */
public class SparkMaxPidMotor {
    public static final double RAMP_RATE = 0;

    // The neo motor controller
    private final SparkMax neoMotor;
    // pidConfigs by slots
    private final Map<ClosedLoopSlot, SparkMaxPidConfig> pidConfigs;
    // The built-in relative encoder
    private final RelativeEncoder encoder;
    // The built-in PID controller
    private final SparkClosedLoopController pidController;

    // The desired motor setpoint (for position, velocity, etc.)
    private double setPoint = 0.0;

    /**
     * Constructor using reasonable default values.
     * This constructs a PidMotor with the default values (and slot0) as the single config used
     *
     * @param id            the CAN ID for the controller
     * @param usesMaxMotion whether to use MaxMotion for the config
     */
    public SparkMaxPidMotor(int id, boolean usesMaxMotion) {
        this(id, new SparkMaxPidConfig(usesMaxMotion));
    }

    /**
     * Constructor using multiple (1 or more) configs.
     * The first config will be used for all non-slot-specific fields, then every config for its specific slot.
     *
     * @param id         the CAN ID for the controller
     * @param pidConfigs 1 or more configs to use, a config for each slot needed
     */
    public SparkMaxPidMotor(int id, SparkMaxPidConfig... pidConfigs) {
        neoMotor = new SparkMax(id, SparkLowLevel.MotorType.kBrushless);
        encoder = neoMotor.getEncoder();
        pidController = neoMotor.getClosedLoopController();

        // sanity check
        if (pidConfigs.length == 0) {
            System.out.println("WARNING: no pid configs given to SparkMaxPidMotor");
        }
        // This will fail if the keys are not unique (two configs have the same slot)
        this.pidConfigs = Arrays.stream(pidConfigs).collect(Collectors.toMap(SparkMaxPidConfig::getClosedLoopSlot, config -> config));

        SparkMaxConfig config = new SparkMaxConfig();
        // use the non-slot-specific values from the first config
        config
                .smartCurrentLimit(pidConfigs[0].getCurrentLimit())
                .closedLoopRampRate(RAMP_RATE)
                .idleMode(pidConfigs[0].getIdleMode());
        config
                .closedLoop
                .feedbackSensor(FeedbackSensor.kPrimaryEncoder);

        config
                .limitSwitch
                .forwardLimitSwitchType(LimitSwitchConfig.Type.kNormallyOpen)
                .forwardLimitSwitchTriggerBehavior(LimitSwitchConfig.Behavior.kStopMovingMotor)
                .reverseLimitSwitchType(LimitSwitchConfig.Type.kNormallyOpen)
                .reverseLimitSwitchTriggerBehavior(LimitSwitchConfig.Behavior.kStopMovingMotor);

        // use the slot-specific values from all configs
        for (SparkMaxPidConfig pidConfig : pidConfigs) {
            ClosedLoopSlot closedLoopSlot = pidConfig.getClosedLoopSlot();
            config
                    .closedLoop
                    .pid(pidConfig.getP(), pidConfig.getI(), pidConfig.getD(), closedLoopSlot)
                    .iZone(pidConfig.getIZone(), closedLoopSlot)
                    .outputRange(-1, 1, closedLoopSlot)
                    .feedForward.kV(pidConfig.getFF(), closedLoopSlot);

            if (pidConfig.getUsesMaxMotion()) {
                config
                        .closedLoop
                        .maxMotion
                        .cruiseVelocity(pidConfig.getMaxVelocity(), closedLoopSlot)
                        .maxAcceleration(pidConfig.getMaxAccel(), closedLoopSlot)
                        .positionMode(MAXMotionPositionMode.kMAXMotionTrapezoidal, closedLoopSlot)
                        .allowedProfileError(pidConfig.getAllowedError(), closedLoopSlot);
            } else {
                config
                        .closedLoop
                        .allowedClosedLoopError(pidConfig.getAllowedError(), closedLoopSlot);
            }
        }
        // actually use the config
        neoMotor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    /**
     * Reconfigure the PID fully using some of the values from motor params.
     * This method uses the PID, iZone, FF, maxVelocity, maxAcceleration and allowedError to reconfigure
     * the PidMotor.
     *
     * @param params the params to use
     */
    public void configurePID(SparkMaxPidConfig params) {
        SparkMaxConfig config = new SparkMaxConfig();
        ClosedLoopSlot closedLoopSlot = params.getClosedLoopSlot();
        config
                .closedLoop
                .pid(params.getP(), params.getI(), params.getD(), closedLoopSlot)
                .iZone(params.getIZone(), closedLoopSlot)
                .feedForward.kV(params.getFF(), closedLoopSlot);
        if (params.getUsesMaxMotion()) {
            config
                    .closedLoop
                    .maxMotion
                    .cruiseVelocity(params.getMaxVelocity(), closedLoopSlot)
                    .maxAcceleration(params.getMaxAccel(), closedLoopSlot)
                    .allowedProfileError(params.getAllowedError(), closedLoopSlot);
        } else {
            config
                    .closedLoop
                    .allowedClosedLoopError(params.getAllowedError(), closedLoopSlot);
        }
        neoMotor.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
    }

    /**
     * Set the desired position using the relative encoder as a reference.
     * This defaults to slot 0.
     *
     * @param position the desired motor position
     */
    public void setPidPosition(double position) {
        setPidPosition(position, ClosedLoopSlot.kSlot0);
    }

    /**
     * Set the set point position and switch to the given slot.
     *
     * @param position       the desired motor position
     * @param closedLoopSlot the slot to use
     */
    public void setPidPosition(double position, ClosedLoopSlot closedLoopSlot) {
        SparkBase.ControlType type = pidConfigs.get(closedLoopSlot).getUsesMaxMotion() ? SparkBase.ControlType.kMAXMotionPositionControl : SparkBase.ControlType.kPosition;
        pidController.setSetpoint(position, type, closedLoopSlot);
        this.setPoint = position;
    }

    /**
     * Set the desired velocity (in RPM) using the relative encoder as a reference.
     * This defaults to slot 0.
     *
     * @param velocity the desired motor velocity
     */
    public void setPidVelocity(double velocity) {
        setPidVelocity(velocity, ClosedLoopSlot.kSlot0);
    }

    /**
     * Set the desired velocity (in RPM) and switch to the given slot.
     *
     * @param velocity       the desired motor velocity
     * @param closedLoopSlot the slot to set the setpoint
     */
    public void setPidVelocity(double velocity, ClosedLoopSlot closedLoopSlot) {
        SparkBase.ControlType type = pidConfigs.get(closedLoopSlot).getUsesMaxMotion() ? SparkBase.ControlType.kMAXMotionVelocityControl : SparkBase.ControlType.kVelocity;
        pidController.setSetpoint(velocity, type, closedLoopSlot);
        this.setPoint = velocity;
    }

    public double getPidSetPoint() {
        return setPoint;
    }

    public void setPid(double pidP, double pidI, double pidD) {
        setPid(pidP, pidI, pidD, ClosedLoopSlot.kSlot0);
    }

    public void setPid(double pidP, double pidI, double pidD, ClosedLoopSlot closedLoopSlot) {
        SparkMaxConfig config = new SparkMaxConfig();
        config.closedLoop.pid(pidP, pidI, pidD, closedLoopSlot);
        neoMotor.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
    }

    public void setPid(double pidP, double pidI, double pidD, double iZone, double pidFF) {
        setPid(pidP, pidI, pidD, iZone, pidFF, ClosedLoopSlot.kSlot0);
    }

    public void setPid(double pidP, double pidI, double pidD, double iZone, double pidFF, ClosedLoopSlot closedLoopSlot) {
        // TODO: Maybe use apply on the existing config to just get the new values
        SparkMaxConfig config = new SparkMaxConfig();
        config.closedLoop.pid(pidP, pidI, pidD, closedLoopSlot)
                .iZone(iZone, closedLoopSlot)
                .feedForward.kV(pidFF, closedLoopSlot);
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
