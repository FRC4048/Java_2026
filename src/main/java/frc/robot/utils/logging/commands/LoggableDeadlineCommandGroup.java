package frc.robot.utils.logging.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.ProxyCommand;

public class LoggableDeadlineCommandGroup extends ParallelDeadlineGroup implements Loggable {
  private String basicName = getClass().getSimpleName();
  private Command parent = new BlankCommand();

  public LoggableDeadlineCommandGroup(Command deadline, Command... others) {
    super(new Command() {});
    ProxyCommand[] proxyCommands = new ProxyCommand[others.length];
    for (int i = 0; i < others.length; i++) {
      Command command = others[i];
      if (command instanceof Loggable) {
        ((Loggable) command).setParent(this);
        proxyCommands[i] = command.asProxy();
      } else {
        LoggableCommandWrapper wrapper = LoggableCommandWrapper.wrap(command);
        wrapper.setParent(this);
        proxyCommands[i] = wrapper.asProxy();
      }
    }
    addCommands(proxyCommands);
    Command deadlineCommand = deadline;
    if (deadlineCommand instanceof Loggable) {
      ((Loggable) deadlineCommand).setParent(this);
      setDeadline(deadlineCommand.asProxy());
    } else {
      LoggableCommandWrapper wrapper = LoggableCommandWrapper.wrap(deadlineCommand);
      wrapper.setParent(this);
      setDeadline(wrapper.asProxy());
    }
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

  public LoggableDeadlineCommandGroup withBasicName(String name) {
    basicName = name;
    return this;
  }
}
