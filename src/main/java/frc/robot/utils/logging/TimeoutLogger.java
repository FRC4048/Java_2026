package frc.robot.utils.logging;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj.DriverStation;

/** Keeps track of the amount of timeouts from each command*/
public class TimeoutLogger {
  private int timeoutCounter = 0; // per command
  private static int totalTimeouts = 0; // every timeout
  private final String commandName;

  public TimeoutLogger(String commandName) {
    this.commandName = commandName;
    Logger.recordOutput("Timeouts/" + commandName, timeoutCounter);
  }

  public double getTimeoutCount() {
    return timeoutCounter;
  }

  public void increaseTimeoutCount() {
    timeoutCounter++;
    totalTimeouts++;
    DriverStation.reportWarning("Command" + commandName + "has timeout out this is due to a limit switch not working and that is BAD", false);
    Logger.recordOutput("Timeouts/" + commandName, timeoutCounter);
  }

  public String getCommandName() {
    return commandName;
  }

  public void resetCounter() {
    timeoutCounter = 0;
    Logger.recordOutput("Timeouts/" + commandName, timeoutCounter);
  }

  public static int getTotalTimeouts() {
    return totalTimeouts;
  }
}
