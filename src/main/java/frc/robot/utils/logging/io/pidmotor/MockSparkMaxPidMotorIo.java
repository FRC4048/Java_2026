// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.utils.logging.io.pidmotor;

import frc.robot.utils.logging.input.MotorLoggableInputs;
import frc.robot.utils.logging.io.motor.MockSparkMaxIo;

/**
 * Mock implementation (noop) for a SparkMax PidMotorIO.
 */
public class MockSparkMaxPidMotorIo extends MockSparkMaxIo implements SparkMaxPidMotorIo {
    public MockSparkMaxPidMotorIo(String name, MotorLoggableInputs inputs) {
        super(name, inputs);
    }

    @Override
    public void configurePID(SparkMaxPidConfig params) {
    }

    @Override
    public void setPidPosition(double position) {
    }

    @Override
    public void setPidVelocity(double velocity) {
    }

    @Override
    public void setPid(double pidP, double pidI, double pidD) {
    }

    @Override
    public void setPid(double pidP, double pidI, double pidD, double iZone, double pidFF) {
    }
}
