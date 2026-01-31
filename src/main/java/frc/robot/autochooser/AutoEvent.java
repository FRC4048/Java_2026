package frc.robot.autochooser;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AutoEvent autoEvent = (AutoEvent) o;
    return action.equals(autoEvent.action) && location.equals(autoEvent.location);
  }

  @Override
  public int hashCode() {
    return Objects.hash(action, location);
  }

  @Override
  public String toString() {
    return "AutoEvent[action = " + action.toString() + ", location = " + location.toString() + "]";
  }

  public static AutoEvent fromString(String name) {
    // Regex to capture the values inside 'action = ' and 'location = '
    Pattern pattern = Pattern.compile("AutoEvent\\[action = (.*), location = (.*)\\]");
    Matcher matcher = pattern.matcher(name);

    if (matcher.find()) {
        AutoAction action = AutoAction.fromName(matcher.group(1));
        FieldLocation location = FieldLocation.fromName(matcher.group(2));
        return new AutoEvent(action, location);
    }
    throw new IllegalArgumentException("Invalid format");
  }
}
