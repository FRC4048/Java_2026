package frc.robot.commands.shooter;

import frc.robot.constants.ShootingState;
import frc.robot.constants.ShootingState.ShootState;
import frc.robot.utils.logging.commands.LoggableCommand;

public class SetShootingState extends LoggableCommand {
    
    private ShootingState shootState;
    private ShootState newState;

    public SetShootingState(ShootingState shootState, ShootState newState) {
        this.shootState = shootState;
        this.newState = newState;
    }

    @Override
    public void initialize() {
    }  

    @Override
    public void execute() {
        shootState.setShootState(newState);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
    }

}
