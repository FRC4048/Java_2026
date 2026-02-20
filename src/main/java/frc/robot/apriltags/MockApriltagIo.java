package frc.robot.apriltags;

import frc.robot.utils.logging.io.BaseIoImpl;

public class MockApriltagIo extends BaseIoImpl<ApriltagInputs> implements ApriltagIO {
    public MockApriltagIo(String name, ApriltagInputs inputs) {
        super(name, inputs);
    }
    @Override
    public void updateInputs(ApriltagInputs inputs) {}
    @Override
    public void addReading(ApriltagReading reading) {}
}