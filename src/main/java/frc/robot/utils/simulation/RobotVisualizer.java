package frc.robot.utils.simulation;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;
import frc.robot.Constants;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.mechanism.LoggedMechanism2d;
import org.littletonrobotics.junction.mechanism.LoggedMechanismLigament2d;
import org.littletonrobotics.junction.mechanism.LoggedMechanismRoot2d;

public class RobotVisualizer {
    private final LoggedMechanism2d mech2d = new LoggedMechanism2d(2, Units.feetToMeters(7));
    private final LoggedMechanismLigament2d tiltLigament;
    private final LoggedMechanismLigament2d rollerLigament;

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
    }

    public LoggedMechanismLigament2d getRollerLigament() {
        return rollerLigament;
    }

    public LoggedMechanismLigament2d getTiltLigament() {
        return tiltLigament;
    }

    public void logMechanism() {
        Logger.recordOutput("Mechanism2d/", mech2d);
    }

    public void close() {
        mech2d.close();
    }
}
