package frc.robot.autochooser;

import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;

import frc.robot.utils.logging.commands.LoggableCommand;

public class AutoChooser extends LoggedDashboardChooser<LoggableCommand> {
    
    public AutoChooser(String key) {
        super(key);
    }

    public void addDefaultOption(AutoEvent key, LoggableCommand value) {
        super.addDefaultOption(key.toString(), value);
    }

    public void addOption(AutoEvent key, LoggableCommand value) {
        super.addOption(key.toString(), value);
    }

}
