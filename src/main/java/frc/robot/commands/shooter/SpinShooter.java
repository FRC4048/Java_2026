package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.constants.Constants;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class SpinShooter extends LoggableCommand{
    
    private final ShooterSubsystem subsystem;
    private final double speed;
    private final Timer timer;
  
    public SpinShooter(ShooterSubsystem subsystem, double speed) {
        this.subsystem = subsystem;
        this.speed = speed;
        timer = new Timer();
        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
        timer.restart();
    }  

    @Override
    public void execute() {
        subsystem.setPidVelocity(speed);
    }

    @Override
    public boolean isFinished() {
        return timer.hasElapsed(Constants.SHOOTER_TIMEOUT);
    }

    @Override
    public void end(boolean interrupted) {
        subsystem.stopMotors();
    }

}
