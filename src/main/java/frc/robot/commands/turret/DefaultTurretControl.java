package frc.robot.commands.turret;

import frc.robot.subsystems.ControllerSubsystem;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class DefaultTurretControl extends LoggableCommand {

    private final TurretSubsystem turretSubsystem;
    private final ControllerSubsystem controllerSubsystem;
    private double lastValue = 1000;
    public DefaultTurretControl(TurretSubsystem turretSubsystem, ControllerSubsystem controllerSubsystem) {
        this.turretSubsystem = turretSubsystem;
        this.controllerSubsystem = controllerSubsystem;
        addRequirements(turretSubsystem);
    }

    @Override
    public void execute() {
        if(lastValue != controllerSubsystem.getTargetTurretAngleDegrees()){
            turretSubsystem.setAngle(controllerSubsystem.getTargetTurretAngleDegrees());
            lastValue = controllerSubsystem.getTargetTurretAngleDegrees();
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
