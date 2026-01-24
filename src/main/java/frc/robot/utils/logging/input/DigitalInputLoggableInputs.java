package frc.robot.utils.logging.input;

import edu.wpi.first.wpilibj.DigitalInput;
import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

import java.util.EnumSet;
import java.util.Objects;

public class DigitalInputLoggableInputs implements LoggableInputs {

    private static final String PRESSED_KEY = "pressed";

    private boolean pressed = false;

    @Override
    public void toLog(LogTable table) {
        table.put(PRESSED_KEY, pressed);
    }

    @Override
    public void fromLog(LogTable table) {
        pressed = table.get(PRESSED_KEY, pressed);
    }
    

    public void fromHardware(DigitalInput input) {
        pressed = input.get();
    }

    public boolean isPressed() {
        return pressed;
    }
}

