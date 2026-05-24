// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.commands.intakeDeployment.RunDeployer;
import frc.robot.constants.Constants;
import frc.robot.constants.GameConstants;
import frc.robot.constants.enums.DeploymentState;
import frc.robot.utils.diag.DiagSparkMaxEncoder;
import frc.robot.utils.diag.DiagSparkMaxSwitch;
import frc.robot.utils.diag.DiagSparkMaxSwitch.Direction;
import frc.robot.utils.logging.input.MotorLoggableInputs;
import frc.robot.utils.logging.io.motor.MockSparkMaxIo;
import frc.robot.utils.logging.io.motor.RealSparkMaxIo;
import frc.robot.utils.logging.io.motor.SimSparkMaxIo;
import frc.robot.utils.logging.io.motor.SparkMaxIo;
import frc.robot.utils.simulation.ArmParameters;
import frc.robot.utils.simulation.ArmSimulator;
import frc.robot.utils.simulation.MotorSimulator;
import frc.robot.utils.simulation.RobotVisualizer;

public class IntakeDeployerSubsystem extends SubsystemBase {
  public static final String LOGGING_NAME = "IntakeDeployer";
  public DeploymentState deploymentState = DeploymentState.UP;
  private final SparkMaxIo io;

  public IntakeDeployerSubsystem(SparkMaxIo io) {
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
                "Intake Deployer", "Encoder", GameConstants.INTAKE_DEPLOYER_DIAGS_ENCODER, motor));

    Robot.getDiagnostics()
        .addDiagnosable(
            new DiagSparkMaxSwitch(
                "Intake Deployer", "ForwardLimit", motor, DiagSparkMaxSwitch.Direction.FORWARD));

    Robot.getDiagnostics()
        .addDiagnosable(
            new DiagSparkMaxSwitch(
                "Intake Deployer", "ReverseLimit", motor, DiagSparkMaxSwitch.Direction.REVERSE));

    return new RealSparkMaxIo(LOGGING_NAME, motor, MotorLoggableInputs.allMetrics());
  }

  public static SparkMaxIo createSimIo(RobotVisualizer visualizer) {
    SparkMax motor = createMotor();
    ArmParameters params = new ArmParameters();
    params.armGearing = 100.0;
    params.armInertia = 10.0;
    params.armLength = 100;
    params.name = "Intake deployer";
    params.armMinAngle = Rotation2d.fromDegrees(0);
    params.armMaxAngle = Rotation2d.fromDegrees(90);
    params.reverse = true;
    ArmSimulator arm = new ArmSimulator(motor, params, visualizer.getIntakeDeploymentLigament());
    return new SimSparkMaxIo(LOGGING_NAME, motor, MotorLoggableInputs.allMetrics(), arm);
  }

  private static SparkMax createMotor() {
    SparkMax motor = new SparkMax(Constants.INTAKE_DEPLOYMENT_ID, SparkLowLevel.MotorType.kBrushless);
    SparkMaxConfig motorConfig = new SparkMaxConfig();
    motorConfig.idleMode(SparkBaseConfig.IdleMode.kBrake);
    motorConfig.smartCurrentLimit(Constants.INTAKE_DEPLOYER_CURRENT_LIMIT);
    motor.configure(
        motorConfig,
        ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);
    return motor;
  }

  public DeploymentState getDeploymentState() {
    return deploymentState;
  }

  public void setDeploymentState(DeploymentState deploymentState) {
    this.deploymentState = deploymentState;
  }

  public void toggleState() {
    if (deploymentState == DeploymentState.UP) {
      deploymentState = DeploymentState.DOWN;
    } else {
      deploymentState = DeploymentState.UP;
    } 
  }
  public boolean getFwdLimitSwitchState(){
    return io.isFwdSwitchPressed();
  }
  public boolean getRevLimitSwitchState(){
    return io.isRevSwitchPressed();
  }
}
