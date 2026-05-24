package frc.robot.commands.lightStrip;

import frc.robot.constants.Constants;
import frc.robot.constants.enums.ShootingState;
import frc.robot.constants.enums.Trench;
import frc.robot.constants.enums.ShootingState.ShootState;
import frc.robot.subsystems.ControllerSubsystem;
import frc.robot.subsystems.LightStripSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.BlinkinPattern;
import frc.robot.utils.logging.commands.LoggableCommand;

public class SetLed extends LoggableCommand {

    private final LightStripSubsystem lightStrip;
    private final ControllerSubsystem controllerSubsystem;
    private final ShooterSubsystem shooterSubsystem;

    public SetLed(LightStripSubsystem lightStrip, ControllerSubsystem controllerSubsystem,
            ShooterSubsystem shooterSubsystem) {

        this.lightStrip = lightStrip;
        this.controllerSubsystem = controllerSubsystem;
        this.shooterSubsystem = shooterSubsystem;
        addRequirements(lightStrip);

    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        if (controllerSubsystem.getTargetShooterVelocityRpm() > -1500) {
            if (controllerSubsystem.getTargetShooterVelocityRpm() > shooterSubsystem.getRPM()
                    * Constants.SHOOTER_RPM_THRESHOLD) {
                lightStrip.setPattern(BlinkinPattern.STROBE_RED);
            } else {
                lightStrip.setPattern(BlinkinPattern.GREEN);
            }
        } else {
            lightStrip.setPattern(BlinkinPattern.RAINBOW_PARTY_PALETTE);
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {

    }

}
