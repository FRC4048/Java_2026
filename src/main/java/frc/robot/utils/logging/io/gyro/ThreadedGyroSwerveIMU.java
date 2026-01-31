package frc.robot.utils.logging.io.gyro;

import com.studica.frc.AHRS;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.units.measure.MutAngularVelocity;
import frc.robot.utils.logging.input.GyroValues;
import swervelib.imu.SwerveIMU;

import java.util.Optional;

import static edu.wpi.first.units.Units.DegreesPerSecond;

/**
 * An implementation of the {@link SwerveIMU} that uses a {@link ThreadedGyro} as its underlying.
 * In order to support a background threaded implementation of Gyro updates, this
 * class uses the ThreadedGyro to offload the Gyro access, and to read the latest
 * values from memory.
 * It is modeled after the {@link swervelib.imu.NavXSwerve} implementation.
 */
public class ThreadedGyroSwerveIMU extends SwerveIMU {
    /**
     * Mutable {@link MutAngularVelocity} for readings.
     */
    private final MutAngularVelocity yawVel = new MutAngularVelocity(0, 0, DegreesPerSecond);

    private ThreadedGyro threadedGyro;

    /**
     * Offset for the NavX.
     */
    private Rotation3d offset = new Rotation3d();

    /**
     * Inversion state of the {@link AHRS}.
     */
    private boolean inverted = false;

    public ThreadedGyroSwerveIMU(ThreadedGyro threadedGyro) {
        this.threadedGyro = threadedGyro;
    }

    @Override
    public void close() {
        threadedGyro.stop();
    }

    /**
     * Reset offset to current gyro reading. Does not call NavX({@link AHRS#reset()}) because it has been reported to be
     * too slow.
     */
    @Override
    public void factoryDefault() {
        // gyro.reset(); // Reported to be slow
        offset = threadedGyro.getGyroValues().getRotation3d();
    }

    @Override
    public void clearStickyFaults() {
    }

    @Override
    public void setOffset(Rotation3d offset) {
        this.offset = offset;
    }

    /**
     * Set the gyro to invert its default direction
     *
     * @param invertIMU invert gyro direction
     */
    @Override
    public void setInverted(boolean invertIMU) {
        inverted = invertIMU;
//    setOffset(getRawRotation3d());
    }

    @Override
    public Rotation3d getRawRotation3d() {
        return inverted ? negate(threadedGyro.getGyroValues().getRotation3d()) : threadedGyro.getGyroValues().getRotation3d();
    }

    /**
     * Fetch the {@link Rotation3d} from the IMU. Robot relative.
     *
     * @return {@link Rotation3d} from the IMU.
     */
    @Override
    public Rotation3d getRotation3d() {
        return getRawRotation3d().rotateBy(offset.unaryMinus());
    }

    /**
     * Fetch the acceleration [x, y, z] from the IMU in meters per second squared. If acceleration isn't supported returns
     * empty.
     *
     * @return {@link Translation3d} of the acceleration as an {@link Optional}.
     */
    @Override
    public Optional<Translation3d> getAccel()
    {
        GyroValues gyroValues = threadedGyro.getGyroValues();
        return Optional.of(
                new Translation3d(
                        gyroValues.getWorldLinearAccelX(),
                        gyroValues.getWorldLinearAccelY(),
                        gyroValues.getWorldLinearAccelZ())
                        .times(9.81));
    }

    @Override
    public MutAngularVelocity getYawAngularVelocity() {
        return yawVel.mut_setMagnitude(threadedGyro.getGyroValues().getRate());
    }

    @Override
    public Object getIMU() {
        return threadedGyro.getNavxInstance();
    }

    private Rotation3d negate(Rotation3d rot) {
        return new Rotation3d(-rot.getX(), -rot.getY(), -rot.getZ());
    }
}
