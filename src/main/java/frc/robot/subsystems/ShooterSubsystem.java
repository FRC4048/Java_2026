package frc.robot.subsystems;



import com.ctre.phoenix6.controls.Follower;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import frc.robot.utils.logging.input.MotorLoggableInputs;
import frc.robot.utils.logging.io.motor.SparkMaxIo;
import frc.robot.utils.logging.io.pidmotor.MockSparkMaxPidMotorIo;
import frc.robot.utils.logging.io.pidmotor.RealSparkMaxPidMotorIo;
import frc.robot.utils.logging.io.pidmotor.SimSparkMaxPidMotorIo;
import frc.robot.utils.logging.io.pidmotor.SparkMaxPidConfig;
import frc.robot.utils.logging.io.pidmotor.SparkMaxPidMotor;
import frc.robot.utils.logging.io.pidmotor.SparkMaxPidMotorIo;
import frc.robot.utils.simulation.MotorSimulator;
import frc.robot.utils.simulation.RobotVisualizer;

public class ShooterSubsystem extends SubsystemBase {
    
    public static final String LOGGING_NAME = "ShooterSubsystem";
    
    private final SparkMaxPidMotorIo io;
    private final SparkMax followerMotor;
    private final SparkMaxConfig followerConfig;

    public ShooterSubsystem(SparkMaxPidMotorIo io) {
        this.io = io;
        io.setPid(0.0000002, 0.000015, 0.000015); // Pid needs tuning
        followerMotor = new SparkMax(Constants.SHOOTER_FOLLOWER_MOTOR_ID, SparkLowLevel.MotorType.kBrushless);
        followerConfig = new SparkMaxConfig();
        followerConfig.follow(Constants.SHOOTER_MOTOR_ID, true);
        followerMotor.configure(followerConfig, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);

    }

    // setSpeed expects a power value from -1 to 1
    public void setSpeed(double speed) {
        io.set(speed);
    }

    public void stopMotors() {
        io.stopMotor();
    }

    // setPidVelocity expects a speed in RPM
    public void setPidVelocity(double velocity) {
        io.setPidVelocity(velocity);
    }

    @Override
    public void periodic() {
        io.periodic();
    }

    public static SparkMaxPidMotorIo createMockIo() {
        return new MockSparkMaxPidMotorIo(LOGGING_NAME, MotorLoggableInputs.allMetrics());
    }

    public static SparkMaxPidMotorIo createRealIo() {
        return new RealSparkMaxPidMotorIo(LOGGING_NAME, createMotor(), MotorLoggableInputs.allMetrics());
    }

    public static SparkMaxPidMotorIo createSimIo(RobotVisualizer visualizer) {
        SparkMaxPidMotor motor = createMotor();
        return new SimSparkMaxPidMotorIo(LOGGING_NAME, motor, MotorLoggableInputs.allMetrics(), new MotorSimulator(motor.getNeoMotor(), visualizer.getShooterLigament()));
    }

    public static SparkMaxPidMotor createMotor() {
        return new SparkMaxPidMotor(Constants.SHOOTER_MOTOR_ID, true);
    }

}
