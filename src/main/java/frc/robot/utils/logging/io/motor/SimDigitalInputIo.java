package frc.robot.utils.logging.io.motor;

import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.constants.Constants;
import frc.robot.utils.logging.input.DigitalInputLoggableInputs;

public class SimDigitalInputIo extends RealDigitalInputIo {

    public SimDigitalInputIo(String name, DigitalInput input, DigitalInputLoggableInputs inputs) {
        super(name, input, inputs);
    }

    @Override
    protected void updateInputs(DigitalInputLoggableInputs inputs) {
        super.updateInputs(inputs);

        if (Constants.currentMode == Constants.Mode.SIM) {
            // Simulation code go here
        }
    }
}
