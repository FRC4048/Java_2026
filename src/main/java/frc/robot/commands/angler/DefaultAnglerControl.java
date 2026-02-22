package frc.robot.commands.angler;

import frc.robot.subsystems.AnglerSubsystem;
import frc.robot.subsystems.ControllerSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class DefaultAnglerControl extends LoggableCommand {

    private final AnglerSubsystem anglerSubsystem;
    private final ControllerSubsystem controllerSubsystem;

    public DefaultAnglerControl(AnglerSubsystem anglerSubsystem, ControllerSubsystem controllerSubsystem) {
        this.anglerSubsystem = anglerSubsystem;
        this.controllerSubsystem = controllerSubsystem;
        addRequirements(anglerSubsystem);
    }

    @Override
    public void execute() {
        anglerSubsystem.setAngle(controllerSubsystem.getTargetAnglerAngleDegrees());
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
