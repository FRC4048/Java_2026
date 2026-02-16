package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;

/**
 * A command that prints a string when initialized.
 *
 * <p>This class is provided by the NewCommands VendorDep
 */
public class PrintCommand extends InstantCommand {
  /**
   * Creates a new a PrintCommand.
   *
   * @param message the message to print
   */
  public PrintCommand(String message) {
    super(() -> System.out.println(message));
  }

  @Override
  public boolean runsWhenDisabled() {
    return true;
  }
}
