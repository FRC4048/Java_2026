package frc.robot.utils.simulation;

import com.revrobotics.sim.SparkMaxSim;
import com.revrobotics.sim.SparkRelativeEncoderSim;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.units.measure.Angle;
import org.littletonrobotics.junction.mechanism.LoggedMechanismLigament2d;

import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.Rotations;

/**
 * A simulator for a climber mechanism: A motor connected to a winch that has a rope (or a strap)
 * wrapped around a shaft. The shaft is wrapping the rope around it and pulling on the hook that
 * pulls the robot up. There are two limit switches (connected to the motor controller). The forward
 * limit switch is triggered when the hook in pulled all the way in. Once the motor drivers forward
 * that switch gets released. When the hook gets to the extended-most position the second switch
 * gets triggered (this one is connected to the "reverse" position on the motor controller and so
 * will not stop the motor). At that point, the hook gets lodged in the climbing "bar" (the "cage")
 * and the motor can be driven forward again until the forward limit switch is triggered again.
 * FORWARD_ROTATIONS_TO_REVERSE_SWITCH are the number of rotations until the first ("reverse")
 * switch is hit, FORWARD_ROTATIONS_TO_FORWARD_SWITCH are the number of rotations until the second
 * ("forward") switch is hit GEARING_RATIO is the gearbox ratio for the motor.
 */
public class ClimberSimulator implements Simulator {
    // number of rotations from start point to where piece hits reverse switch
    // This is also the position of full extension and the midway point, where the rope is unwound and
    // is
    // beginning to rewind again
    public static final Angle FORWARD_ROTATIONS_TO_REVERSE_SWITCH = Rotations.of(40);
    // number of rotations from start point to where the piece is back in the starting position (and
    // hits the forsward switch again)
    public static final Angle FORWARD_ROTATIONS_TO_FORWARD_SWITCH = Rotations.of(80);
    public static final Angle ARM_MIN_ANGLE = Degrees.of(0);
    public static final Angle ARM_MAX_ANGLE = Degrees.of(90);
    // The switch margin of error
    public static final double SWITCH_MARGIN = 1.0;
    private static final double RPM_PER_VOLT = 100;

    // Gearbox represents a gearbox (1:1 conversion rate) with 1 or motors connected
    private final DCMotor gearbox = DCMotor.getNEO(1);
    private final SparkMax motor;
    // The simulated motor controller wrapping the actual motor
    private final SparkMaxSim motorSim;
    private final LoggedMechanismLigament2d ligament;
    // The encoder simulator from the simulated motor
    private final SparkRelativeEncoderSim encoderSim;

    public ClimberSimulator(SparkMax motor, LoggedMechanismLigament2d ligament) {
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
        // In this method, we update our simulation of what our climber is doing
        // First, we set our "inputs" (voltages)
        double motorOut = motorSim.getAppliedOutput() * 12.0; // * RoboRioSim.getVInVoltage();

        // motor cannot run in reverse because of the ratchet
        if (motorOut < 0) {
            return;
        }

        // Next, we set our simulated encoder's readings and simulated battery voltage
        // We use a very simplistic formula to calculate the no-load motor speed
        double rpm = motorOut * RPM_PER_VOLT;
        motorSim.iterate(rpm, 12, 0.020);
        double position = encoderSim.getPosition();

        // Finally, calculate the switch positions based off of climber-like simulation
        boolean forwardSwitch =
                (Math.abs(FORWARD_ROTATIONS_TO_FORWARD_SWITCH.in(Rotations) - position) < SWITCH_MARGIN)
                        || (position < SWITCH_MARGIN);

        boolean reverseSwitch =
                Math.abs(FORWARD_ROTATIONS_TO_REVERSE_SWITCH.in(Rotations) - position) < SWITCH_MARGIN;

        motorSim.getForwardLimitSwitchSim().setPressed(forwardSwitch);
        motorSim.getReverseLimitSwitchSim().setPressed(reverseSwitch);

        if (position < 40) {
            ligament.setAngle(fromRotations(position).plus(ARM_MIN_ANGLE).times(-1).in(Degrees));
        } else if (position > 40) {
            ligament.setAngle(
                    fromRotations(position).plus(ARM_MAX_ANGLE).plus(ARM_MAX_ANGLE).in(Degrees));
        }
    }

    public void close() {
        motor.close();
    }

    /**
     * Calculate the mechanism arm angle based off of the rotations of the motor.
     *
     * @param rotations the encoder rotations
     * @return the mechanism (arm) angle
     */
    private Angle fromRotations(double rotations) {
        double adjustedRotations;
        if (rotations <= FORWARD_ROTATIONS_TO_FORWARD_SWITCH.in(Rotations)) {
            // moving in the first half of the motion
            adjustedRotations = rotations;
        } else {
            // moving in the second half of the motion
            adjustedRotations = FORWARD_ROTATIONS_TO_FORWARD_SWITCH.in(Rotations) - rotations;
        }
        // linear translation from motor rotations to arm rotations
        double armRotations =
                (adjustedRotations / FORWARD_ROTATIONS_TO_REVERSE_SWITCH.in(Rotations))
                        * (ARM_MAX_ANGLE.minus(ARM_MIN_ANGLE)).in(Rotations);
        return Rotations.of(armRotations);
    }
}
