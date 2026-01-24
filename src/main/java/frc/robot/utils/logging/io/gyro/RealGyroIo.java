package frc.robot.utils.logging.io.gyro;

import com.studica.frc.AHRS;
import frc.robot.utils.logging.input.GyroInputs;
import frc.robot.utils.logging.input.GyroValues;
import frc.robot.utils.logging.io.BaseIoImpl;

public class RealGyroIo extends BaseIoImpl<GyroInputs> implements GyroIo {
    private ThreadedGyro threadedGyro;

    public RealGyroIo() {
        super(LOGGING_NAME, new GyroInputs());
        threadedGyro = new ThreadedGyro(new AHRS(AHRS.NavXComType.kMXP_SPI));
    }

    public void start() {
        threadedGyro.start();
    }

    public void stop() {
        threadedGyro.stop();
    }

    @Override
    public void setAngleOffset(double offset) {
        threadedGyro.setAngleAdjustment(offset);
    }

    @Override
    public void resetGyro() {
        threadedGyro.resetGyro();
    }

    @Override
    public GyroValues getGyroValues() {
        return getInputs().getGyroValues();
    }

    @Override
    protected void updateInputs(GyroInputs inputs) {
        inputs.fromHardware(threadedGyro.getGyroValues());
    }
}
