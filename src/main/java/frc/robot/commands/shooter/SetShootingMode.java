package frc.robot.commands.shooter;


import frc.robot.constants.ShootingMode;


import frc.robot.utils.logging.commands.LoggableCommand;

public class SetShootingMode extends LoggableCommand {
    
    private ShootingMode shootingMode;
    private final ShootingMode newMode;
  
    public SetShootingMode(ShootingMode shootingMode, ShootingMode newMode) {
        this.shootingMode = shootingMode;
        this.newMode = newMode;
    }

    @Override
    public void initialize() {
    }  

    @Override
    public void execute() {
        shootingMode = newMode;
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
    
    }

}
