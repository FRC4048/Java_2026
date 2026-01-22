package frc.robot.utils.logging.io.motor;

import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.utils.logging.input.MotorLoggableInputs;
import frc.robot.utils.logging.io.BaseIoImpl;

public class RealDigitalInputIo extends BaseIoImpl<MotorLoggableInputs> implements DigitalInputIo {

    protected final DigitalInput input;
    private boolean pressed;

    public RealDigitalInputIo(String name, DigitalInput input, MotorLoggableInputs inputs) {
        super(name, inputs);
        this.input = input;
    }

    @Override
    public boolean isPressed() {
        return pressed;
    }

    @Override
    protected void updateInputs(MotorLoggableInputs inputs) {
        pressed = input.get();
    }
}
