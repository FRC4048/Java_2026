package frc.robot.utils.logging.io.pidmotor;

import frc.robot.utils.logging.input.MotorLoggableInputs;
import frc.robot.utils.logging.io.motor.RealSparkMaxIo;

/**
 * IO implementation for a real SparkMaxPidMotor.
 * Note that this used the {@link MotorLoggableInputs} therefore will not log specific PID controller values.
 */
public class RealSparkMaxPidMotorIo extends RealSparkMaxIo implements SparkMaxPidMotorIo {
    private final SparkMaxPidMotor pidMotor;

    public RealSparkMaxPidMotorIo(String name, SparkMaxPidMotor pidMotor, MotorLoggableInputs inputs) {
        super(name, pidMotor.getNeoMotor(), inputs);
        this.pidMotor = pidMotor;
    }

    @Override
    public void configurePID(SparkMaxPidConfig params) {
        pidMotor.configurePID(params);
    }

    @Override
    public void setPidPosition(double position) {
        pidMotor.setPidPosition(position);
    }

    @Override
    public void setPidVelocity(double velocity) {
        pidMotor.setPidVelocity(velocity);
    }

    @Override
    public void setPid(double pidP, double pidI, double pidD) {
        pidMotor.setPid(pidP, pidI, pidD);
    }

    @Override
    public void setPid(double pidP, double pidI, double pidD, double iZone, double pidFF) {
        pidMotor.setPid(pidP, pidI, pidD, iZone, pidFF);
    }

    @Override
    public void resetEncoderPosition(double positionRotations) {
        pidMotor.getEncoder().setPosition(positionRotations);
    }
}
