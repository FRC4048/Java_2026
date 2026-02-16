package frc.robot.commands;

import frc.robot.utils.logging.commands.LoggableCommand;

/**
 * A command that prints a string when initialized.
 *
 * <p>This class is provided by the NewCommands VendorDep
 */
public class PrintCommand extends LoggableCommand {
  /**
   * Creates a new a PrintCommand.
   *
   * @param message the message to print
   */
  public PrintCommand(String message) {
    System.out.println(message);
  }

  @Override
  public boolean runsWhenDisabled() {
    return true;
  }
}
