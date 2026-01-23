package frc.robot.utils.logging.io.motor;

import frc.robot.utils.logging.input.DigitalInputLoggableInputs;
import frc.robot.utils.logging.io.BaseIoImpl;

public class MockDigitalInputIo extends BaseIoImpl<DigitalInputLoggableInputs> implements DigitalInputIo {

    private boolean pressed;

    public MockDigitalInputIo(String name, DigitalInputLoggableInputs inputs) {
        super(name, inputs);
    }

    @Override
    public boolean isPressed() {
        return pressed;
    }

    @Override
    protected void updateInputs(DigitalInputLoggableInputs inputs) {
        // No hardware so not used
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }
}

