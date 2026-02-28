package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.Command;

public class CommandDescription extends Command {
    String desc;
    public CommandDescription(String desc) {
        this.desc = desc;
    }
    @Override
    public String getName() {
        return desc;
    }
}
