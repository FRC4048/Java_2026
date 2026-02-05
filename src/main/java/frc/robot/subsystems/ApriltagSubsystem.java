package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.apriltags.ApriltagIO;
import frc.robot.apriltags.ApriltagInputs;
import frc.robot.apriltags.MockApriltagIo;
import frc.robot.apriltags.TCPApriltagIo;
import frc.robot.utils.logging.io.BaseIoImpl;

public class ApriltagSubsystem extends SubsystemBase {

    public static final String LOGGING_NAME = "ApriltagSubsystem";
    private final ApriltagIO io;

    public ApriltagSubsystem(ApriltagIO io) {
        this.io = io;
    }

    public static ApriltagIO createRealIo() {
        return new TCPApriltagIo(LOGGING_NAME, new ApriltagInputs());
    }

    public static ApriltagIO createMockIo() {
        return new MockApriltagIo(LOGGING_NAME, new ApriltagInputs());
    }

    @Override
    public void periodic() {
        io.periodic();
    }
}
