package frc.robot.utils.logging.io.gyro;


import frc.robot.utils.logging.input.GyroValues;
import frc.robot.utils.logging.io.BaseIo;

public interface GyroIo extends BaseIo {
    String LOGGING_NAME = "Gyro";

    void setAngleOffset(double offset);

    void resetGyro();

    GyroValues getGyroValues();
}
