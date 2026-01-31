package frc.robot.utils.simulation;
import com.revrobotics.sim.SparkMaxSim;
import com.revrobotics.sim.SparkRelativeEncoderSim;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.math.system.plant.DCMotor;
import org.littletonrobotics.junction.mechanism.LoggedMechanismLigament2d;

import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.Rotations;

public class AngularMotorSimulator implements Simulator {
    private static final double RPM_PER_VOLT = 100; // match MotorSimulator

    // physical motor model used by other sims
    private final DCMotor gearbox = DCMotor.getNEO(1);
    private final SparkMax motor;
    private final SparkMaxSim motorSim;
    private final SparkRelativeEncoderSim encoderSim;
    private final LoggedMechanismLigament2d ligament;
    private double target = 0;

    public AngularMotorSimulator(SparkMax motor, LoggedMechanismLigament2d ligament) {
        this.motor = motor;
        this.motorSim = new SparkMaxSim(motor, gearbox);
        this.ligament = ligament;
        this.encoderSim = motorSim.getRelativeEncoderSim();
        encoderSim.setPosition(0.0);
        encoderSim.setInverted(false);
    }

    @Override
    public void stepSimulation() {
        double motorOut = motorSim.getAppliedOutput() * 12.0;
        double rpm = motorOut * RPM_PER_VOLT;
        motorSim.iterate(rpm, 12, 0.020);

        if (ligament != null) {
            ligament.setAngle(Rotations.of(encoderSim.getPosition()).in(Degrees));
        }
    }

    @Override
    public void setTargetPosition(double rotations) {
        target = rotations;
    }

    public SparkRelativeEncoderSim getEncoder() {
        return encoderSim;
    }

    public void close() {
        motor.close();
    }
}
