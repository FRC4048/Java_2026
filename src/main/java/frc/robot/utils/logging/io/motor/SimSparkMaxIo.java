package frc.robot.utils.logging.io.motor;

import com.revrobotics.spark.SparkMax;
import frc.robot.Constants;
import frc.robot.utils.logging.input.MotorLoggableInputs;
import frc.robot.utils.simulation.Simulator;
import org.littletonrobotics.junction.mechanism.LoggedMechanismLigament2d;

/**
 * IO Implementation for a simulated SparkMax IO controller.
 */
public class SimSparkMaxIo extends RealSparkMaxIo {
    private final Simulator motorSimulator;

    public SimSparkMaxIo(String name, SparkMax motor, MotorLoggableInputs inputs,
                         Simulator simulator) {
        super(name, motor, inputs);
        this.motorSimulator = simulator;
    }

    @Override
    public void updateInputs(MotorLoggableInputs inputs) {
        super.updateInputs(inputs);
        if (Constants.currentMode == Constants.Mode.SIM) {
            motorSimulator.stepSimulation();
        }
    }
}
