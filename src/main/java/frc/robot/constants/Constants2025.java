package frc.robot.constants;

public class Constants2025 extends GameConstants {
  /** Constants2025 is only for CANIDs and nothing else, everything else goes into GameConstants. */

  // Elevator
  public static final int ELEVATOR_MOTOR_ID = 35;

  public static final double MAX_ELEVATOR_ENCODER_POSITION = -46.9;

  // Coral Shooter
  public static final int SHOOTER_MOTOR_LEADER_ID = 53;
  public static final int SHOOTER_MOTOR_FOLLOWER_ID = 8;
  public static final int SHOOTER_MOTOR_ALIGNER_ID = 10;

  // Algae Roller
  public static final int ALGAE_ROLLER_CAN_ID = 2;

  // Algae Extender
  public static final int ALGAE_EXTENDER_MOTOR_ID = 3;

  // Algae Bye-Bye
  public static final int ALGAE_BYEBYE_SPINING_ID = 5;
  public static final int ALGAE_BYEBYE_TILT_ID = 1;

  // Climber
  public static final int CLIMBER_MOTOR_ID = 61; // TODO: change later

  // light strip
  public static final int LIGHTSTRIP_PORT = 0; // TODO: change later

  // Drive
  public static final int DRIVE_FRONT_RIGHT_D = 34;
  public static final int DRIVE_BACK_RIGHT_D = 31;
  public static final int DRIVE_FRONT_LEFT_D = 56;
  public static final int DRIVE_BACK_LEFT_D = 51;
  public static final int DRIVE_CANCODER_FRONT_RIGHT = 12;
  public static final int DRIVE_CANCODER_BACK_RIGHT = 13;
  public static final int DRIVE_CANCODER_FRONT_LEFT = 11;
  public static final int DRIVE_CANCODER_BACK_LEFT = 14;

  // Steer
  public static final int DRIVE_FRONT_RIGHT_S = 46;
  public static final int DRIVE_BACK_RIGHT_S = 43;
  public static final int DRIVE_FRONT_LEFT_S = 55;
  public static final int DRIVE_BACK_LEFT_S = 49;

  // Measured distance between the center of the wheels
  public static final double DRIVE_BASE_WIDTH = 0.61595;

  // Measured distance between the center of the wheels
  public static final double DRIVE_BASE_LENGTH = 0.61595;
}