package frc.robot.utils.logging.input;

import com.revrobotics.spark.SparkMax;

import frc.robot.utils.motor.NeoPidConfig;
import frc.robot.utils.motor.NeoPidMotor;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

import java.util.EnumSet;
import java.util.Objects;

/**
 * A {@link org.littletonrobotics.junction.inputs.LoggableInputs} subclass that can handle motor state.
 * This implementation can hold state for a generic motor controller and be updated from various types
 * of "real" motor controllers.
 */
public class PidMotorInputs implements LoggableInputs {
    public enum PIDMotorInputTypes {
        ENCODER_POSITION("encoderPosition"),
        ENCODER_VELOCITY("encoderVelocity"),
        MOTOR_CURRENT("motorCurrent"),
        MOTOR_TEMPERATURE("motorTemperature"),
        APPLIED_OUTPUT("appliedOutput"),
        FWD_LIMIT_SWITCH("fwdLimit"),
        REV_LIMIT_SWITCH("revLimit"),
        PID_SETPOINT("pidSetpoint");

        private final String logKey;

        PIDMotorInputTypes(String logKey) {
            this.logKey = logKey;
        }

        public String getLogKey() {
            return logKey;
        }
    }

    // Set of booleans for values that we care about
    private final EnumSet<PIDMotorInputTypes> loggedInputFilter;
    private double encoderPosition = 0.0;
    private double encoderVelocity = 0.0;
    private double motorCurrent = 0.0;
    private double motorTemperature = 0.0;
    private double appliedOutput = 0.0;
    private boolean fwdLimit = false;
    private boolean revLimit = false;
    private double pidSetpoint = 0;

    public PidMotorInputs(EnumSet<PIDMotorInputTypes> loggedInputFilter) {
        this.loggedInputFilter = Objects.requireNonNull(loggedInputFilter);
    }

    public static PidMotorInputs allMetrics() {
        return new PidMotorInputs(EnumSet.allOf(PIDMotorInputTypes.class));
    }

    @Override
    public void toLog(LogTable table) {
        if (loggedInputFilter.contains(PIDMotorInputTypes.ENCODER_POSITION)) {
            table.put(PIDMotorInputTypes.ENCODER_POSITION.getLogKey(), encoderPosition);
        }
        if (loggedInputFilter.contains(PIDMotorInputTypes.ENCODER_VELOCITY)) {
            table.put(PIDMotorInputTypes.ENCODER_VELOCITY.getLogKey(), encoderVelocity);
        }
        if (loggedInputFilter.contains(PIDMotorInputTypes.MOTOR_CURRENT)) {
            table.put(PIDMotorInputTypes.MOTOR_CURRENT.getLogKey(), motorCurrent);
        }
        if (loggedInputFilter.contains(PIDMotorInputTypes.MOTOR_TEMPERATURE)) {
            table.put(PIDMotorInputTypes.MOTOR_TEMPERATURE.getLogKey(), motorTemperature);
        }
        if (loggedInputFilter.contains(PIDMotorInputTypes.APPLIED_OUTPUT)) {
            table.put(PIDMotorInputTypes.APPLIED_OUTPUT.getLogKey(), appliedOutput);
        }
        if (loggedInputFilter.contains(PIDMotorInputTypes.FWD_LIMIT_SWITCH)) {
            table.put(PIDMotorInputTypes.FWD_LIMIT_SWITCH.getLogKey(), fwdLimit);
        }
        if (loggedInputFilter.contains(PIDMotorInputTypes.REV_LIMIT_SWITCH)) {
            table.put(PIDMotorInputTypes.REV_LIMIT_SWITCH.getLogKey(), revLimit);
        }
        if (loggedInputFilter.contains(PIDMotorInputTypes.PID_SETPOINT)) {
          table.put(PIDMotorInputTypes.PID_SETPOINT.getLogKey(), pidSetpoint);
        }
    }

    @Override
    public void fromLog(LogTable table) {
        if (loggedInputFilter.contains(PIDMotorInputTypes.ENCODER_POSITION)) {
            encoderPosition = table.get(PIDMotorInputTypes.ENCODER_POSITION.getLogKey(), encoderPosition);
        }
        if (loggedInputFilter.contains(PIDMotorInputTypes.ENCODER_VELOCITY)) {
            encoderVelocity = table.get(PIDMotorInputTypes.ENCODER_VELOCITY.getLogKey(), encoderVelocity);
        }
        if (loggedInputFilter.contains(PIDMotorInputTypes.MOTOR_CURRENT)) {
            motorCurrent = table.get(PIDMotorInputTypes.MOTOR_CURRENT.getLogKey(), motorCurrent);
        }
        if (loggedInputFilter.contains(PIDMotorInputTypes.MOTOR_TEMPERATURE)) {
            motorTemperature = table.get(PIDMotorInputTypes.MOTOR_TEMPERATURE.getLogKey(), motorTemperature);
        }
        if (loggedInputFilter.contains(PIDMotorInputTypes.APPLIED_OUTPUT)) {
            appliedOutput = table.get(PIDMotorInputTypes.APPLIED_OUTPUT.getLogKey(), appliedOutput);
        }
        if (loggedInputFilter.contains(PIDMotorInputTypes.FWD_LIMIT_SWITCH)) {
            fwdLimit = table.get(PIDMotorInputTypes.FWD_LIMIT_SWITCH.getLogKey(), fwdLimit);
        }
        if (loggedInputFilter.contains(PIDMotorInputTypes.REV_LIMIT_SWITCH)) {
            revLimit = table.get(PIDMotorInputTypes.REV_LIMIT_SWITCH.getLogKey(), revLimit);
        }
        if (loggedInputFilter.contains(PIDMotorInputTypes.PID_SETPOINT)) {
          pidSetpoint = table.get(PIDMotorInputTypes.PID_SETPOINT.getLogKey(), pidSetpoint);
        }
    }

    public void fromHardware(NeoPidMotor pidMotor) {
      SparkMax sparkMax = pidMotor.getNeoMotor();
        if (loggedInputFilter.contains(PIDMotorInputTypes.ENCODER_POSITION)) {
            encoderPosition = sparkMax.getEncoder().getPosition();
        }
        if (loggedInputFilter.contains(PIDMotorInputTypes.ENCODER_VELOCITY)) {
            encoderVelocity = sparkMax.getEncoder().getVelocity();
        }
        if (loggedInputFilter.contains(PIDMotorInputTypes.MOTOR_CURRENT)) {
            motorCurrent = sparkMax.getOutputCurrent();
        }
        if (loggedInputFilter.contains(PIDMotorInputTypes.MOTOR_TEMPERATURE)) {
            motorTemperature = sparkMax.getMotorTemperature();
        }
        if (loggedInputFilter.contains(PIDMotorInputTypes.APPLIED_OUTPUT)) {
            appliedOutput = sparkMax.getAppliedOutput();
        }
        if (loggedInputFilter.contains(PIDMotorInputTypes.FWD_LIMIT_SWITCH)) {
            fwdLimit = sparkMax.getForwardLimitSwitch().isPressed();
        }
        if (loggedInputFilter.contains(PIDMotorInputTypes.REV_LIMIT_SWITCH)) {
            revLimit = sparkMax.getReverseLimitSwitch().isPressed();
        }
        if (loggedInputFilter.contains(PIDMotorInputTypes.PID_SETPOINT)) {
          pidMotor.getPidPosition();
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
    public Double getPidPosition() {
      return pidSetpoint;
    }
}
