package frc.robot.utils.logging.input;

import com.revrobotics.spark.SparkMax;
import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

import java.util.EnumSet;
import java.util.Objects;

/**
 * A {@link org.littletonrobotics.junction.inputs.LoggableInputs} subclass that can handle motor state.
 * This implementation can hold state for a generic motor controller and be updated from various types
 * of "real" motor controllers.
 */
public class MotorLoggableInputs implements LoggableInputs {
    public enum MotorInputTypes {
        ENCODER_POSITION("encoderPosition"),
        ENCODER_VELOCITY("encoderVelocity"),
        MOTOR_CURRENT("motorCurrent"),
        MOTOR_TEMPERATURE("motorTemperature"),
        APPLIED_OUTPUT("appliedOutput"),
        FWD_LIMIT_SWITCH("fwdLimit"),
        REV_LIMIT_SWITCH("revLimit");

        private final String logKey;

        MotorInputTypes(String logKey) {
            this.logKey = logKey;
        }

        public String getLogKey() {
            return logKey;
        }
    }

    // Set of booleans for values that we care about
    private final EnumSet<MotorInputTypes> loggedInputFilter;
    private double encoderPosition = 0.0;
    private double encoderVelocity = 0.0;
    private double motorCurrent = 0.0;
    private double motorTemperature = 0.0;
    private double appliedOutput = 0.0;
    private boolean fwdLimit = false;
    private boolean revLimit = false;

    public MotorLoggableInputs(EnumSet<MotorInputTypes> loggedInputFilter) {
        this.loggedInputFilter = Objects.requireNonNull(loggedInputFilter);
    }

    public static MotorLoggableInputs allMetrics() {
        return new MotorLoggableInputs(EnumSet.allOf(MotorInputTypes.class));
    }

    @Override
    public void toLog(LogTable table) {
        if (loggedInputFilter.contains(MotorInputTypes.ENCODER_POSITION)) {
            table.put(MotorInputTypes.ENCODER_POSITION.getLogKey(), encoderPosition);
        }
        if (loggedInputFilter.contains(MotorInputTypes.ENCODER_VELOCITY)) {
            table.put(MotorInputTypes.ENCODER_VELOCITY.getLogKey(), encoderVelocity);
        }
        if (loggedInputFilter.contains(MotorInputTypes.MOTOR_CURRENT)) {
            table.put(MotorInputTypes.MOTOR_CURRENT.getLogKey(), motorCurrent);
        }
        if (loggedInputFilter.contains(MotorInputTypes.MOTOR_TEMPERATURE)) {
            table.put(MotorInputTypes.MOTOR_TEMPERATURE.getLogKey(), motorTemperature);
        }
        if (loggedInputFilter.contains(MotorInputTypes.APPLIED_OUTPUT)) {
            table.put(MotorInputTypes.APPLIED_OUTPUT.getLogKey(), appliedOutput);
        }
        if (loggedInputFilter.contains(MotorInputTypes.FWD_LIMIT_SWITCH)) {
            table.put(MotorInputTypes.FWD_LIMIT_SWITCH.getLogKey(), fwdLimit);
        }
        if (loggedInputFilter.contains(MotorInputTypes.REV_LIMIT_SWITCH)) {
            table.put(MotorInputTypes.REV_LIMIT_SWITCH.getLogKey(), revLimit);
        }
    }

    @Override
    public void fromLog(LogTable table) {
        if (loggedInputFilter.contains(MotorInputTypes.ENCODER_POSITION)) {
            encoderPosition = table.get(MotorInputTypes.ENCODER_POSITION.getLogKey(), encoderPosition);
        }
        if (loggedInputFilter.contains(MotorInputTypes.ENCODER_VELOCITY)) {
            encoderVelocity = table.get(MotorInputTypes.ENCODER_VELOCITY.getLogKey(), encoderVelocity);
        }
        if (loggedInputFilter.contains(MotorInputTypes.MOTOR_CURRENT)) {
            motorCurrent = table.get(MotorInputTypes.MOTOR_CURRENT.getLogKey(), motorCurrent);
        }
        if (loggedInputFilter.contains(MotorInputTypes.MOTOR_TEMPERATURE)) {
            motorTemperature = table.get(MotorInputTypes.MOTOR_TEMPERATURE.getLogKey(), motorTemperature);
        }
        if (loggedInputFilter.contains(MotorInputTypes.APPLIED_OUTPUT)) {
            appliedOutput = table.get(MotorInputTypes.APPLIED_OUTPUT.getLogKey(), appliedOutput);
        }
        if (loggedInputFilter.contains(MotorInputTypes.FWD_LIMIT_SWITCH)) {
            fwdLimit = table.get(MotorInputTypes.FWD_LIMIT_SWITCH.getLogKey(), fwdLimit);
        }
        if (loggedInputFilter.contains(MotorInputTypes.REV_LIMIT_SWITCH)) {
            revLimit = table.get(MotorInputTypes.REV_LIMIT_SWITCH.getLogKey(), revLimit);
        }
    }

    public void fromHardware(SparkMax sparkMax) {
        if (loggedInputFilter.contains(MotorInputTypes.ENCODER_POSITION)) {
            encoderPosition = sparkMax.getEncoder().getPosition();
        }
        if (loggedInputFilter.contains(MotorInputTypes.ENCODER_VELOCITY)) {
            encoderVelocity = sparkMax.getEncoder().getVelocity();
        }
        if (loggedInputFilter.contains(MotorInputTypes.MOTOR_CURRENT)) {
            motorCurrent = sparkMax.getOutputCurrent();
        }
        if (loggedInputFilter.contains(MotorInputTypes.MOTOR_TEMPERATURE)) {
            motorTemperature = sparkMax.getMotorTemperature();
        }
        if (loggedInputFilter.contains(MotorInputTypes.APPLIED_OUTPUT)) {
            appliedOutput = sparkMax.getAppliedOutput();
        }
        if (loggedInputFilter.contains(MotorInputTypes.FWD_LIMIT_SWITCH)) {
            fwdLimit = sparkMax.getForwardLimitSwitch().isPressed();
        }
        if (loggedInputFilter.contains(MotorInputTypes.REV_LIMIT_SWITCH)) {
            revLimit = sparkMax.getReverseLimitSwitch().isPressed();
        }
    }

    public Double getEncoderPosition() {
        return encoderPosition;
    }

    public Double getEncoderVelocity() {
        return encoderVelocity;
    }

    public Double getMotorCurrent() {
        return motorCurrent;
    }

    public Double getMotorTemperature() {
        return motorTemperature;
    }

    public Boolean getFwdLimit() {
        return fwdLimit;
    }

    public Boolean getRevLimit() {
        return revLimit;
    }

    public Double getAppliedOutput() {
        return appliedOutput;
    }
}
