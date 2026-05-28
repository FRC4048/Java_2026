package frc.robot.commands.lightStrip;

import frc.robot.constants.enums.ShootingState;
import frc.robot.constants.enums.Trench;
import frc.robot.constants.enums.ShootingState.ShootState;
import frc.robot.subsystems.LightStripSubsystem;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.BlinkinPattern;
import frc.robot.utils.logging.commands.LoggableCommand;

public class SetLed extends LoggableCommand{

    private final LightStripSubsystem lightStrip;
    private ShootingState shootingState;
    private SwerveSubsystem drivebase;
  
    public SetLed(LightStripSubsystem lightStrip, ShootingState shootingState, SwerveSubsystem drivebase) {

        this.lightStrip = lightStrip;
        this.drivebase = drivebase;
        this.shootingState = shootingState;
        addRequirements(lightStrip);

    }

    @Override
    public void initialize() {

    }  

    @Override
    public void execute() {
   /*     double x = drivebase.getPose().getX();
        double y = drivebase.getPose().getY();

        // If statement checks if the robot is near the trench
        if (x > Trench.RED_BOTTOM_LOWER.getX() && x < Trench.RED_BOTTOM_HIGHER.getX() && y > Trench.RED_BOTTOM_LOWER.getY() && y < Trench.RED_BOTTOM_HIGHER.getY() ||
            x > Trench.RED_TOP_LOWER.getX() && x < Trench.RED_TOP_HIGHER.getX() && y > Trench.RED_TOP_LOWER.getY() && y < Trench.RED_TOP_HIGHER.getY() ||
            x > Trench.BLUE_BOTTOM_LOWER.getX() && x < Trench.BLUE_BOTTOM_HIGHER.getX() && y > Trench.BLUE_BOTTOM_LOWER.getY() && y < Trench.BLUE_BOTTOM_HIGHER.getY() ||
            x > Trench.BLUE_TOP_LOWER.getX() && x < Trench.BLUE_TOP_HIGHER.getX() && y > Trench.BLUE_TOP_LOWER.getY() && y < Trench.BLUE_TOP_HIGHER.getY()) {

                if (shootingState.getShootState() != ShootState.STOPPED) { // Light strip only blinks if the shooting state is not stopped
                    lightStrip.setPattern(BlinkinPattern.STROBE_RED);
                }

        } else {
            switch (shootingState.getShootState()) {
            case STOPPED:
                lightStrip.setPattern(BlinkinPattern.BLACK);
                break;
            case FIXED:
                lightStrip.setPattern(BlinkinPattern.AQUA);
                break;
            case FIXED_2:
                lightStrip.setPattern(BlinkinPattern.AQUA);
                break;
            case SHOOTING_HUB:
                lightStrip.setPattern(BlinkinPattern.RAINBOW_RAINBOW_PALETTE);
                break;
            case AUTO_AIM:
                lightStrip.setPattern(BlinkinPattern.RAINBOW_LAVA_PALETTE);
                break;
            case SHUTTLING:
                lightStrip.setPattern(BlinkinPattern.GREEN);
                break;
            }

        }*/ 
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        
    }
    
}
