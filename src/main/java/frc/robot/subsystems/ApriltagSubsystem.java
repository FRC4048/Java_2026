package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.apriltags.*;
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

    public static ApriltagIO createSimIo() {
        return new SimApriltagIO(LOGGING_NAME, new ApriltagInputs(), new SimTCPServer(0)); // port doesnt matter at all
    }
    // This is used to inject april tag readings manually and will pretty much only be used for simulation.
    public void addSimReading(ApriltagReading reading) {
        io.addReading(reading);
    }
    @Override
    public void periodic() {
        io.periodic();
    }
}
