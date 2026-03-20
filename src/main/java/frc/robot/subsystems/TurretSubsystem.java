package frc.robot.subsystems;

import com.revrobotics.spark.ClosedLoopSlot;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.constants.Constants;
import frc.robot.constants.GameConstants;
import frc.robot.constants.enums.ShootingState;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.diag.DiagSparkMaxEncoder;
import frc.robot.utils.diag.DiagSparkMaxSwitch;
import frc.robot.utils.logging.input.MotorLoggableInputs;
import frc.robot.utils.logging.io.pidmotor.*;
import frc.robot.utils.motor.TunablePIDManager;
import frc.robot.utils.simulation.ArmParameters;
import frc.robot.utils.simulation.ArmSimulator;
import frc.robot.utils.simulation.RobotVisualizer;

public class TurretSubsystem extends SubsystemBase {

    public static final String LOGGING_NAME = "TurretSubsystem";

    private final SparkMaxPidMotorIo io;
    private final TunablePIDManager pidManager;
    private double lastAngle = 999;

    public TurretSubsystem(SparkMaxPidMotorIo io) {
        this.io = io;
        // Create 2 tunable configs in case we want to tune both
        this.pidManager = new TunablePIDManager(LOGGING_NAME, io, createPidConfig0());
        createPidConfig1();
        stopMotors();
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
        // Decide which slot to use based on distance from target

        if (Math.abs(targetEncoderPosition - lastAngle) >= Constants.TURRET_PID_DISTANCE_THRESHOLD) {
            io.setPidPosition(targetEncoderPosition, ClosedLoopSlot.kSlot1); // longer encoder distance pid
        } else {
            io.setPidPosition(targetEncoderPosition, ClosedLoopSlot.kSlot0); // shorter encoder distance pid
        }
    }

    /**
     * Gets the desired Turret Position and uses a PID controller to get the motor there
     * @param targetAngle Desired angle position of turret in degrees
     */
    public void setAngle(double targetAngle) {
        double targetRotations = calculateRotationsForAngle(
                targetAngle,
                Constants.TURRET_ENCODER_MAX,
                Constants.TURRET_ENCODER_MIN,
                Constants.TURRET_MAX_ANGLE,
                Constants.TURRET_MIN_ANGLE);
        if (lastAngle != targetAngle) {
            setPosition(targetRotations);
            lastAngle = targetAngle;
        }
    }

    //Range translate code with a clamp
    public static double calculateRotationsForAngle(
            double targetAngle,
            double encoderHigh,
            double encoderLow,
            double angleHigh,
            double angleLow) {

        double targetEncoder = (targetAngle - angleLow) / (angleHigh - angleLow) * encoderHigh;
        return MathUtil.clamp(targetEncoder, encoderLow, encoderHigh);
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
                        new DiagSparkMaxEncoder(
                                "Turret", "Encoder", GameConstants.TURRET_DIAGS_ENCODER, motor.getNeoMotor()));

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

    // PID config for slot 0 (close range)
    private static SparkMaxPidConfig createPidConfig0() {
        return new SparkMaxPidConfig(false, ClosedLoopSlot.kSlot0)
                .setCurrentLimit(Constants.NEO_CURRENT_LIMIT)
                .setAllowedError(.1)
                .setPidf(
                        Constants.TURRET_SHORT_RANGE_P,
                        Constants.TURRET_SHORT_RANGE_I,
                        Constants.TURRET_SHORT_RANGE_D,
                        Constants.TURRET_SHORT_RANGE_FF);
    }

    // PID config for slot 1 (far range)
    private static SparkMaxPidConfig createPidConfig1() {
        return new SparkMaxPidConfig(true, ClosedLoopSlot.kSlot1)
                .setCurrentLimit(Constants.NEO_CURRENT_LIMIT)
                .setAllowedError(.1)
                .setPidf(
                        Constants.TURRET_LONG_RANGE_P,
                        Constants.TURRET_LONG_RANGE_I,
                        Constants.TURRET_LONG_RANGE_D,
                        Constants.TURRET_LONG_RANGE_FF)
                .setMaxAccel(6000)
                .setMaxVelocity(3000);
    }

    private static SparkMaxPidMotor createMotor() {
        // Use 2 configs for the PID
        return new SparkMaxPidMotor(Constants.TURRET_MOTOR_ID, createPidConfig0(), createPidConfig1());
    }

    private static ArmParameters createParams() {
        ArmParameters params = new ArmParameters();
        params.name = "TURRET";
        params.armGearing = Constants.TURRET_GEARING;
        params.armInertia = Constants.TURRET_INERTIA;
        params.armLength = Constants.TURRET_LENGTH;
        params.armMinAngle = Rotation2d.fromDegrees(Constants.TURRET_MIN_ANGLE);
        params.armMaxAngle = Rotation2d.fromDegrees(Constants.TURRET_MAX_ANGLE);
        return params;
    }
}
