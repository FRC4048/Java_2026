package frc.robot.commands.turret;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class DefaultSetTurretAngle extends LoggableCommand {
    private final TurretSubsystem turret;
    private double targetAngle;


    
    public DefaultSetTurretAngle(TurretSubsystem turret) {
        this.turret = turret;
        addRequirements(turret);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        turret.setAngle(SmartDashboard.getNumber("Target Angle",0));
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}