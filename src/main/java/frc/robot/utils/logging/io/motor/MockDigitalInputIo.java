package frc.robot.utils.logging.io.motor;

import frc.robot.utils.logging.input.MotorLoggableInputs;
import frc.robot.utils.logging.io.BaseIoImpl;

/**
 * Mock implementation (noop) for a DigitalInput IO.
 */
public class MockDigitalInputIo extends BaseIoImpl<MotorLoggableInputs> implements DigitalInputIo {

    private boolean pressed;

    public MockDigitalInputIo(String name, MotorLoggableInputs inputs) {
        super(name, inputs);
    }

    @Override
    public boolean isPressed() {
        return pressed;
    }

    @Override
    protected void updateInputs(MotorLoggableInputs inputs) {
        // No hardware - value can be set via tests if needed
    }

    // Optional helper for tests
    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }
}

