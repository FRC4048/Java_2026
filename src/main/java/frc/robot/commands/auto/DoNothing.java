package frc.robot.commands.auto;

import frc.robot.utils.logging.commands.LoggableCommand;

public class DoNothing extends LoggableCommand{
    public DoNothing() {
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}