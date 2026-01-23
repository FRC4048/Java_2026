package frc.robot.utils.logging.input;

import edu.wpi.first.wpilibj.DigitalInput;
import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

import java.util.EnumSet;
import java.util.Objects;

public class DigitalInputLoggableInputs implements LoggableInputs {

    public enum DigitalInputTypes {
        PRESSED("pressed");

        private final String logKey;

        DigitalInputTypes(String logKey) {
            this.logKey = logKey;
        }

        public String getLogKey() {
            return logKey;
        }
    }

    private final EnumSet<DigitalInputTypes> loggedInputFilter;
    private boolean pressed = false;

    public DigitalInputLoggableInputs(EnumSet<DigitalInputTypes> loggedInputFilter) {
        this.loggedInputFilter = Objects.requireNonNull(loggedInputFilter);
    }

    public static DigitalInputLoggableInputs allMetrics() {
        return new DigitalInputLoggableInputs(EnumSet.allOf(DigitalInputTypes.class));
    }

    @Override
    public void toLog(LogTable table) {
        if (loggedInputFilter.contains(DigitalInputTypes.PRESSED)) {
            table.put(DigitalInputTypes.PRESSED.getLogKey(), pressed);
        }
    }

    @Override
    public void fromLog(LogTable table) {
        if (loggedInputFilter.contains(DigitalInputTypes.PRESSED)) {
            pressed = table.get(
                    DigitalInputTypes.PRESSED.getLogKey(),
                    pressed
            );
        }
    }

    public void fromHardware(DigitalInput input) {
        if (loggedInputFilter.contains(DigitalInputTypes.PRESSED)) {
            pressed = input.get();
        }
    }

    public boolean isPressed() {
        return pressed;
    }
}

