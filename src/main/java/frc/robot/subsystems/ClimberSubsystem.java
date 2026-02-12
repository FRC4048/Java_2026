package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
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
import frc.robot.utils.simulation.MotorSimulator;
import frc.robot.utils.simulation.RobotVisualizer;

public class ClimberSubsystem extends SubsystemBase {
    
    public static final String LOGGING_NAME = "ClimberSubsystem";
    private final SparkMaxIo io;
    private final DigitalInputIo limitSwitch;

    public ClimberSubsystem(SparkMaxIo io, DigitalInputIo limitSwitch) {
       this.io = io;
       this.limitSwitch = limitSwitch;
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
        return new RealSparkMaxIo(LOGGING_NAME, createMotor(), MotorLoggableInputs.allMetrics());
    }

    public static SparkMaxIo createSimIo(RobotVisualizer visualizer) {
        SparkMax motor = createMotor();
        return new SimSparkMaxIo(LOGGING_NAME, motor, MotorLoggableInputs.allMetrics(),
                new MotorSimulator(motor, visualizer.getClimberLigament()));
    }

    public static DigitalInputIo createMockDeploymentSwitch() {
    return new MockDigitalInputIo(
            LOGGING_NAME + "/DeploymentSwitch",
            new DigitalInputLoggableInputs()
    );
}    

public static DigitalInputIo createSimDeploymentSwitch() {
    return new SimDigitalInputIo(
        LOGGING_NAME + "/DeploymentSwitch",
        new DigitalInput(Constants.CLIMBER_DIGITAL_INPUT_CHANNEL),
        new DigitalInputLoggableInputs()
    );
}

public static DigitalInputIo createRealDeploymentSwitch() {
    return new RealDigitalInputIo(
            LOGGING_NAME + "/DeploymentSwitch",
            new DigitalInput(Constants.CLIMBER_DIGITAL_INPUT_CHANNEL),
            new DigitalInputLoggableInputs()
    );

}

public DigitalInputIo getLimitSwitch() { 
    return limitSwitch;
}
    private static SparkMax createMotor() {
        SparkMax motor = new SparkMax(Constants.CLIMBER_MOTOR_ID, SparkLowLevel.MotorType.kBrushless);
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