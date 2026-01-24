// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.utils.logging.io.motor;

import com.revrobotics.spark.SparkMax;
import frc.robot.utils.logging.input.MotorLoggableInputs;
import frc.robot.utils.logging.io.BaseIoImpl;

/**
 * Mock implementation (noop) for a SparkMax IO.
 */
public class MockSparkMaxIo extends BaseIoImpl<MotorLoggableInputs> implements SparkMaxIo {
    public MockSparkMaxIo(String name, MotorLoggableInputs inputs) {
        super(name, inputs);
    }

    @Override
    public void set(double speed) {
    }

    @Override
    public void setVoltage(double voltage) {
    }

    @Override
    public void stopMotor() {
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
    protected void updateInputs(MotorLoggableInputs inputs) {
    }
}
