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
   * @param location The location where the robot starts.
   * @param color The alliance color.
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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AutoEvent autoEvent = (AutoEvent) o;
    return action == autoEvent.action && location == autoEvent.location && allianceColor == autoEvent.allianceColor;
  }

  @Override
  public int hashCode() {
    return Objects.hash(action, location, allianceColor);
  }

  /**
   * Return an event without color (to allow lookup in the map for a color-agnostic entry)
   */
  public AutoEvent withoutColor() {
    return new AutoEvent(action, location);
  }
}
