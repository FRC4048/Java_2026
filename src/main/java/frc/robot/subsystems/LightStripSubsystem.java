package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.lightStrip.SetLed;
import frc.robot.constants.Constants;
import frc.robot.constants.enums.ShootingState;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.BlinkinPattern;

public class LightStripSubsystem extends SubsystemBase{
    
    public static final String LOGGING_NAME = "LightStripSubsystem";
    private final Spark io;
    private BlinkinPattern pattern;

    public LightStripSubsystem(SwerveSubsystem drivebase, ShootingState shootingState) {
        this.io = new Spark(Constants.LIGHT_STRIP_CHANNEL);
        setDefaultCommand(new SetLed(this, drivebase, shootingState, false));
    }

    public void setPattern(BlinkinPattern pattern) {
        this.pattern = pattern;
        io.set(this.pattern.getPwm());
    }

    public BlinkinPattern getPattern() {
        return pattern;
    }

}
