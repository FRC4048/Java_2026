package frc.robot.autochooser;

import static edu.wpi.first.wpilibj.DriverStation.Alliance;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import frc.robot.Robot;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum FieldLocation {
  ZERO(0, 0, 0, "Zero"),
  INVALID(-1, -1, -1, "INVALID"),
  LEFT(7.150, 7.000, 180, "NON Processor Side"),
  MIDDLE(7.150, 4.500, 180, "Middle"),
  RIGHT(7.150, 2.000, 180, "Processor Side");

  private static final double RED_X_POS = 2.3876; // meters
  public static final double HEIGHT_OF_FIELD = 8.05;
  public static final double LENGTH_OF_FIELD = 17.548225;
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
    Alliance alliance = Robot.getAllianceColor().orElse(null);
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
