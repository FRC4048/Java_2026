package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import frc.robot.utils.logging.input.MotorLoggableInputs;
import frc.robot.utils.logging.io.pidmotor.MockSparkMaxPidMotorIo;
import frc.robot.utils.logging.io.pidmotor.RealSparkMaxPidMotorIo;
import frc.robot.utils.logging.io.pidmotor.SimSparkMaxPidMotorIo;
import frc.robot.utils.logging.io.pidmotor.SparkMaxPidConfig;
import frc.robot.utils.logging.io.pidmotor.SparkMaxPidMotor;
import frc.robot.utils.logging.io.pidmotor.SparkMaxPidMotorIo;
import frc.robot.utils.simulation.ArmParameters;
import frc.robot.utils.simulation.ArmSimulator;
import frc.robot.utils.simulation.RobotVisualizer;
import frc.robot.utils.motor.TunablePIDManager;

public class AnglerSubsystem extends SubsystemBase {

    public static final String LOGGING_NAME = "AnglerSubsystem";

    private final SparkMaxPidMotorIo io;
    private final TunablePIDManager pidManager;

    public AnglerSubsystem(SparkMaxPidMotorIo io) {
        this.io = io;
        this.pidManager = new TunablePIDManager(LOGGING_NAME, io, createPidConfig());
    }

    @Override
    public void periodic() {
        pidManager.periodic();
        io.periodic();
    }

/**
   * Gets the desired Encoder Position and uses a PID controller to get the motor there
   * Planning to add a setPosition based on an angle position soon
   */
    public void setPosition(double targetEncoderPosition) {
        io.setPidPosition(targetEncoderPosition);
    }

    public void stopMotors() {
        io.stopMotor();
    }

    public static SparkMaxPidMotorIo createMockIo() {
        return new MockSparkMaxPidMotorIo(LOGGING_NAME, MotorLoggableInputs.allMetrics());
    }

    public static SparkMaxPidMotorIo createRealIo() {
        return new RealSparkMaxPidMotorIo(LOGGING_NAME, createMotor(), MotorLoggableInputs.allMetrics());
    }

    public static SparkMaxPidMotorIo createSimIo(RobotVisualizer visualizer) {
        SparkMaxPidMotor motor = createMotor();
        ArmSimulator simulator = new ArmSimulator(motor.getNeoMotor(), createParams(), visualizer.getAnglerLigament());
        return new SimSparkMaxPidMotorIo(
                LOGGING_NAME,
                motor,
                MotorLoggableInputs.allMetrics(),
                simulator);
    }

    private static SparkMaxPidConfig createPidConfig() {
        return new SparkMaxPidConfig(false)
                .setCurrentLimit(Constants.NEO_CURRENT_LIMIT)
                .setAllowedError(.1)
                .setPidf(
                        Constants.ANGLER_P,
                        Constants.ANGLER_I,
                        Constants.ANGLER_D,
                        Constants.ANGLER_FF);
    }

    private static SparkMaxPidMotor createMotor() {
        return new SparkMaxPidMotor(Constants.ANGLER_MOTOR_ID, createPidConfig());
    }

    private static ArmParameters createParams() {
        ArmParameters params = new ArmParameters();
        params.name = "ANGLER";
        params.armGearing = Constants.ANGLER_GEARING;
        params.armInertia = Constants.ANGLER_INERTIA;
        params.armLength = Constants.ANGLER_LENGTH;
        params.armMinAngle = Constants.ANGLER_MIN_ANGLE;
        params.armMaxAngle = Constants.ANGLER_MAX_ANGLE;
        params.armSimulateGravity = Constants.ANGLER_SIMULATE_GRAVITY;
        return params;
    }
}
