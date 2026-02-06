package frc.robot.autochooser;

import frc.robot.utils.logging.commands.LoggableCommand;
import frc.robot.utils.logging.commands.DoNothingCommand;
import frc.robot.utils.logging.commands.DoSomethingCommand;

/** An enum to associate commands with human-readable descriptions. */
public enum AutoCommand {

    // Add commands here. Importantly, you should give each command
    // a readable description so that the drive team can tell what
    // the robot will actually do. This will be used to give the
    // drive team visual feedback on the elastic dashboard when
    // selecting an autonoumous command.
    Invalid(
        "The selection is invalid. (The robot won't do anything.)",
        new DoNothingCommand()
    ),
    DoNothing("The robot won't do anything.", new DoNothingCommand()),
    DoSomething(
        "Something will be printed to the terminal.",
        new DoSomethingCommand("""
            SUCCESSFULLY

            DID

            SOMETHING
            """)
    );

    private String description;
    private LoggableCommand command;

    AutoCommand(String description, LoggableCommand command) {
        this.description = description;
        this.command = command;
    }

    public String getDescription() {
        return description;
    }

    public LoggableCommand getCommand() {
        return command;
    }

}
