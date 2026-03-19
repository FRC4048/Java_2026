package frc.robot.commands.turret;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.constants.Constants;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class TurretToRev extends LoggableCommand{
    
    private final TurretSubsystem turret;
    private final Timer timer = new Timer();

    public TurretToRev(TurretSubsystem turret) {
        this.turret = turret;
        addRequirements(turret);
    }

    @Override
    public void initialize() {
        timer.restart();
    }

    @Override
    public void execute() {
        turret.runReverse();
    }

    @Override
    public void end(boolean interrupted) {
        turret.stopMotors();
    }

    @Override
    public boolean isFinished() {
        return turret.isAtReverseLimit() || timer.hasElapsed(Constants.TURRET_TIMEOUT);
    }
}
