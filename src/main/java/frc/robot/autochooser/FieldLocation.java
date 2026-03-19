package frc.robot.autochooser;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum FieldLocation {
  ZERO(0, 0, 0, "ZERO"),
  INVALID(-1, -1, -1, "INVALID"),
  DEPOT_SIDE(3.599, 6.57, 180, "Depot side - Driver Station LEFT"), 
  MID(3.599, 4.029, 180, "Middle"),
  OUTPOST_SIDE(3.599, 1.43, 180, "Outpost side - Driver Station RIGHT");

  private static final double RED_X_POS = 9.338; // meters
  public static final double HEIGHT_OF_FIELD = 8.043;
  public static final double LENGTH_OF_FIELD = 16.411;
  private final double yPose;
  private final double xPose;
  private final double angle;
  private final String name;
  private static final HashMap<String, FieldLocation> nameMap =
      new HashMap<>(
          Arrays.stream(FieldLocation.values())
              .collect(Collectors.toMap(FieldLocation::getShuffleboardName, Function.identity())));

  FieldLocation(double xPos, double yPose, double angle, String name) {
    this.xPose = xPos;
    this.yPose = yPose;
    this.angle = angle;
    this.name = name;
  }

  public static FieldLocation fromName(String string) {
    return nameMap.get(string);
  }

  public Pose2d getLocation() {
    Alliance alliance = Robot.allianceColor().orElse(null);
    if (alliance == null) {
      return new Pose2d(INVALID.xPose, INVALID.yPose, Rotation2d.fromDegrees(INVALID.angle));
    }
    double x = (alliance == Alliance.Red) ? xPose + RED_X_POS + Units.inchesToMeters(36) : xPose;
    double y = (alliance == Alliance.Red) ? yPose - 2 * (yPose - (HEIGHT_OF_FIELD / 2)) : yPose;
    double radian =
        (alliance == Alliance.Red) ? Math.toRadians(180 - angle) : Math.toRadians(angle);
    return new Pose2d(x, y, Rotation2d.fromRadians(radian));
  }

  public String getShuffleboardName() {
    return name;
  }
}