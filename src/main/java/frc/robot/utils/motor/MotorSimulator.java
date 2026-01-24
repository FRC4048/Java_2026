package frc.robot.utils.motor;

import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.Rotations;

import com.revrobotics.sim.SparkMaxSim;
import com.revrobotics.sim.SparkRelativeEncoderSim;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.math.system.plant.DCMotor;
import org.littletonrobotics.junction.mechanism.LoggedMechanismLigament2d;

public class MotorSimulator {
  private static final double RPM_PER_VOLT = 100;
  // Gearbox represents a gearbox (1:1 conversion rate) with 1 or motors connected
  private final DCMotor gearbox = DCMotor.getNEO(1);
  private final SparkMax motor;
  // The simulated motor controller wrapping the actual motor
  private final SparkMaxSim motorSim;
  private final LoggedMechanismLigament2d ligament;
  // The encoder simulator from the simulated motor
  private final SparkRelativeEncoderSim encoderSim;

  public MotorSimulator(SparkMax motor, LoggedMechanismLigament2d ligament) {
    this.motor = motor;
    motorSim = new SparkMaxSim(motor, gearbox);
    this.ligament = ligament;
    encoderSim = motorSim.getRelativeEncoderSim();

    encoderSim.setPositionConversionFactor(1.0);
    encoderSim.setPosition(0.0);
    encoderSim.setInverted(false);
  }

  /** Advance the simulation. */
  public void stepSimulation() {
    // In this method, we update our simulation of what our elevator is doing
    // First, we set our "inputs" (voltages)
    double motorOut = motorSim.getAppliedOutput() * 12.0; // * RoboRioSim.getVInVoltage();

    // Finally, we set our simulated encoder's readings and simulated battery voltage
    // We use a very simplistic formula to calculate the no-load motor speed
    double rpm = motorOut * RPM_PER_VOLT;
    motorSim.iterate(rpm, 12, 0.020);
    // each rotation of the motor is one rotation of the ligament
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
