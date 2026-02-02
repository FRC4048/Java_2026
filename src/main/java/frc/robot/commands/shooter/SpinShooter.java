package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.constants.Constants;
import frc.robot.subsystems.shooter.ShooterLeaderSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class SpinShooter extends LoggableCommand{
    
    public final ShooterLeaderSubsystem subsystem;
    public final Timer timer;
    

    public SpinShooter(ShooterLeaderSubsystem subsystem){
        timer = new Timer();
        this.subsystem = subsystem;
        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
      timer.restart();
    }

    @Override
    public void execute() {
            subsystem.setSpeed(Constants.SHOOTER_SPEED);
    }

    @Override
    public void end(boolean interrupted) {
        subsystem.stopMotors();
    }

    @Override
    public boolean isFinished() {
        if (timer.hasElapsed(Constants.SHOOTER_TIMEOUT)){
            return true;
        }
        else{
            return false;
        }

    }

    
}
