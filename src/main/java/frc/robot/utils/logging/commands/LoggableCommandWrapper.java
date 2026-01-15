package frc.robot.utils.logging.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

import java.util.Set;

public class LoggableCommandWrapper extends LoggableCommand {
  private final Command wrap;

  public LoggableCommandWrapper(Command toWrap) {
    this.wrap = toWrap;
  }

  public static LoggableCommandWrapper wrap(Command toWrap) {
    return new LoggableCommandWrapper(toWrap);
  }

  @Override
  public void initialize() {
    wrap.initialize();
  }

  @Override
  public void execute() {
    wrap.execute();
  }

  @Override
  public void end(boolean interrupted) {
    wrap.end(interrupted);
  }

  @Override
  public boolean isFinished() {
    return wrap.isFinished();
  }

  @Override
  public Set<Subsystem> getRequirements() {
    return wrap.getRequirements();
  }

  @Override
  public InterruptionBehavior getInterruptionBehavior() {
    return wrap.getInterruptionBehavior();
  }

  @Override
  public boolean runsWhenDisabled() {
    return wrap.runsWhenDisabled();
  }
}
