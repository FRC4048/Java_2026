package frc.robot.commands.auto;

import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;

public class DoNothing extends LoggableSequentialCommandGroup{
    public DoNothing() {
        super(
                new DoNothing()
        );
    }
}