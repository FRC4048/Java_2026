// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.utils.logging.input.MotorLoggableInputs;
import frc.robot.utils.logging.io.motor.MockSparkMaxIo;
import frc.robot.utils.logging.io.motor.RealSparkMaxIo;
import frc.robot.utils.logging.io.motor.SimSparkMaxIo;
import frc.robot.utils.logging.io.motor.SparkMaxIo;
import frc.robot.utils.simulation.ArmParameters;
import frc.robot.utils.simulation.ArmSimulator;
import frc.robot.utils.simulation.RobotVisualizer;

// The Tilt subsystem extends and retracts the Algae-Go-Bye-Bye mechanism.

public class TiltSubsystem extends SubsystemBase {
    public static final String LOGGING_NAME = "TiltSubsystem";
    private final SparkMaxIo io;

    public TiltSubsystem(SparkMaxIo io) {
        this.io = io;
    }

    public void setSpeed(double speed) {
        io.set(speed);
    }

    public void stopMotors() {
        io.stopMotor();
    }

    public boolean isAtTop() {
        // Arm motor is inverted - rev switch is at top
        return io.isRevSwitchPressed();
    }

    public boolean isAtBottom() {
        // Arm motor is inverted - fwd switch is at bottom
        return io.isFwdSwitchPressed();
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
        ArmSimulator simulator = new ArmSimulator(motor, createParams(), visualizer.getTiltLigament());
        return new SimSparkMaxIo(LOGGING_NAME, motor, MotorLoggableInputs.allMetrics(), simulator);
    }

    private static SparkMax createMotor() {
        SparkMax motor = new SparkMax(Constants.TILT_MOTOR_ID, SparkLowLevel.MotorType.kBrushless);
        SparkMaxConfig motorConfig = new SparkMaxConfig();
        motorConfig.idleMode(SparkBaseConfig.IdleMode.kBrake);
        motorConfig.smartCurrentLimit(Constants.NEO_CURRENT_LIMIT);
        motor.configure(
                motorConfig,
                SparkBase.ResetMode.kResetSafeParameters,
                SparkBase.PersistMode.kPersistParameters);

        return motor;
    }

    private static ArmParameters createParams() {
        ArmParameters params = new ArmParameters();
        params.name = "Tilt";
        params.armGearing = Constants.TILT_GEARING;
        params.armInertia = Constants.TILT_INERTIA;
        params.armLength = Constants.TILT_LENGTH;
        params.armMinAngle = Constants.TILT_MIN_ANGLE;
        params.armMaxAngle = Constants.TILT_MAX_ANGLE;
        params.armSimulateGravity = Constants.TILT_SIMULATE_GRAVITY;
        return params;
    }
}
