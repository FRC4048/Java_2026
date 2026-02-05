package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import frc.robot.utils.logging.input.MotorLoggableInputs;
import frc.robot.utils.logging.io.pidmotor.MockSparkMaxPidMotorIo;
import frc.robot.utils.logging.io.pidmotor.RealSparkMaxPidMotorIo;
import frc.robot.utils.logging.io.pidmotor.SimSparkMaxPidMotorIo;
import frc.robot.utils.logging.io.pidmotor.SparkMaxPidConfig;
import frc.robot.utils.logging.io.pidmotor.SparkMaxPidMotor;
import frc.robot.utils.logging.io.pidmotor.SparkMaxPidMotorIo;
import frc.robot.utils.simulation.MotorSimulator;
import frc.robot.utils.simulation.RobotVisualizer;

public class AnglerSubsystem extends SubsystemBase {

    public static final String LOGGING_NAME = "AnglerSubsystem";

    private final SparkMaxPidMotorIo io;

    public AnglerSubsystem(SparkMaxPidMotorIo io) {
        this.io = io;
    }

    @Override
    public void periodic() {
        io.periodic();
    }

    public void setPosition(double targetRotations) {
        io.setPidPosition(targetRotations);
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
        return new SimSparkMaxPidMotorIo(
                LOGGING_NAME,
                motor,
                MotorLoggableInputs.allMetrics(),
                new MotorSimulator(motor.getNeoMotor(), null));
    }

    private static SparkMaxPidMotor createMotor() {
        return new SparkMaxPidMotor(Constants.ANGULAR_MOTOR_ID, createPidConfig());
    }

    private static SparkMaxPidConfig createPidConfig() {
        return new SparkMaxPidConfig(false)
                .setCurrentLimit(Constants.NEO_CURRENT_LIMIT)
                .setPidf(
                        Constants.ANGLER_P,
                        Constants.ANGLER_I,
                        Constants.ANGLER_D,
                        Constants.ANGLER_FF);
    }
}
