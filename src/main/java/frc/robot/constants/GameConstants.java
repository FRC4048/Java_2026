package frc.robot.constants;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.RobotBase;
import swervelib.telemetry.SwerveDriveTelemetry.TelemetryVerbosity;

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

    //Joystick
    public static final int DRIVE_JOYSTICK_PORT = 0;
    public static final int STEER_JOYSTICK_PORT = 1;
    public static final int XBOX_CONTROLLER_PORT = 2;

    //Speeds
    public static final double ROLLER_SPEED = 0.25;
    public static final double TILT_SPEED = -0.5; // Arm motor is inverted - use negative speed
    public static final double INTAKE_SPEED = -0.5;
    public static final double HOPPER_SPEED = 0.35;//Want to increase this later
    public static final double CLIMBER_SPEED_UP = 0.1;
    public static final double CLIMBER_SPEED_DOWN = -0.1;
    public static final double FEEDER_SPEED = 0.5;//Might make veolcity PID
    public static final double MAX_SPEED = Units.feetToMeters(14.5);
    public static final double SHOOTER_SPEED = 1500;
    public static final double INTAKE_DEPLOYER_SPEED = -0.075;
    public static final double INTAKE_RETRACTION_SPEED = 0.1;
    public static final double INITIAL_INTAKE_DEPLOYMENT_SPEED = -0.1;
    public static final double INITIAL_INTAKE_RETRACTION_SPEED = 0.5;


    //Diags
    public static final double HOPPER_DIAGS_ENCODER = 1;
    public static final double INTAKE_ROLLER_DIAGS_ENCODER = 1;
    public static final double FEEDER_DIAGS_ENCODER = 1;
    public static final double CLIMBER_DIAGS_ENCODER = 1;
    public static final double SHOOTER_DIAGS_ENCODER = 1;
    public static final double GYRO_DIAGS_ANGLE = 30;
    public static final double TURRET_DIAGS_ENCODER = 1;
    public static final double INTAKE_DEPLOYER_DIAGS_ENCODER = 1;
    public static final double ANGLER_DIAGS_ENCODER = 1;

    //Timeouts
    public static final double SPIN_TIMEOUT = 5;
    public static final double TILT_TIMEOUT = 5;
    public static final double HOPPER_TIMEOUT = 60;
    public static final double CLIMBER_TIMEOUT = 10;
    public static final double FEEDER_TIMEOUT = 60;
    public static final double ANGLER_TIMEOUT = 60;
    public static final int SERVER_SOCKET_CONNECTION_TIMEOUT = 2000;
    public static final double SHOOTER_TIMEOUT = 5;
    public static final double INTAKE_DEPLOYER_BURNOUT_TIMER = 5;
    public static final double TURRET_TIMEOUT = 5;

    //Angles
    public static final Rotation2d TILT_MIN_ANGLE = Rotation2d.fromDegrees(45);
    public static final Rotation2d TILT_MAX_ANGLE = Rotation2d.fromDegrees(90);
    public static final Rotation2d ANGLER_MIN_ANGLE = Rotation2d.fromDegrees(45);
    public static final Rotation2d ANGLER_MAX_ANGLE = Rotation2d.fromDegrees(90);

    public static final double TILT_LENGTH = 0.2;
    public static final double TILT_INERTIA = 0.5;
    public static final double TILT_GEARING = 45.0;
    public static final boolean TILT_SIMULATE_GRAVITY = false;
    public static final double ANGLER_LENGTH = 0.2;
    public static final double ANGLER_INERTIA = 0.5;
    public static final double ANGLER_GEARING = 45.0;
    public static final boolean ANGLER_SIMULATE_GRAVITY = false;
    public static final int NEO_CURRENT_LIMIT = 20;
    public static final double TURRET_LENGTH = 0.4;
    public static final double TURRET_INERTIA = 0.5;
    public static final double TURRET_GEARING = 45.0;

    // angler (turret) PID
    public static final double ANGLER_P = 0.7;
    public static final double ANGLER_I = 0.000001;
    public static final double ANGLER_D = 0.0;
    public static final double ANGLER_FF = 0.0;
    public static final double ANGLER_HOME_ROTATIONS = 0.0;
    public static final double ANGLER_ENCODER_LOW = 0; //Lowest encoder position of Angler
    public static final double ANGLER_ENCODER_HIGH = 100; //Highest encoder position of Angler
    public static final double ANGLER_ANGLE_LOW = 17; //Lowest angle position of Angler
    public static final double ANGLER_ANGLE_HIGH = 45; //Highest angle position of Angler
    public static final double ANGLER_FIXED_ROTATIONS = 0.1; //Fixed encoder position of Angler in Fixed ShootState
    public static final double ANGLER_FIXED_ANGLE = 10; //Fixed encoder position of Angler in Fixed ShootState
    public static final double ANGLER_LIMIT_SPEED = 0.2;


 // turret (pan angle) PID
    public static final double TURRET_P = 1;
    public static final double TURRET_I = 0.000000;
    public static final double TURRET_D = 0.0;
    public static final double TURRET_FF = 0.0;
    public static final double TURRET_ENCODER_MIN = 0; //Lowest encoder position of Turret
    public static final double TURRET_ENCODER_MAX = 77; //Highest encoder position of Turret
    public static final double TURRET_HOME_ANGLE = 0.0; //Turret facing forward
    public static final double TURRET_MIN_ANGLE = -97;
    public static final double TURRET_MAX_ANGLE = 97;
    public static final double TURRET_LIMIT_SPEED = 0.2;

    //swerve config
    public static final TelemetryVerbosity TELEMENTRY_VERBOSITY = TelemetryVerbosity.HIGH;
    public static final boolean SET_HEADING_CORRECTION = false;
    public static final boolean COSIN_COMPENSATOR = false;
    public static final boolean USE_ANGULAR_VELOCITY_COMPENSATION_IN_TELEOP = true;
    public static final boolean USE_ANGULAR_VELOCITY_COMPENSATION_IN_AUTO = true;
    public static final double ANGULAR_VELOCITY_COEFFICENT = 0.1;
    public static final boolean SET_MODULE_ENCODER_AUTO_SYNCHRONIZE = false;
    public static final double SET_MODULE_ENCODER_AUTO_SYNCHRONIZE_DEADBAND = 1;

    // turret pan angle and launch angle calculations constants
    public static final double GRAVITY = 9.81;
    public static final double HUB_HEIGHT = 1.83;
    public static final double SHOOTER_HEIGHT = 0.5;
    public static final double BLUE_HUB_X_POSITION = 4.6256;
    public static final double BLUE_HUB_Y_POSITION = 4.0345;
    public static final double RED_HUB_X_POSITION = 11.9154;
    public static final double RED_HUB_Y_POSITION = 4.0345;
    public static final double X_DISTANCE_BETWEEN_ROBOT_AND_TURRET = .4; // needs value from hardware
    public static final double Y_DISTANCE_BETWEEN_ROBOT_AND_TURRET = .4; // needs value from hardware

    // Shift timings
    public static final int SHIFT_1_START = 130;
    public static final int SHIFT_2_START = 105;
    public static final int SHIFT_3_START = 80;
    public static final int SHIFT_4_START = 55;
    public static final int ENDGAME_START = 30;

    public static final double VISION_CONSISTENCY_THRESHOLD = 0.25; //How close 2 vision measurements have to be (needs to be tuned potentially but seemingly from my testing it also might not be needed)
    public static final boolean ENABLE_VISION = true;
    public static final double POSE_BUFFER_STORAGE_TIME = 2; //how many past measurements are stored in the buffer (might increase if we need further back)
    public static final String DRIVER_CAM_IP_ADDRESS = "10.40.48.2:1181/?action=stream";
    public static final double FIELD_LENGTH = 16.5; //TODO: Change Later
    public static final double FIELD_WIDTH = 8.1; //TODO: Change Later
    // Vision
    public static final Transform3d ROBOT_TO_CAMERA = new Transform3d(0,0,0, new Rotation3d(0,0,0)); // TODO: change
    public static final double HORIZONTAL_FOV = Units.degreesToRadians(110); // radians; TODO: Change Later
    public static final double VERTICAL_FOV = Units.degreesToRadians(90); // radians; TODO: Change Later
    public static final double AVERAGE_CAM_LATENCY = 0; // seconds; TODO: change Later
    public static final double AVERAGE_CAM_LATENCY_STD_DEV = 0; // seconds; TODO: change Later
    public static final double MAX_VISION_DISTANCE_SIMULATION = 6;
}
