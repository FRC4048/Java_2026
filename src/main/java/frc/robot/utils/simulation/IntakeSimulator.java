package frc.robot.utils.simulation;

import com.revrobotics.sim.SparkMaxSim;
import com.revrobotics.sim.SparkRelativeEncoderSim;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.math.system.plant.DCMotor;
import org.littletonrobotics.junction.mechanism.LoggedMechanismLigament2d;

import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.Rotations;

/**
 * A simulator for an intake mechanism: A motor hooked up to some wheels and a limit switch. The
 * mechanism run in the "forward" direction for some rotations until it hits the
 * FORWARD_ROTATIONS_TO_SWITCH limit. At this point, the forward limit switch will trigger. (The
 * state will change from EMPTY to FULL). At this point, driving the motor backwards until the
 * BACKWARDS_ROTATIONS_TO_UNSWITCH will release the switch. Driving backwards more, until
 * BACKWARD_ROTATIONS_TO_CLEAR will also change the state to EMPTY (the starting point). To simulate
 * "shooting", one can configure the motor to NOT have a switch configured (so that when the switch
 * triggers the motor will not stop) and moving the motor forward again. Once the motor gets to
 * FORWARD_ROTATIONS_TO_CLEAR the state will change to EMPTY and the switch will also clear.
 */
public class IntakeSimulator implements Simulator {
    // number of rotations from start point to where piece hits forward switch
    public static final double FORWARD_ROTATIONS_TO_SWITCH = 20;
    // number of rotations from start point to where piece clears the mechanism off the front
    public static final double FORWARD_ROTATIONS_TO_CLEAR = 24;
    // number of rotations from start point to where piece clears the mechanism off the back
    public static final double BACKWARD_ROTATIONS_TO_CLEAR = -5;
    // number of rotations from start point to where piece clears the forward switch
    public static final double BACKWARDS_ROTATIONS_TO_UNSWITCH = 14;

    private enum Mode {
        EMPTY,
        FULL
    }

    private static final double RPM_PER_VOLT = 100;

    // Gearbox represents a gearbox (1:1 conversion rate) with 1 or motors connected
    private final DCMotor gearbox = DCMotor.getNEO(1);
    private final SparkMax motor;
    // The simulated motor controller wrapping the actual motor
    private final SparkMaxSim motorSim;
    private final LoggedMechanismLigament2d ligament;
    // The encoder simulator from the simulated motor
    private final SparkRelativeEncoderSim encoderSim;

    // The operation mode for the motor
    private Mode mode = Mode.EMPTY;
    // Mode start point (encoder position when mode change)
    private double startPoint = 0;

    public IntakeSimulator(SparkMax motor, LoggedMechanismLigament2d ligament) {
        this.motor = motor;
        motorSim = new SparkMaxSim(motor, gearbox);
        this.ligament = ligament;
        encoderSim = motorSim.getRelativeEncoderSim();

        //encoderSim.setPositionConversionFactor(1.0);
        encoderSim.setPosition(0.0);
        encoderSim.setInverted(false);
    }

    /**
     * Advance the simulation.
     */
    @Override
    public void stepSimulation() {
        // In this method, we update our simulation of what our intake is doing
        // First, we set our "inputs" (voltages)
        double motorOut = motorSim.getAppliedOutput() * 12.0; // * RoboRioSim.getVInVoltage();

        // Next, we set our simulated encoder's readings and simulated battery voltage
        // We use a very simplistic formula to calculate the no-load motor speed
        double rpm = motorOut * RPM_PER_VOLT;
        motorSim.iterate(rpm, 12, 0.020);

        // Finally, calculate the switch positions based off of intake-like simulation
        boolean forwardSwitch = motorSim.getForwardLimitSwitchSim().getPressed();
        double position = encoderSim.getPosition();
        if (mode == Mode.EMPTY) {
            // from empty, hitting the forward switch
            if ((position - startPoint) > FORWARD_ROTATIONS_TO_SWITCH) {
                forwardSwitch = true;
                mode = Mode.FULL;
            }
        } else {
            if ((position - startPoint) > FORWARD_ROTATIONS_TO_CLEAR) {
                // from forward, clearing forward
                mode = Mode.EMPTY;
                startPoint = position;
                forwardSwitch = false;
            } else if ((position - startPoint) > FORWARD_ROTATIONS_TO_SWITCH) {
                // make sure to keep the switch on if we are in range
                forwardSwitch = true;
            } else if ((position - startPoint) < BACKWARD_ROTATIONS_TO_CLEAR) {
                // from forward, clear backwards
                mode = Mode.EMPTY;
                startPoint = position;
                forwardSwitch = false;
            } else if ((position - startPoint) < BACKWARDS_ROTATIONS_TO_UNSWITCH) {
                // from forward, move a little backwards
                forwardSwitch = false;
            }
        }
        motorSim.getForwardLimitSwitchSim().setPressed(forwardSwitch);

        if (ligament != null) {
            ligament.setAngle(Rotations.of(encoderSim.getPosition()).in(Degrees));
        }
    }

    public SparkRelativeEncoderSim getEncoder() {
        return encoderSim;
    }

    public void close() {
        motor.close();
    }
}
