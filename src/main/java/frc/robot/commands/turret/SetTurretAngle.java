package frc.robot.commands.turret;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

/**
 * Runs the turret to the target angle
 */
public class SetTurretAngle extends LoggableCommand {
    private final TurretSubsystem turret;
    private double targetAngle;


    
    public SetTurretAngle(TurretSubsystem turret, double targetAngle) {
        this.turret = turret;
        this.targetAngle = targetAngle;
        addRequirements(turret);
    }

    public SetTurretAngle(TurretSubsystem turret) {
        this(turret, SmartDashboard.getNumber("turret/Target Turret Angle", 0));
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        turret.setAngle(targetAngle);
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}