package frc.robot.utils.logging.io.motor;

import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.Constants;
import frc.robot.utils.logging.input.MotorLoggableInputs;

public class SimDigitalInputIo extends RealDigitalInputIo {

    public SimDigitalInputIo(String name, DigitalInput input, MotorLoggableInputs inputs) {
        super(name, input, inputs);
    }

    @Override
    protected void updateInputs(MotorLoggableInputs inputs) {
        super.updateInputs(inputs);

        if (Constants.currentMode == Constants.Mode.SIM) {
            // Simulation hooks go here if needed
        }
    }
}
