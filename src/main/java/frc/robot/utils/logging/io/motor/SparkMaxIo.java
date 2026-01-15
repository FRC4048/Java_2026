package frc.robot.utils.logging.io.motor;

import frc.robot.utils.logging.io.BaseIo;

/**
 * An interface for SparkMax IO controller.
 */
public interface SparkMaxIo extends BaseIo {
    void set(double speed);

    void setVoltage(double voltage);

    void stopMotor();

    boolean isFwdSwitchPressed();
    boolean isRevSwitchPressed();
}
