package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.commands.intake.SpinIntake;
import frc.robot.constants.Constants;
import frc.robot.constants.GameConstants;
import frc.robot.constants.enums.DeploymentState;
import frc.robot.utils.diag.DiagSparkMaxEncoder;
import frc.robot.utils.logging.input.DigitalInputLoggableInputs;
import frc.robot.utils.logging.input.MotorLoggableInputs;
import frc.robot.utils.logging.io.motor.MockSparkMaxIo;
import frc.robot.utils.logging.io.motor.RealSparkMaxIo;
import frc.robot.utils.logging.io.motor.SimSparkMaxIo;
import frc.robot.utils.logging.io.motor.SparkMaxIo;
import frc.robot.utils.simulation.MotorSimulator;
import frc.robot.utils.simulation.RobotVisualizer;

public class IntakeSubsystem extends SubsystemBase {

    public static final String LOGGING_NAME = "IntakeSubsystem";
    private final SparkMaxIo io;

    public IntakeSubsystem(SparkMaxIo io) {
        this.io = io;
    }

    public void setSpeed(double speed) {
        io.set(speed);
    }

    public void stopMotors() {
        io.stopMotor();
    }

    @Override
    public void periodic() {
        io.periodic();
    }

    public static SparkMaxIo createMockIo() {
        return new MockSparkMaxIo(LOGGING_NAME, MotorLoggableInputs.allMetrics());
    }

    public static SparkMaxIo createRealIo() {

        SparkMax motor = createMotor();

        Robot.getDiagnostics()
                .addDiagnosable(
                        new DiagSparkMaxEncoder(
                                "Intake Roller", "Encoder", GameConstants.INTAKE_ROLLER_DIAGS_ENCODER, motor));

        return new RealSparkMaxIo(LOGGING_NAME, motor, MotorLoggableInputs.allMetrics());
    }

    public static SparkMaxIo createSimIo(RobotVisualizer visualizer) {
        SparkMax motor = createMotor();
        return new SimSparkMaxIo(LOGGING_NAME, motor, MotorLoggableInputs.allMetrics(),
                new MotorSimulator(motor, visualizer.getIntakeLigament()));
    }

    private static SparkMax createMotor() {
        SparkMax motor = new SparkMax(Constants.INTAKE_MOTOR_ID, SparkLowLevel.MotorType.kBrushless);
        SparkMaxConfig motorConfig = new SparkMaxConfig();
        motorConfig.idleMode(SparkBaseConfig.IdleMode.kBrake);
        motorConfig.smartCurrentLimit(Constants.NEO_CURRENT_LIMIT);
        motor.configure(
                motorConfig,
                ResetMode.kResetSafeParameters,
                PersistMode.kPersistParameters);
        return motor;
    }
}