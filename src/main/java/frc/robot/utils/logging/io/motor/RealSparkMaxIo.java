// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.utils.logging.io.motor;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import frc.robot.constants.Constants;
import frc.robot.utils.logging.input.MotorLoggableInputs;
import frc.robot.utils.logging.io.BaseIoImpl;

/**
 * IO implementation for a real SparkMax motor controller.
 */
public class RealSparkMaxIo extends BaseIoImpl<MotorLoggableInputs> implements SparkMaxIo {
    protected final SparkMax motor;

    public RealSparkMaxIo(String name, SparkMax motor, MotorLoggableInputs inputs) {
        super(name, inputs);
        this.motor = motor;
    }

    @Override
    public void set(double speed) {
        motor.set(speed);
    }

    @Override
    public void setVoltage(double voltage) {
        motor.setVoltage(voltage);
    }

    @Override
    public void stopMotor() {
        motor.stopMotor();
    }

    @Override
    public boolean isFwdSwitchPressed() {
        return getInputs().getFwdLimit();
    }

    @Override
    public boolean isRevSwitchPressed() {
        return getInputs().getRevLimit();
    }
    

    @Override
    public void setPosition(double targetRotations) {
        motor.getClosedLoopController()
             .setReference(targetRotations, ControlType.kPosition);
    }

    @Override
    protected void updateInputs(MotorLoggableInputs inputs) {
        inputs.fromHardware(motor);
    }
}
