package frc.robot.utils.logging.io.pidmotor;

import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import frc.robot.utils.logging.io.motor.SparkMaxIo;

/**
 * An interface for SparkMax IO controller with PID capabilities.
 * This interface extends the {@link SparkMaxIo} with PID configuration and information.
 */
public interface SparkMaxPidMotorIo extends SparkMaxIo {
    /**
     * Set all PID control parameters
     * @param params the PID parametrs to set
     */
    void configurePID(SparkMaxPidConfig params);

    /**
     * Set PID position.
     * Set the position (in rotations) for the PidMotor to get to.
     *
     * @param position the position (in rotations) to drive to
     */
    void setPidPosition(double position);

    /**
     * Set PID velocity.
     * Set the velocity (in RPM) for the PidMotor to get to.
     *
     * @param velocity the position (in rotations) to drive to
     */
    void setPidVelocity(double velocity);

    /**
     * Reset the relative encoder position.
     *
     * @param positionRotations the position (in rotations) to set the encoder to
     */
    void resetEncoderPosition(double positionRotations);

    /**
     * Set new PID values for the PidMotor.
     * This will replace the existing PID values with the nerw ones
     * @param pidP P value to set
     * @param pidI I valkue to set
     * @param pidD D value to set
     */
    void setPid(double pidP, double pidI, double pidD);

    /**
     * Set new PID values for the PidMotor.
     * This will replace the existing PID values with the nerw ones
     * @param pidP P value to set
     * @param pidI I valkue to set
     * @param pidD D value to set
     * @param iZone the iZone value to set
     * @param pidFF the FF value to set
     */
    void setPid(double pidP, double pidI, double pidD, double iZone, double pidFF);

}
