package frc.robot.commands.auto;

import frc.robot.utils.logging.commands.LoggableCommand;

public class CommandDescription extends LoggableCommand {
    String desc;
    public CommandDescription(String desc) {
        this.desc = desc;
    }
    @Override
    public String getName() {
        return desc;
    }
}
