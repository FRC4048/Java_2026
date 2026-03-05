package frc.robot.commands.turret;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.ControllerSubsystem;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class DefaultTurretControl extends LoggableCommand {

    private final TurretSubsystem turretSubsystem;

    public DefaultTurretControl(TurretSubsystem turretSubsystem) {
        this.turretSubsystem = turretSubsystem;
        addRequirements(turretSubsystem);
    }

    @Override
    public void execute() {
        turretSubsystem.setAngle(SmartDashboard.getNumber("turret/setPosition", 0));
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
