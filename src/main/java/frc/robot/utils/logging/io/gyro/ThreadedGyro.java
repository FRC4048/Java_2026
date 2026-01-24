package frc.robot.utils.logging.io.gyro;

import com.studica.frc.AHRS;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.utils.logging.input.GyroValues;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class ThreadedGyro {
    private static final long GYRO_THREAD_RATE_MS = 20;
    private final AHRS gyro;
    private AtomicReference<GyroValues> gyroValues;
    private final AtomicBoolean shouldReset = new AtomicBoolean(false);
    private final AtomicBoolean shouldOffset = new AtomicBoolean(false);
    private final AtomicLong gyroOffset = new AtomicLong();
    private final ScheduledExecutorService executor;

    public ThreadedGyro(AHRS gyro) {
        this.gyro = gyro;
        this.gyroValues = new AtomicReference<>();
        this.executor = Executors.newScheduledThreadPool(1);
    }

    public void start() {
        updateGyro();
        executor.scheduleAtFixedRate(
                () -> {
                    if (shouldReset.get()) {
                        gyro.reset();
                        shouldReset.set(false);
                    }
                    if (shouldOffset.get()) {
                        gyro.setAngleAdjustment(Double.longBitsToDouble(gyroOffset.get()));
                        shouldOffset.set(false);
                    }
                    updateGyro();
                },
                0,
                GYRO_THREAD_RATE_MS,
                TimeUnit.MILLISECONDS);
    }

    public void stop() {
        executor.shutdownNow();
    }

    public boolean stopAndWait(long maxTime, TimeUnit timeUnit) {
        executor.shutdownNow();
        try {
            return executor.awaitTermination(maxTime, timeUnit);
        } catch (InterruptedException e) {
            DriverStation.reportError(
                    "ThreadedGyro thread termination was interrupted: " + e.getMessage(), true);
            return false;
        }
    }

    private void updateGyro() {
        GyroValues newValues = new GyroValues();
        newValues.setAnglesInDeg((((gyro.getAngle()) % 360) * -1));

        newValues.setWorldLinearAccelX(gyro.getWorldLinearAccelX());
        newValues.setWorldLinearAccelY(gyro.getWorldLinearAccelY());
        newValues.setWorldLinearAccelZ(gyro.getWorldLinearAccelZ());

        newValues.setVelocityX(gyro.getVelocityX());
        newValues.setVelocityY(gyro.getVelocityY());
        newValues.setVelocityZ(gyro.getVelocityZ());

        newValues.setDisplacementX(gyro.getDisplacementX());
        newValues.setDisplacementY(gyro.getDisplacementY());
        newValues.setDisplacementZ(gyro.getDisplacementZ());

        newValues.setRawGyroX(gyro.getRawGyroX());
        newValues.setRawGyroY(gyro.getRawGyroY());
        newValues.setRawGyroZ(gyro.getRawGyroZ());

        newValues.setRawAccelX(gyro.getRawAccelX());
        newValues.setRawAccelY(gyro.getRawAccelY());
        newValues.setRawAccelZ(gyro.getRawAccelZ());

        newValues.setRawMagX(gyro.getRawMagX());
        newValues.setRawMagY(gyro.getRawMagY());
        newValues.setRawMagZ(gyro.getRawMagZ());

        newValues.setYaw(gyro.getYaw());
        newValues.setPitch(gyro.getPitch());
        newValues.setRoll(gyro.getRoll());

        newValues.setFusedHeading(gyro.getFusedHeading());
        newValues.setCompassHeading(gyro.getCompassHeading());

        newValues.setRobotCentricVelocityX(gyro.getRobotCentricVelocityX());
        newValues.setRobotCentricVelocityY(gyro.getRobotCentricVelocityY());
        newValues.setRobotCentricVelocityZ(gyro.getRobotCentricVelocityZ());

        gyroValues.set(newValues);
    }

    public GyroValues getGyroValues() {
        return gyroValues.get();
    }

    public void resetGyro() {
        shouldReset.set(true);
    }

    public void setAngleAdjustment(double degrees) {
        gyroOffset.set(Double.doubleToLongBits(degrees));
        shouldOffset.set(true);
    }

    public double getAngleOffset() {
        return Double.longBitsToDouble(gyroOffset.get());
    }
}
