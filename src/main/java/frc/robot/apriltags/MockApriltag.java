package frc.robot.apriltags;

import frc.robot.utils.logging.input.MotorLoggableInputs;
import frc.robot.utils.logging.io.BaseIoImpl;

public class MockApriltag extends BaseIOImpl<ApriltagInputs> implements ApriltagIO {
  public MockApriltag(String name, ApriltagInputs inputs) {
          super(name, inputs);
      }
  @Override
  protected void updateInputs(ApriltagInputs inputs) {}
}