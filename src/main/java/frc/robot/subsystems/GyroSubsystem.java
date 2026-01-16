package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.logging.io.gyro.GyroIo;
import frc.robot.utils.logging.io.gyro.MockGyroIo;
import frc.robot.utils.logging.io.gyro.RealGyroIo;
import frc.robot.utils.simulation.RobotVisualizer;

public class GyroSubsystem extends SubsystemBase {
    private final GyroIo io;

    public GyroSubsystem(GyroIo io) {
        this.io = io;
    }

    public void setAngleOffset(double offset) {
        io.setAngleOffset(offset);
    }

    public void resetGyro() {
        io.resetGyro();
    }

    @Override
    public void periodic() {
        io.periodic();
    }

    public static GyroIo createMockIo() {
        return new MockGyroIo();
    }

    public static GyroIo createRealIo() {
        RealGyroIo realGyroIo = new RealGyroIo();
        realGyroIo.start();
        return realGyroIo;
    }

    public static GyroIo createSimIo(RobotVisualizer visualizer) {
        // For now, we don't actually simulate the gyro
        return new MockGyroIo();
    }
}
