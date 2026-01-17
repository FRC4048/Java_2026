package frc.robot.constants;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.RobotBase;

public class GameConstants {

  // Controllers
  public static final int LEFT_JOYSTICK_ID = 0;
  public static final int RIGHT_JOYSTICK_ID = 1;
  public static final int XBOX_CONTROLLER_ID = 2;

  // Diags
  public static final double BYEBYE_ROLLER_DIAGS_ENCODER = 1;
  public static final double HIHI_ROLLER_DIAGS_ENCODER = 0.3;
  public static final double CORAL_ALIGNER_DIAGS_ENCODER = 0.3;
  public static final double ELEVATOR_DIAGS_ENCODER = 1;
  public static final double HIHI_EXTENDER_DIAGS_ENCODER = 1;
  public static final double CORAL_DIAGS_ENCODER = 1;
  public static final double GYRO_DIAGS_ANGLE = 30;

  // Debug
  public static final boolean SWERVE_DEBUG = false;
  public static final boolean INTAKE_DEBUG = false;
  public static final boolean CLIMBER_DEBUG = false;
  public static final boolean ELEVATOR_DEBUG = false;
  public static final boolean CORAL_DEBUG = false;
  public static final boolean HIHI_DEBUG = false;
  public static final boolean BYEBYE_DEBUG = false;
  public static final boolean COMMAND_DEBUG = false;
  public static final boolean INPUTS_DEBUG = false;
  public static final boolean ARM_DEBUG = false;
  public static final boolean TUNING_MODE = false;

  // Speeds
  public static final double MAX_AUTO_ALIGN_SPEED = 0.9;
  public static final double ELEVATOR_RISE_SPEED = 0.66;
  public static final double CLIMBER_PHASE1_SPEED = 0.3; // TODO: change later
  public static final double CLIMBER_PHASE2_SPEED = 0.3; // TODO: change later
  public static final double ELEVATOR_LOWER_SPEED = -0.5;
  public static final int ALGAE_EXTENDER_MOTOR_SPEED = 4; // TODO: change later
  public static final double BYEBYE_ROLLER_SPEED = 0.5;
  public static final double TILT_SPEED = 0.15;
  public static final double BYEBYE_FORWARD_SPEED = 0.5; // TODO: change later
  public static final double BYEBYE_REVERSE_SPEED = -0.7; // TODO: change later
  public static final double INTAKE_MOTOR_SPEED = 0.25;
  public static final double INTAKE_ALIGNER_SPEED = 0.5;
  public static final double INTAKE_TILT_VELOCITY = 0.5;
  public static final double CORAL_SHOOTER_SPEED = 0.5;
  public static final double HIHI_EXTEND_SPEED = 0.4;
  public static final double HIHI_RETRACT_SPEED = -0.15;
  public static final double HIHI_INTAKE_SPEED = 0.7;
  public static final double HIHI_SHOOT_SPEED = -0.9;
  public static final double CLIMBER_SPEED = 0.5;
  public static final double CLIMBER_RISE_SPEED = 0.5;
  public static final double HIHI_INTAKE_BASE_VELOCITY = 7000.0;

  // Accelerations
  public static final double MAX_PATHPLANNER_ACCEL = 11.7;
  public static final double MAX_PATHPLANNER_ANGULAR_ACCEL = 3797;

  // Timeouts
  public static final int AUTO_ALIGN_TIMEOUT = 10;
  public static final int SERVER_SOCKET_CONNECTION_TIMEOUT = 2000;
  public static final int ELEVATOR_TIMEOUT = 10;
  public static final int ELEVATOR_RESET_TIMEOUT = 10;
  public static final int BYEBYE_SPIN_ROLLER_TIMEOUT = 10;
  public static final int ELEVATOR_TO_POSITION_TIMEOUT = 10; // TODO: change later
  public static final int BYEBYE_FORWARD_TIMEOUT = 10; // TODO: change later
  public static final int BYEBYE_REVERSE_TIMEOUT = 10; // TODO: change later
  public static final double SHOOT_CORAL_TIMEOUT = 0.75;
  public static final int CORAL_FWR_TIMEOUT = 10;
  public static final int ROLL_ALGAE_TIMEOUT = 10;
  public static final double HIHI_RETRACT_TIMEOUT = 10;
  public static final double HIHI_ROLLER_OUT_TIMEOUT = 5;
  public static final double HIHI_ROLLER_IN_TIMEOUT = 5;
  public static final int INTAKE_CORAL_TIMEOUT = 10;
  public static final int CLIMBER_PHASE2_TIMEOUT = 10; // TODO: change later
  public static final int RESET_CLIMBER_TIMEOUT = 10;
  public static final double HIHI_INTAKE_TIMEOUT = 10; // TODO: Change Later
  public static final double INTAKE_LED_STRIP_TIME = 1;

  // Logging
  public static final long MAX_LOG_TIME_WAIT = 10;
  public static final boolean ENABLE_LOGGING = true;

  // Treshholds
  public static final double VISION_CONSISTANCY_THRESHOLD = 0.25;
  public static final double AUTO_ALIGN_THRESHOLD = 2.3; // degrees //TODO: change later
  public static final double ELEVATOR_ENCODER_THRESHHOLD = 1; // TODO: Change Later
  public static final int HIHI_EXTENDER_TICK_LIMIT = 10;

  // Mode
  public static final Mode simMode = Mode.SIM;
  public static final Mode currentMode = RobotBase.isReal() ? Mode.REAL : simMode;

  public enum Mode {
    /** Running on a real robot. */
    REAL,

    /** Running a physics simulator. */
    SIM,
    /** Replaying from a log file. */
    REPLAY
  }

  // Limits
  public static final int DRIVE_SMART_LIMIT = 38; // TODO: change later
  public static final int DRIVE_SECONDARY_LIMIT = 48; // TODO: change later
  public static final double DRIVE_RAMP_RATE_LIMIT = 0.1; // TODO: change later
  public static final int NEO_CURRENT_LIMIT = 20;
  public static final int HIHI_CURRENT_LIMIT = 10;

  // HiHi PID
  public static final double HIHI_PID_P = 0.02; // 0.08 with max motion
  public static final double HIHI_PID_I = 0;
  public static final double HIHI_PID_D = 0;
  public static final double HIHI_PID_I_ZONE = 0;
  public static final double HIHI_PID_FF = 0;
  public static final double HIHI_PID_MAX_VEL = 3000;
  public static final double HIHI_PID_MAX_ACC = 2500;
  public static final double HIHI_PID_ALLOWED_ERROR = 0.5;
  public static final boolean HIHI_USE_MAX_MOTION = false;

  // Elevator PID
  public static final double ELEVATOR_PID_P = 0.03;
  public static final double ELEVATOR_PID_I = 0;
  public static final double ELEVATOR_PID_D = 0;
  public static final double ELEVATOR_PID_FF = 0.001;
  public static final double ELEVATOR_PID_IZONE = 0;
  public static final double ELEVATOR_PID_MAX_VELOCITY = 3000;
  public static final double ELEVATOR_PID_MAX_ACCELERATION = 30000;
  public static final boolean ELEVATOR_USE_MAX_MOTION = true;

  // Drive PID
  public static final double DRIVE_PID_P = 2; // TODO: change later
  public static final double DRIVE_PID_I = 0; // TODO: change later
  public static final double DRIVE_PID_D = 0; // TODO: change later
  public static final double DRIVE_PID_FF_S = 0.19;
  public static final double DRIVE_PID_FF_V = 3.3;
  public static final double DRIVE_PID_I_ZONE = 0; // TODO: change later
  public static final double DRIVE_PID_ALLOWED_ERROR = 0;

  // Steer PID
  public static final double STEER_PID_P = 0.3; // TODO: change later
  public static final double STEER_PID_I = 0; // TODO: change later
  public static final double STEER_PID_D = 0.005; // TODO: change later
  public static final double STEER_PID_FF_S = 0; // 0.2; //TODO: change later
  public static final double STEER_PID_FF_V = 0; // 0.8; //TODO: change later

  // pathplanner SLOW ROBOT PID CHANGE FOR COMPETITION
  public static final double PATH_PLANNER_TRANSLATION_PID_P = 5;
  public static final double PATH_PLANNER_TRANSLATION_PID_I = 0;
  public static final double PATH_PLANNER_TRANSLATION_PID_D = 0;
  public static final double PATH_PLANNER_ROTATION_PID_P = 1;
  public static final double PATH_PLANNER_ROTATION_PID_I = 0;
  public static final double PATH_PLANNER_ROTATION_PID_D = 0;

  // Lengths
  public static final double ELEVATOR_DRUM_RADIUS =
      Units.inchesToMeters(1); // In M(in), change later
  public static final double MIN_ELEVATOR_HEIGHT_METERS = 0; // in m
  public static final double MAX_ELEVATOR_HEIGHT_METERS = 1; // in m
  public static final double INITIAL_ELEVATOR_HEIGHT = 0; // TODO: change later
  public static final double HIHI_LENGTH = 0.2; // TODO: change later
  public static final double BYEBYE_TILT_LENGTH = 0.2; // TODO: change later
  public static final double INITIAL_CLIMBER_HEIGHT = 0.2;
  public static final double CLIMBER_BASE_LENGTH = 0.2;
  public static final double CLIMBER_ARM_LENGTH = 0.2;

  // Angles
  public static final Rotation2d HIHI_MIN_ANGLE = Rotation2d.fromDegrees(0);
  public static final Rotation2d HIHI_MAX_ANGLE = Rotation2d.fromDegrees(90);
  public static final Rotation2d BYEBYE_TILT_MIN_ANGLE =
      Rotation2d.fromDegrees(45); // TODO: change later
  public static final Rotation2d BYEBYE_TILT_MAX_ANGLE =
      Rotation2d.fromDegrees(90); // TODO: change later
  public static final double BYEBYE_TILT_INERTIA = 0.5; // TODO: change later
  public static final double BYEBYE_TILT_GEARING = 45.0; // TODO: change later
  public static final boolean BYEBYE_TILT_SIMULATE_GRAVITY = true;
  public static final double HIHI_EXTEND_POSITION = 15.5; // TODO: change later
  public static final double HIHI_RETRACT_POSITION = 0.0; // TODO: change later

  // Zeros
  public static final double BACK_RIGHT_ABS_ENCODER_ZERO = 0.306885; // TODO: change later
  public static final double FRONT_LEFT_ABS_ENCODER_ZERO = -0.059082; // TODO: change later
  public static final double BACK_LEFT_ABS_ENCODER_ZERO = 0.379150; // TODO: change later
  public static final double FRONT_RIGHT_ABS_ENCODER_ZERO = -0.100586; // TODO: change later

  // Climber
  public static final double CLIMBER_PHASE1_POSITION = 150.0;
  public static final double CLIMBER_DEADBAND = 0.4;
  public static final double CLIMBER_DEPLOY_HARPOON_TIMEOUT = 10;

  // Drivetrain
  public static final double WHEEL_RADIUS = 0.0508; // TODO: change later
  public static final double MAX_VELOCITY = 1.5; // 4 meters per second //TODO: change later
  public static final double MAX_ANGULAR_SPEED = 6 * Math.PI; // TODO: change later

  // Other
  public static final double GRAVITY = -9.81;
  public static final long GYRO_THREAD_RATE_MS = 10;
  public static final int SERVER_SOCKET_ATTEMPT_DELAY = 100;
  public static final int TCP_SERVER_PORT = 5806;
  public static final boolean ENABLE_VISION = true;
  public static final long POSE_BUFFER_STORAGE_TIME = 2;
  public static final double ELEVATOR_GEARING = 10; // TODO: change later
  public static final double CARRIAGE_MASS = 25.4; // In Kg, change later
//   public static final SwerveModuleProfileV2 SWERVE_MODULE_PROFILE =
//       SwerveModuleProfileV2.MK4I; // TODO: Uncomment AND change later

  public static final double HIHI_GEARING = 15.0; // TODO: change later
  public static final double HIHI_INERTIA = 0.01; // TODO: change later
  public static final boolean HI_HI_SIMULATE_GRAVITY = false;
  public static final int MAX_VALID_TICKS_INTAKE = 15; // TODO: Change Later
  public static final int MAX_VALID_TICKS_ELEVATOR = 10; // TODO: Change Later
  public static final double ALIGNMENT_DISTANCE_THRESHOLD = 0.005; // TODO: change later

  public static final double ROBOT_MASS = 58.967; // In Kg, change later
  public static final double ROBOT_BUMPER_WIDTH = 0.914;
  public static final double ROBOT_BUMPER_LENGTH = 0.914;
  public static final double STEER_ROTATIONAL_INERTIA = 0.03; // TODO: change later
  public static final double COEFFICIENT_OF_FRICTION = 1.542;
  public static final double STEER_FRICTION_VOLTAGE = 0.2;

  // ELEVATOR CONSTANTS
  public static final double ELEVATOR_MANUAL_DEADBAND = 0.2;
  public static final double ELEVATOR_MANUAL_MAX_SPEED_UP = -.3;
  public static final double ELEVATOR_MANUAL_MAX_SPEED_DOWN = .15;

  // Limelight
  public static final Transform3d LIMELIGHT_TO_ROBOT =
      new Transform3d(
          0.3429, -0.0635, 0, new Rotation3d(0.0, (-47 * Math.PI) / 180, 0.0)); // z = 0.720725
  public static final String LIMELIGHT_IP_ADDRESS = "10.40.48.104"; // TODO Change Later
  public static final double MINIMUM_PIECE_DETECTION_DOT = 0;
  public static final boolean ENABLE_FANCY_LIMELIGHT_MATH = true;
}