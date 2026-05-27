package frc.robot.subsystems;


import com.ctre.phoenix6.controls.Follower;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import edu.wpi.first.wpilibj.sysid.SysIdRoutineLog;

import static edu.wpi.first.units.Units.*;
import frc.robot.Robot;
import frc.robot.constants.Constants;
import frc.robot.constants.GameConstants;
import frc.robot.utils.diag.DiagSparkMaxEncoder;
import frc.robot.utils.logging.input.MotorLoggableInputs;
import frc.robot.utils.logging.io.motor.RealSparkMaxIo;
import frc.robot.utils.logging.io.motor.SparkMaxIo;
import frc.robot.utils.logging.io.pidmotor.MockSparkMaxPidMotorIo;
import frc.robot.utils.logging.io.pidmotor.RealSparkMaxPidMotorIo;
import frc.robot.utils.logging.io.pidmotor.SimSparkMaxPidMotorIo;
import frc.robot.utils.logging.io.pidmotor.SparkMaxPidConfig;
import frc.robot.utils.logging.io.pidmotor.SparkMaxPidMotor;
import frc.robot.utils.logging.io.pidmotor.SparkMaxPidMotorIo;
import frc.robot.utils.motor.TunablePIDManager;
import frc.robot.utils.simulation.MotorSimulator;
import frc.robot.utils.simulation.RobotVisualizer;

public class ShooterSubsystem extends SubsystemBase {
    
    public static final String LOGGING_NAME = "ShooterSubsystem";
    
    private final SparkMaxPidMotorIo io;
    private SparkMaxIo followerIo;
    private final TunablePIDManager pidManager;
    private final SysIdRoutine sysIdRoutine;

    public ShooterSubsystem(MotorPairIO motorPairIO) {
        this((SparkMaxPidMotorIo) motorPairIO.mainMotor);
        this.followerIo = motorPairIO.followerMotor;
        stopMotors();
    }

    public ShooterSubsystem(SparkMaxPidMotorIo io) {
        this.io = io ;
        this.pidManager = new TunablePIDManager(LOGGING_NAME, io, createPidConfig());
        this.sysIdRoutine = new SysIdRoutine(
                new SysIdRoutine.Config(),
                new SysIdRoutine.Mechanism(
                        this::sysIdDrive,
                        this::sysIdLog,
                        this
                )
        );
        //  io.setPid(0.0000002, 0.000015, 0.000015); // Pid needs tuning
        stopMotors();
    }

    private static SparkMaxPidConfig createPidConfig() {
        return new SparkMaxPidConfig(false)
                .setCurrentLimit(Constants.NEO_CURRENT_LIMIT)
                .setAllowedError(.1)
                .setIdleMode(IdleMode.kCoast)
                .setPid(0.0003,0.000000005,0.005)
                .setFF(0.0025);
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
        pidManager.periodic();
        io.periodic();
    }

    private void sysIdDrive(Voltage voltage) {
        io.setVoltage(voltage.in(Volts));
        if (followerIo != null) {
            followerIo.setVoltage(voltage.in(Volts));
        }
    }

    private void sysIdLog(SysIdRoutineLog log) {
        log.motor("shooter")
                .voltage(Volts.of(io.getAppliedOutput() * RobotController.getBatteryVoltage()))
                .angularPosition(Rotations.of(io.getEncoderPosition()))
                .angularVelocity(RPM.of(io.getEncoderVelocity()));
    }

    public Command sysIdQuasistatic(SysIdRoutine.Direction direction) {
        return sysIdRoutine.quasistatic(direction);
    }

    public Command sysIdDynamic(SysIdRoutine.Direction direction) {
        return sysIdRoutine.dynamic(direction);
    }

    public static SparkMaxPidMotorIo createMockIo() {
        return new MockSparkMaxPidMotorIo(LOGGING_NAME, MotorLoggableInputs.allMetrics());
    }

    public static MotorPairIO createRealIo() {
        MotorPair motor = createMotor();
        RealSparkMaxIo motorIO = new RealSparkMaxPidMotorIo(LOGGING_NAME, motor.mainMotor, MotorLoggableInputs.allMetrics());
        RealSparkMaxIo followerIO = new RealSparkMaxIo(LOGGING_NAME, motor.followerMotor, MotorLoggableInputs.allMetrics());
        Robot.getDiagnostics()
                .addDiagnosable(
                        new DiagSparkMaxEncoder(
                                "Shooter", "Encoder", GameConstants.SHOOTER_DIAGS_ENCODER, motor.mainMotor.getNeoMotor()));

        return new MotorPairIO(motorIO, followerIO);
    }

    public static SparkMaxPidMotorIo createSimIo(RobotVisualizer visualizer) {
        SparkMaxPidMotor motor = createMotor().mainMotor;
        return new SimSparkMaxPidMotorIo(LOGGING_NAME, motor, MotorLoggableInputs.allMetrics(), new MotorSimulator(motor.getNeoMotor(), visualizer.getShooterLigament()));
    }

    public static MotorPair createMotor() {
        SparkMax followerIo = new SparkMax(Constants.SHOOTER_FOLLOWER_MOTOR_ID, SparkLowLevel.MotorType.kBrushless);
        SparkMaxConfig followerConfig = new SparkMaxConfig();
        followerConfig.follow(Constants.SHOOTER_MOTOR_ID, true).idleMode(IdleMode.kCoast);
        followerIo.configure(followerConfig, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
        return new MotorPair(new SparkMaxPidMotor(Constants.SHOOTER_MOTOR_ID, createPidConfig()),followerIo);
    }
    public record MotorPair(SparkMaxPidMotor mainMotor, SparkMax followerMotor){}
    public record MotorPairIO(RealSparkMaxIo mainMotor, RealSparkMaxIo followerMotor){}
}
