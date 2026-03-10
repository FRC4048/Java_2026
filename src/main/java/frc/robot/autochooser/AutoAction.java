package frc.robot.autochooser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;

public enum AutoAction {
  DO_NOTHING("Do Nothing", new Pose2d(new Translation2d(5,5), new Rotation2d())),
  TWO_PIECE_HIGH("2 Piece L4", new Pose2d(new Translation2d(-5,-5), new Rotation2d())),
  TWO_PIECE_LOW("2 Piece L2", new Pose2d(new Translation2d(9,9), new Rotation2d())),
  ONE_PIECE("1 Piece", new Pose2d(new Translation2d(-9,-9), new Rotation2d())),
  CROSS_THE_LINE("Cross The Line", new Pose2d(new Translation2d(7,7), new Rotation2d())),
  INVALID("INVALID", new Pose2d(new Translation2d(), new Rotation2d()));
  DO_NOTHING("Do Nothing"),
  SHOOT("Shoot"),
  SHOOT_AND_CLIMB("Shoot and Climb"),
  INVALID("INVALID");
  private final String name;
  private final Pose2d startingPose;
  private static final HashMap<String, AutoAction> nameMap =
      new HashMap<>(
          Arrays.stream(AutoAction.values())
              .collect(Collectors.toMap(AutoAction::getName, Function.identity())));

  AutoAction(String name, Pose2d startingPose) {
    this.name = name;
    this.startingPose = startingPose;
  }

  public String getName() {
    return name;
  }
  public Pose2d getPose(){
    return startingPose;
  }
  @Override
  public String toString() {
    return getName();
  }

  public static AutoAction fromName(String name) {
    return nameMap.get(name);
  }
}
