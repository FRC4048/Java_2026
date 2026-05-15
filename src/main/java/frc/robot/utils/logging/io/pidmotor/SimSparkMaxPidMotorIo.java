package frc.robot.utils.logging.io.pidmotor;

import frc.robot.constants.Constants;
import frc.robot.utils.logging.input.MotorLoggableInputs;
import frc.robot.utils.simulation.ArmSimulator;
import frc.robot.utils.simulation.Simulator;

/**
 * IO implementation for simulation PID motor.
 * Similar to the {@link frc.robot.utils.logging.io.motor.SimSparkMaxIo},
 * this extends the Real implementation and adds simulation step.
 */
public class SimSparkMaxPidMotorIo extends RealSparkMaxPidMotorIo {
    private final Simulator motorSimulator;

    public SimSparkMaxPidMotorIo(String name, SparkMaxPidMotor pidMotor, MotorLoggableInputs inputs, Simulator simulator) {
        super(name, pidMotor, inputs);
        this.motorSimulator = simulator;
    }

    @Override
    public void updateInputs(MotorLoggableInputs inputs) {
        super.updateInputs(inputs);
        if (Constants.currentMode == Constants.Mode.SIM) {
            motorSimulator.stepSimulation();
        }
    }

    @Override
    public void resetEncoderPosition(double positionRotations) {
        super.resetEncoderPosition(positionRotations);
        // This is a hack put in place to support resetting of the encoder
        // The arm does not have an abs encoder to take a value from upon initialization, so it needs to be moved
        // into the end position and the encoder reset to 0. Since the hardware simulation holds onto a value
        // of its own other than the encoder itself, we need to reset both.
        // So far only arm simulators show this issue, but we may need a similar hack in the future
        // for other simulators.
        if (motorSimulator instanceof ArmSimulator) {
            ((ArmSimulator)motorSimulator).resetEncoderPosition(positionRotations);
        }
    }
}
