package frc.robot.utils.logging.io.motor;

import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.utils.logging.input.DigitalInputLoggableInputs;
import frc.robot.utils.logging.io.BaseIoImpl;

public class RealDigitalInputIo extends BaseIoImpl<DigitalInputLoggableInputs> implements DigitalInputIo {

    protected final DigitalInput digitalInput;

    public RealDigitalInputIo(String name, DigitalInput digitalInput, DigitalInputLoggableInputs inputs) {
        super(name, inputs);
        this.digitalInput = digitalInput;
    }

    @Override
    protected void updateInputs(DigitalInputLoggableInputs inputs) {
        inputs.fromHardware(digitalInput);
    }

    @Override
    public boolean isPressed() {
        return getInputs().isPressed();
    }
}

