package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class ClearAutonomousCommand extends InstantCommand {
    public ClearAutonomousCommand(Runnable clearAutonomousCommand) {
        super(clearAutonomousCommand);
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
}
