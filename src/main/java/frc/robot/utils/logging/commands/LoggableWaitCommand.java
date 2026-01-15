package frc.robot.utils.logging.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.WaitCommand;

import java.util.Objects;

public class LoggableWaitCommand extends WaitCommand implements Loggable {
  private String basicName = getClass().getSimpleName();
  private Command parent = new BlankCommand();

  public LoggableWaitCommand(double seconds) {
    super(seconds);
  }

  @Override
  public String getBasicName() {
    return basicName;
  }

  @Override
  public String toString() {
    String prefix = parent.toString();
    if (!prefix.isBlank()) {
      prefix = prefix.substring(0, prefix.length() - 5);
      prefix += "/";
    }
    return prefix + getBasicName() + "/inst";
  }

  @Override
  public void setParent(Command loggable) {
    this.parent = loggable == null ? new BlankCommand() : loggable;
  }

  public LoggableWaitCommand withBasicName(String name) {
    this.basicName = name;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    LoggableWaitCommand that = (LoggableWaitCommand) o;
    return Objects.equals(basicName, that.basicName) && Objects.equals(parent, that.parent);
  }

  @Override
  public int hashCode() {
    return Objects.hash(basicName, parent);
  }
}
