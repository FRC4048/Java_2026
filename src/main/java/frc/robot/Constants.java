// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.RobotBase;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

    public enum Mode {
        /**
         * Running on a real robot.
         */
        REAL,
        /**
         * Running a physics simulator.
         */
        SIM,
        /**
         * Replaying from a log file.
         */
        REPLAY
    }

    // Mode
    public static final Mode simMode = Mode.SIM;
    //  public static final Mode simMode = Mode.REPLAY;

    public static final Mode currentMode = RobotBase.isReal() ? Mode.REAL : simMode;

    public static final int NEO_CURRENT_LIMIT = 20;

    // Logging
    public static final long MAX_LOG_TIME_WAIT = 10;
    public static final boolean ENABLE_LOGGING = true;

    public static final boolean DEBUG = true;
    public static final int ROLLER_MOTOR_ID = 1;
    public static final int TILT_MOTOR_ID = 2;
    public static final int INTAKE_MOTOR_ID = 3;

    public static final double ROLLER_SPEED = 0.25;
    public static final double TILT_SPEED = -0.5; // Arm motor is inverted - use negative speed
    public static final double INTAKE_SPEED = -0.5;
    public static final double SPIN_TIMEOUT = 5;
    public static final double TILT_TIMEOUT = 5;
    public static final double TILT_LENGTH = 0.2;
    public static final Rotation2d TILT_MIN_ANGLE = Rotation2d.fromDegrees(45);
    public static final Rotation2d TILT_MAX_ANGLE = Rotation2d.fromDegrees(90);
    public static final double TILT_INERTIA = 0.5;
    public static final double TILT_GEARING = 45.0;
    public static final boolean TILT_SIMULATE_GRAVITY = false;

    public static final boolean ARM_DEBUG = true;
    public static final double DRIVE_BASE_WIDTH = 0.635;
    public static final double DRIVE_BASE_LENGTH = 0.635;
    public static final double INITIAL_ROBOT_HEIGHT = 0;
    public static final double HUB_HEIGHT = 1.83;
    public static final double SHOOTER_HEIGHT = 0.5;
    public static final double HUB_X_POSITION = 4.6256;
    public static final double HUB_Y_POSITION = 4.0345;
    public static final double DRAG_K = 1.20; // experimental value -- needs testing
    public static final double DRAG_Q = 0.055; // experimental value -- needs testing

    public static final double MAX_SPEED = Units.feetToMeters(14.5);

    public static final double DEADBAND = 0.1;
    public static final int INTAKE_DIGITAL_INPUT_CHANNEL = 0;

    public static final int DRIVE_JOYSTICK_PORT = 0;
    public static final int STEER_JOYSTICK_PORT = 1;

}

