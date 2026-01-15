// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.utils.logging.io.motor;

/**
 * Mock implementation (noop) for a SparkMax IO.
 */
public class MockSparkMaxIo implements SparkMaxIo {
    @Override
    public void periodic() {
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
        return false;
    }

    @Override
    public boolean isRevSwitchPressed() {
        return false;
    }
}
