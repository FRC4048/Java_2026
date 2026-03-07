package frc.robot.autochooser;

import java.util.Objects;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

/**
 * Wrapper Class, that Contains a {@link frc.robot.autochooser.AutoAction} and a {@link
 * frc.robot.autochooser.FieldLocation}
 */
public class AutoEvent {
  private final AutoAction action;
  private final FieldLocation location;
  private Alliance allianceColor;
  /**
   * This instantiates the AutoEvent without the alliance color.
   * The importance of doing so is in case the alliance color
   * is not relevant to this specific combination.
   * 
   * @param action The action to do.
   * @param location The location where the robot starts.
   */
  public AutoEvent(AutoAction action, FieldLocation location) {
    this.action = action;
    this.location = location;
  }

  /**
   * This instantiates an autoEvent using an extra alliance
   * color parameter. 
   * @param action The action to do.
   * @param location 
   * @param color
   */
  public AutoEvent(AutoAction action, FieldLocation location, Alliance color) {
    this.action = action;
    this.location = location;
    this.allianceColor = color;
  }

  public AutoAction getAction() {
    return action;
  }

  public FieldLocation getLocation() {
    return location;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.action, this.location);
    /* This hashCode() implementation intentionally disregards
    the alliance color. This is so that when the commandMap
    checks whether two AutoEvent instances are the "same", the
    logic for telling whether two alliance colors are the
    "same" is delayed until the equals() method is called. */
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (obj instanceof AutoEvent other) {
      if (this.action != other.action) return false;
      if (this.location != other.location) return false;
      if (this.allianceColor == null
       || other.allianceColor == null) return true; /*
      If an allianceColor field is null, that means the instance
      was instantiated in using the first constructor, which
      means that the alliance color was intentionally chosen to
      be irrelevant. */
      return this.allianceColor == other.allianceColor;
    } 
    return false;
  }

  

}