package frc.robot.utils.logging.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ProxyCommand;

public class LoggableParallelCommandGroup extends ParallelCommandGroup implements Loggable {
  private String basicName = getClass().getSimpleName();
  private Command parent = new BlankCommand();

  @SafeVarargs
  public <T extends Command & Loggable> LoggableParallelCommandGroup(T... commands) {
    ProxyCommand[] proxyCommands = new ProxyCommand[commands.length];
    for (int i = 0; i < commands.length; i++) {
      commands[i].setParent(this);
      proxyCommands[i] = commands[i].asProxy();
    }
    addCommands(proxyCommands);
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

  public LoggableParallelCommandGroup withBasicName(String name) {
    this.basicName = name;
    return this;
  }
}
