package frc.robot.autochooser;

/**
 * Wrapper Class, that Contains a {@link frc.robot.autochooser.AutoAction} and a {@link
 * frc.robot.autochooser.FieldLocation}
 */
public class AutoEvent {
  private final AutoAction action;
  private final FieldLocation location;

  public AutoEvent(AutoAction action, FieldLocation location) {
    this.action = action;
    this.location = location;
  }

  public AutoAction getAction() {
    return action;
  }

  public FieldLocation getLocation() {
    return location;
  }

}
