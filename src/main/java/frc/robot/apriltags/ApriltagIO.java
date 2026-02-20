package frc.robot.apriltags;

import frc.robot.utils.logging.io.BaseIo;

public interface ApriltagIO extends BaseIo {
    // This is used to inject april tag readings manually and will pretty much only be used for simulation.
    void addReading(ApriltagReading reading);
    ApriltagInputs getInputs();
}