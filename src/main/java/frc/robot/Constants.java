// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants extends GameConstants{
    
    public static final int ROLLER_MOTOR_ID = 1;
    public static final int TILT_MOTOR_ID = 2;
    public static final int INTAKE_MOTOR_ID = 3;
    
    public static final double DRIVE_BASE_WIDTH = 0.635;
    public static final double DRIVE_BASE_LENGTH = 0.635;
    public static final double INITIAL_ROBOT_HEIGHT = 0;

    public static final int INTAKE_DIGITAL_INPUT_CHANNEL = 0;

}

