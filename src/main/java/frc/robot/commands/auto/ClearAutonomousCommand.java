package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.commands.shooter.SetShootingState;
import frc.robot.constants.enums.ShootingState;
import frc.robot.constants.enums.ShootingState.ShootState;

public class ClearAutonomousCommand extends InstantCommand {
    public ClearAutonomousCommand(Runnable clearAutonomousCommand, ShootingState shootstate) {
        super(clearAutonomousCommand);
        new SetShootingState(shootstate, ShootState.STOPPED);
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
}
