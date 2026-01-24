package frc.robot.utils.logging.io.gyro;

import frc.robot.utils.logging.input.GyroInputs;
import frc.robot.utils.logging.input.GyroValues;
import frc.robot.utils.logging.io.BaseIoImpl;

public class MockGyroIo extends BaseIoImpl<GyroInputs> implements GyroIo {
    public MockGyroIo() {
        super(LOGGING_NAME, new GyroInputs());
    }

    @Override
    public void setAngleOffset(double offset) {
    }

    @Override
    public void resetGyro() {
    }

    @Override
    public GyroValues getGyroValues() {
        return getInputs().getGyroValues();
    }

    @Override
    protected void updateInputs(GyroInputs inputs) {
    }
}
