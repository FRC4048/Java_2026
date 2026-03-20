package frc.robot.utils.logging.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ProxyCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.ToggleShooting;
import frc.robot.commands.auto.ResetMechanisms;
import frc.robot.commands.intakeDeployment.ToggleDeployment;
import frc.robot.commands.turret.SetTurretAngle;

public class LoggableSequentialCommandGroup extends SequentialCommandGroup implements Loggable {
  private String basicName = getClass().getSimpleName();
  private Command parent = new BlankCommand();

  public <T extends Command & Loggable> LoggableSequentialCommandGroup(T... commands) {
    ProxyCommand[] proxyCommands = new ProxyCommand[commands.length];
    for (int i = 0; i < commands.length; i++) {
      commands[i].setParent(this);
      proxyCommands[i] = commands[i].asProxy();
    }
    addCommands(proxyCommands);
  }

  public LoggableSequentialCommandGroup(ResetMechanisms resetMechanisms, SetTurretAngle setTurretAngle,
        LoggableCommandWrapper wrap, LoggableCommandWrapper wrap2, ToggleShooting toggleShooting,
        LoggableParallelCommandGroup loggableParallelCommandGroup, LoggableCommandWrapper wrap3,
        ToggleDeployment toggleDeployment, ToggleShooting toggleShooting2) {
    //TODO Auto-generated constructor stub
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

  public LoggableSequentialCommandGroup withBasicName(String name) {
    this.basicName = name;
    return this;
  }
}