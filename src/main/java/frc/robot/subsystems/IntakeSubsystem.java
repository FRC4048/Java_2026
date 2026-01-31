package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.intake.SpinIntake;
import frc.robot.constants.Constants;
import frc.robot.utils.logging.input.DigitalInputLoggableInputs;
import frc.robot.utils.logging.input.MotorLoggableInputs;
import frc.robot.utils.logging.io.motor.DigitalInputIo;
import frc.robot.utils.logging.io.motor.MockDigitalInputIo;
import frc.robot.utils.logging.io.motor.MockSparkMaxIo;
import frc.robot.utils.logging.io.motor.RealDigitalInputIo;
import frc.robot.utils.logging.io.motor.RealSparkMaxIo;
import frc.robot.utils.logging.io.motor.SimDigitalInputIo;
import frc.robot.utils.logging.io.motor.SimSparkMaxIo;
import frc.robot.utils.logging.io.motor.SparkMaxIo;
import frc.robot.utils.logging.io.pidmotor.*;
import frc.robot.utils.motor.TunablePIDManager;
import frc.robot.utils.simulation.MotorSimulator;
import frc.robot.utils.simulation.RobotVisualizer;

public class IntakeSubsystem extends SubsystemBase {
    
    public static final String LOGGING_NAME = "IntakeSubsystem";
    private final SparkMaxPidMotorIo io;
    private final DigitalInputIo intakeDeploymentSwitch;
    private final TunablePIDManager pidConfig;

    public IntakeSubsystem(SparkMaxPidMotorIo io, DigitalInputIo intakeDeploymentSwitch) {
        this.io = io;
        SparkMaxPidConfig neoPidConfig = new SparkMaxPidConfig(true);
        this.pidConfig = new TunablePIDManager("RollerSubsystem", io, neoPidConfig);
        this.intakeDeploymentSwitch = intakeDeploymentSwitch;
        setDefaultCommand(new SpinIntake(this));
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
        intakeDeploymentSwitch.periodic();
        pidConfig.periodic();
    }

    public boolean isDeployed() {
        return intakeDeploymentSwitch.isPressed();
    }

    public static SparkMaxPidMotorIo createMockIo() {
        return new MockSparkMaxPidMotorIo(LOGGING_NAME, MotorLoggableInputs.allMetrics());
    }

    public static SparkMaxPidMotorIo createRealIo() {
        return new RealSparkMaxPidMotorIo(LOGGING_NAME, createMotor(), MotorLoggableInputs.allMetrics());
    }

    public static SparkMaxPidMotorIo createSimIo(RobotVisualizer visualizer) {
        SparkMaxPidMotor motor = createMotor();
        return new SimSparkMaxPidMotorIo(LOGGING_NAME, motor, MotorLoggableInputs.allMetrics(),
                new MotorSimulator(motor.getNeoMotor(), visualizer.getIntakeLigament()));
    }

    public static DigitalInputIo createMockDeploymentSwitch() {
    return new MockDigitalInputIo(
            LOGGING_NAME + "/DeploymentSwitch",
            new DigitalInputLoggableInputs()
    );
}

public static DigitalInputIo createRealDeploymentSwitch() {
    return new RealDigitalInputIo(
            LOGGING_NAME + "/DeploymentSwitch",
            new DigitalInput(Constants.INTAKE_DIGITAL_INPUT_CHANNEL),
            new DigitalInputLoggableInputs()
    );
    
}

public static DigitalInputIo createSimDeploymentSwitch() {
    return new SimDigitalInputIo(
        LOGGING_NAME + "/DeploymentSwitch",
        new DigitalInput(Constants.INTAKE_DIGITAL_INPUT_CHANNEL),
        new DigitalInputLoggableInputs()
    );
}

    private static SparkMaxPidMotor createMotor() {
        SparkMaxPidMotor motor = new SparkMaxPidMotor(Constants.INTAKE_MOTOR_ID, true);
        SparkMaxConfig motorConfig = new SparkMaxConfig();
        motorConfig.idleMode(SparkBaseConfig.IdleMode.kBrake);
        motorConfig.smartCurrentLimit(Constants.NEO_CURRENT_LIMIT);
        motor.getNeoMotor().configure(
                motorConfig,
                ResetMode.kResetSafeParameters,
                PersistMode.kPersistParameters);
        return motor;
    }
    
    public DigitalInputIo getDeploymentSwitchIo() {
        return intakeDeploymentSwitch;
    }
}
