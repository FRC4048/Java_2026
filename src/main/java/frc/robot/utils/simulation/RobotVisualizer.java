package frc.robot.utils.simulation;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;
import frc.robot.constants.Constants;

import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.mechanism.LoggedMechanism2d;
import org.littletonrobotics.junction.mechanism.LoggedMechanismLigament2d;
import org.littletonrobotics.junction.mechanism.LoggedMechanismRoot2d;

public class RobotVisualizer {
    private final LoggedMechanism2d mech2d = new LoggedMechanism2d(2, Units.feetToMeters(7));
    private final LoggedMechanismLigament2d tiltLigament;
    private final LoggedMechanismLigament2d rollerLigament;
    private final LoggedMechanismLigament2d intakeLigament;
    private final LoggedMechanismLigament2d hopperLigament;
    private final LoggedMechanismLigament2d shooterLigament;

    public RobotVisualizer() {
        LoggedMechanismRoot2d root =
                mech2d.getRoot("Robot Root", Constants.DRIVE_BASE_WIDTH / 2, Constants.INITIAL_ROBOT_HEIGHT);

        LoggedMechanismLigament2d riserLigament =
                root.append(
                        new LoggedMechanismLigament2d(
                                "Riser", 0.35, 90, 5, new Color8Bit(Color.kDarkGray)));
        this.tiltLigament =
                riserLigament.append(
                        new LoggedMechanismLigament2d(
                                "Tilt",
                                0.5,
                                90.0,
                                4,
                                new Color8Bit(Color.kCornflowerBlue)));
        this.rollerLigament =
                this.tiltLigament.append(
                        new LoggedMechanismLigament2d(
                                "Roller", 0.05, 180, 5, new Color8Bit(Color.kGreen)));
        
        LoggedMechanismRoot2d intakeRoot =
                mech2d.getRoot("Intake Root", Constants.DRIVE_BASE_WIDTH, Constants.INITIAL_ROBOT_HEIGHT);

        LoggedMechanismLigament2d intakeRiserLigament =
                intakeRoot.append(
                        new LoggedMechanismLigament2d(
                                "Intake Riser", 0.35, 30, 5, new Color8Bit(Color.kDarkGray)));
                            
        this.intakeLigament =
                intakeRiserLigament.append(
                        new LoggedMechanismLigament2d(
                                "Intake Wheel",
                                0.1,
                                90.0,
                                4,
                                new Color8Bit(Color.kRed)));
        
        LoggedMechanismRoot2d hopperRoot =
                mech2d.getRoot("Hopper Root", Constants.DRIVE_BASE_LENGTH, Constants.INITIAL_ROBOT_HEIGHT);

        LoggedMechanismLigament2d hopperRiserLigament = 
                hopperRoot.append(
                        new LoggedMechanismLigament2d(
                                "Hopper Riser", 1.5, 90, 5, new Color8Bit(Color.kDarkGray)));
        
        this.hopperLigament = 
                hopperRiserLigament.append(
                        new LoggedMechanismLigament2d(
                                "Hopper",
                                0.7, 
                                90,
                                5,
                                new Color8Bit(Color.kDarkOrchid)));

        LoggedMechanismRoot2d shooterRoot = 
                mech2d.getRoot("Shooter Root", Constants.DRIVE_BASE_LENGTH*(3/4), Constants.INITIAL_ROBOT_HEIGHT);
        
        LoggedMechanismLigament2d shooterRiserLigament = 
                shooterRoot.append(
                        new LoggedMechanismLigament2d(
                                "Shooter Riser", 0.67, 90, 5, new Color8Bit(Color.kDarkGray)));
                                
        this.shooterLigament = 
                shooterRiserLigament.append(
                        new LoggedMechanismLigament2d(
                                "Shooter",
                                0.25,
                                90,
                                5,
                                new Color8Bit(Color.kDarkViolet)));
    }

    public LoggedMechanismLigament2d getRollerLigament() {
        return rollerLigament;
    }

    public LoggedMechanismLigament2d getTiltLigament() {
        return tiltLigament;
    }

    public LoggedMechanismLigament2d getIntakeLigament() {
        return intakeLigament;
    }

    public LoggedMechanismLigament2d getHopperLigament(){
        return hopperLigament;
    }

    public LoggedMechanismLigament2d getShooterLigament(){
        return shooterLigament;
    }
    public void logMechanism() {
        Logger.recordOutput("Mechanism2d/", mech2d);
    }

    public void close() {
        mech2d.close();
    }
}
