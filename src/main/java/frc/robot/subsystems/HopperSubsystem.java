package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.constants.Constants;
import frc.robot.constants.GameConstants;
import frc.robot.utils.diag.DiagSparkMaxEncoder;
import frc.robot.utils.logging.input.MotorLoggableInputs;
import frc.robot.utils.logging.io.motor.MockSparkMaxIo;
import frc.robot.utils.logging.io.motor.RealSparkMaxIo;
import frc.robot.utils.logging.io.motor.SimSparkMaxIo;
import frc.robot.utils.logging.io.motor.SparkMaxIo;
import frc.robot.utils.simulation.MotorSimulator;
import frc.robot.utils.simulation.RobotVisualizer;

//the hopper subsystem spins the hopper such that the fuel can be transported to the feeder

public class HopperSubsystem extends SubsystemBase{
    public static final String LOGGING_NAME = "HopperSubsystem";
    private final SparkMaxIo io;

    public HopperSubsystem(SparkMaxIo io) {
        this.io = io;

        Robot.getDiagnostics()
        .addDiagnosable(
            new DiagSparkMaxEncoder(
                "Hopper", "Encoder", GameConstants.HOPPER_DIAGS_ENCODER, io));
    }

    public void setSpeed(double speed){
        io.set(speed);
    }

    public void stopMotors(){
        io.stopMotor();
    }

    public boolean isLimitSwitchPressed(){
        return io.isFwdSwitchPressed();
    }

    @Override
    public void periodic() {
        // TODO Auto-generated method stub
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
                new MotorSimulator(motor, visualizer.getHopperLigament()));
    }

     private static SparkMax createMotor() {
        SparkMax motor = new SparkMax(Constants.HOPPER_MOTOR_ID, SparkLowLevel.MotorType.kBrushless);
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

