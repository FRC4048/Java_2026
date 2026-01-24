package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.commands.intake.SpinIntake;
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
import frc.robot.utils.simulation.MotorSimulator;
import frc.robot.utils.simulation.RobotVisualizer;

public class IntakeSubsystem extends SubsystemBase {
    
    public static final String LOGGING_NAME = "IntakeSubsystem";
    private final SparkMaxIo io;
    private final DigitalInputIo intakeDeploymentSwitch;

    public IntakeSubsystem(SparkMaxIo io, DigitalInputIo intakeDeploymentSwitch) {
        this.io = io;
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
    }

    public boolean isDeployed() {
        return intakeDeploymentSwitch.isPressed();
    }

    public static SparkMaxIo createMockIo() {
        return new MockSparkMaxIo(LOGGING_NAME, MotorLoggableInputs.allMetrics());
    }

    public static SparkMaxIo createRealIo() {
        return new RealSparkMaxIo(LOGGING_NAME, createMotor(), MotorLoggableInputs.allMetrics());
    }

    public static SparkMaxIo createSimIo(RobotVisualizer visualizer) {
        SparkMax motor = createMotor();
        return new SimSparkMaxIo(LOGGING_NAME, motor, MotorLoggableInputs.allMetrics(),
                new MotorSimulator(motor, visualizer.getIntakeLigament()));
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

    private static SparkMax createMotor() {
        SparkMax motor = new SparkMax(Constants.INTAKE_MOTOR_ID, SparkLowLevel.MotorType.kBrushless);
        SparkMaxConfig motorConfig = new SparkMaxConfig();
        motorConfig.idleMode(SparkBaseConfig.IdleMode.kBrake);
        motorConfig.smartCurrentLimit(Constants.NEO_CURRENT_LIMIT);
        motor.configure(
                motorConfig,
                SparkBase.ResetMode.kResetSafeParameters,
                SparkBase.PersistMode.kPersistParameters);

        return motor;
    }
    
    public DigitalInputIo getDeploymentSwitchIo() {
        return intakeDeploymentSwitch;
    }
}
