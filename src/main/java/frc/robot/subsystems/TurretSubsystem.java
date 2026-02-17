package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.constants.Constants;
import frc.robot.constants.GameConstants;
import frc.robot.utils.diag.DiagSparkMaxSwitch;
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

public class TurretSubsystem extends SubsystemBase {

    public static final String LOGGING_NAME = "TurretSubsystem";

    private final SparkMaxPidMotorIo io;
    private final TunablePIDManager pidManager;

    public TurretSubsystem(SparkMaxPidMotorIo io) {
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
   */
    public void setPosition(double targetEncoderPosition) {
        io.setPidPosition(targetEncoderPosition);
    }   

    /**
   * Gets the desired Turret Position and uses a PID controller to get the motor there
   * @param targetAngle Desired angle position of turret in degrees */
  
    public void setAngle(double targetAngle) {
        double targetRotations = calculateRotationsForAngle(
                targetAngle,
                Constants.TURRET_ENCODER_MAX,
                Constants.TURRET_ENCODER_MIN,
                Constants.TURRET_RIGHT_ANGLE,
                Constants.TURRET_LEFT_ANGLE);
        setPosition(targetRotations);
    }

    //Range translate code with a clamp
    public static double calculateRotationsForAngle(
            double targetAngle,
            double encoderHigh,
            double encoderLow,
            double angleHigh,
            double angleLow) {

        double multiplier = (encoderHigh - encoderLow) / (angleHigh - angleLow);
        double targetRotations = targetAngle * multiplier - (multiplier * angleHigh - encoderHigh);
        return MathUtil.clamp(targetRotations, encoderLow, encoderHigh);
    }

    /**
     * Drive forward at the homing speed. Command should stop when limit is hit.
     */
    public void runForward() {
        io.set(Math.abs(Constants.TURRET_LIMIT_SPEED));
    }

    /**
     * Drive reverse at the homing speed. Command should stop when limit is hit.
     */
    public void runReverse() {
        io.set(-Math.abs(Constants.TURRET_LIMIT_SPEED));
    }

    /**
     * Reset the encoder position to zero.
     */
    public void resetEncoderToZero() {
        io.resetEncoderPosition(0.0);
    }

    public boolean isAtForwardLimit() {
        return io.isFwdSwitchPressed();
    }

    public boolean isAtReverseLimit() {
        return io.isRevSwitchPressed();
    }

    public void stopMotors() {
        io.stopMotor();
    }

    public static SparkMaxPidMotorIo createMockIo() {
        return new MockSparkMaxPidMotorIo(LOGGING_NAME, MotorLoggableInputs.allMetrics());
    }

    public static SparkMaxPidMotorIo createRealIo() {

        SparkMaxPidMotor motor = createMotor();

        Robot.getDiagnostics()
        .addDiagnosable(
            new DiagSparkMaxSwitch(
                "Turret", "ForwardLimit", motor.getNeoMotor(), DiagSparkMaxSwitch.Direction.FORWARD));

        Robot.getDiagnostics()
        .addDiagnosable(
            new DiagSparkMaxSwitch(
                "Turret", "ReverseLimit", motor.getNeoMotor(), DiagSparkMaxSwitch.Direction.REVERSE));

        return new RealSparkMaxPidMotorIo(LOGGING_NAME, motor, MotorLoggableInputs.allMetrics());
    }

    public static SparkMaxPidMotorIo createSimIo(RobotVisualizer visualizer) {
        SparkMaxPidMotor motor = createMotor();
        ArmSimulator simulator = new ArmSimulator(motor.getNeoMotor(), createParams(), visualizer.getTurretLigament());
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
                        Constants.TURRET_P,
                        Constants.TURRET_I,
                        Constants.TURRET_D,
                        Constants.TURRET_FF);
    }

    private static SparkMaxPidMotor createMotor() {
        return new SparkMaxPidMotor(Constants.TURRET_MOTOR_ID, createPidConfig());
    }

    private static ArmParameters createParams() {
        ArmParameters params = new ArmParameters();
        params.name = "TURRET";
        params.armGearing = Constants.TURRET_GEARING;
        params.armInertia = Constants.TURRET_INERTIA;
        params.armLength = Constants.TURRET_LENGTH;
        params.armMinAngle = Constants.TURRET_MIN_ANGLE;
        params.armMaxAngle = Constants.TURRET_MAX_ANGLE;
        return params;
    }
}
