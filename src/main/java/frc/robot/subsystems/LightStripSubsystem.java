package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import frc.robot.utils.BlinkinPattern;

public class LightStripSubsystem extends SubsystemBase{
    
    public static final String LOGGING_NAME = "LightStripSubsystem";
    private final Spark io;
    private BlinkinPattern pattern;

    public LightStripSubsystem() {
        this.io = new Spark(Constants.LIGHT_STRIP_CHANNEL);
    }

    public void setPattern(BlinkinPattern pattern) {
        this.pattern = pattern;
        io.set(this.pattern.getPwm());
    }

    public BlinkinPattern getPattern() {
        return pattern;
    }

}
