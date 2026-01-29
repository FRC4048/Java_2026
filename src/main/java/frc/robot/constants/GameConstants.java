package frc.robot.constants;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.RobotBase;

public class GameConstants {
    
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

    public static final double DEADBAND = 0.1;

    // Mode
    public static final Mode simMode = Mode.SIM;
    //  public static final Mode simMode = Mode.REPLAY;
    public static final Mode currentMode = RobotBase.isReal() ? Mode.REAL : simMode;

    // Logging
    public static final long MAX_LOG_TIME_WAIT = 10;
    public static final boolean ENABLE_LOGGING = true;

    //Debugs
    public static final boolean DEBUG = true;
    public static final boolean ARM_DEBUG = true;

    //Speeds
    public static final double ROLLER_SPEED = 0.25;
    public static final double TILT_SPEED = -0.5; // Arm motor is inverted - use negative speed
    public static final double INTAKE_SPEED = -0.5;
    public static final double MAX_SPEED = Units.feetToMeters(14.5);

    //Timeouts
    public static final double SPIN_TIMEOUT = 5;
    public static final double TILT_TIMEOUT = 5;

    //Angles
    public static final Rotation2d TILT_MIN_ANGLE = Rotation2d.fromDegrees(45);
    public static final Rotation2d TILT_MAX_ANGLE = Rotation2d.fromDegrees(90);

    public static final double TILT_LENGTH = 0.2;
    public static final double TILT_INERTIA = 0.5;
    public static final double TILT_GEARING = 45.0;
    public static final boolean TILT_SIMULATE_GRAVITY = false;
    public static final int NEO_CURRENT_LIMIT = 20;

}
