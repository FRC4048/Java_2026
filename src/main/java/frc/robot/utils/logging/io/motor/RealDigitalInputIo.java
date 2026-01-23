package frc.robot.utils.logging.io.motor;

import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.utils.logging.input.DigitalInputLoggableInputs;
import frc.robot.utils.logging.io.BaseIoImpl;

public class RealDigitalInputIo extends BaseIoImpl<DigitalInputLoggableInputs> implements DigitalInputIo {

    protected final DigitalInput input;
    private boolean pressed;

    public RealDigitalInputIo(String name, DigitalInput input, DigitalInputLoggableInputs inputs) {
        super(name, inputs);
        this.input = input;
    }

    @Override
    public boolean isPressed() {
        return pressed;
    }

    @Override
    protected void updateInputs(DigitalInputLoggableInputs inputs) {
        pressed = input.get();
    }
}
