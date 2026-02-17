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
        private final LoggedMechanismLigament2d climberLigament;
    private final LoggedMechanismLigament2d feederLigament;
        private final LoggedMechanismLigament2d anglerLigament;
    private final LoggedMechanismLigament2d turretLigament;
        private final LoggedMechanismLigament2d shooterTiltLigament;
        private final LoggedMechanismLigament2d shooterLigament;
        private final LoggedMechanismLigament2d intakeDeploymentLigament;

        public RobotVisualizer() {
                LoggedMechanismRoot2d root = mech2d.getRoot("Robot Root", Constants.DRIVE_BASE_WIDTH / 2,
                                Constants.INITIAL_ROBOT_HEIGHT);

                LoggedMechanismLigament2d riserLigament = root.append(
                                new LoggedMechanismLigament2d(
                                                "Riser", 0.35, 90, 5, new Color8Bit(Color.kDarkGray)));
                this.tiltLigament = riserLigament.append(
                                new LoggedMechanismLigament2d(
                                                "Tilt",
                                                0.5,
                                                90.0,
                                                4,
                                                new Color8Bit(Color.kCornflowerBlue)));
                this.rollerLigament = this.tiltLigament.append(
                                new LoggedMechanismLigament2d(
                                                "Roller", 0.05, 180, 5, new Color8Bit(Color.kGreen)));

                LoggedMechanismRoot2d anglerRoot = mech2d.getRoot("Angler Root", Constants.DRIVE_BASE_WIDTH,
                                Constants.INITIAL_ROBOT_HEIGHT);

                LoggedMechanismLigament2d anglerRiserLigament = anglerRoot.append(
                                new LoggedMechanismLigament2d(
                                                "Angler Riser", 0.55, 40, 6.7, new Color8Bit(Color.kDarkGray)));

                this.anglerLigament = anglerRiserLigament.append(
                                new LoggedMechanismLigament2d(
                                                "Angler", 0.1, 180, 5, new Color8Bit(Color.kWhite)));

                LoggedMechanismRoot2d intakeRoot = mech2d.getRoot("Intake Root", Constants.DRIVE_BASE_WIDTH,
                                Constants.INITIAL_ROBOT_HEIGHT);

                LoggedMechanismLigament2d intakeRiserLigament = intakeRoot.append(
                                new LoggedMechanismLigament2d(
                                                "Intake Riser", 0.35, 30, 5, new Color8Bit(Color.kDarkGray)));

                this.intakeLigament = intakeRiserLigament.append(
                                new LoggedMechanismLigament2d(
                                                "Intake Wheel",
                                                0.1,
                                                90.0,
                                                4,
                                                new Color8Bit(Color.kRed)));

                LoggedMechanismRoot2d hopperRoot = mech2d.getRoot("Hopper Root", Constants.DRIVE_BASE_LENGTH,
                                Constants.INITIAL_ROBOT_HEIGHT);

                LoggedMechanismLigament2d hopperRiserLigament = hopperRoot.append(
                                new LoggedMechanismLigament2d(
                                                "Hopper Riser", 1.5, 90, 5, new Color8Bit(Color.kDarkGray)));

                this.hopperLigament = hopperRiserLigament.append(
                                new LoggedMechanismLigament2d(
                                                "Hopper",
                                                0.7,
                                                90,
                                                5,
                                                new Color8Bit(Color.kDarkOrchid)));

        LoggedMechanismRoot2d climberRoot = 
                mech2d.getRoot("Climber Root", Constants.DRIVE_BASE_LENGTH*(3/4), Constants.INITIAL_ROBOT_HEIGHT);
        
        LoggedMechanismLigament2d climberRiserLigament = 
                climberRoot.append(
                        new LoggedMechanismLigament2d(
                                "Climber Riser", 0.67, 90, 5, new Color8Bit(Color.kDarkGray)));
                                
        this.climberLigament = 
                climberRiserLigament.append(
                        new LoggedMechanismLigament2d(
                                "Climber",
                                0.25,
                                90,
                                5,
                                new Color8Bit(Color.kDarkBlue)));
        

                LoggedMechanismRoot2d feederRoot = mech2d.getRoot("Feeder Root", Constants.DRIVE_BASE_WIDTH * 2,
                                Constants.INITIAL_ROBOT_HEIGHT);

                LoggedMechanismLigament2d feederRiserLigament = feederRoot.append(
                                new LoggedMechanismLigament2d(
                                                "Feeder Riser", 0.35, 90, 5, new Color8Bit(Color.kDarkGray)));

                this.feederLigament = feederRiserLigament.append(
                                new LoggedMechanismLigament2d(
                                                "Feeder Wheel",
                                                0.15,
                                                90.0,
                                                4,
                                                new Color8Bit(Color.kYellow)));

                        LoggedMechanismRoot2d turretRoot =
                mech2d.getRoot("Turret Root", Constants.DRIVE_BASE_WIDTH * 2, Constants.INITIAL_ROBOT_HEIGHT);

        LoggedMechanismLigament2d turretRiserLigament =
                intakeRoot.append(
                        new LoggedMechanismLigament2d(
                                "Turret Riser", 0.5, 45, 8, new Color8Bit(Color.kMediumPurple)));
                            
        this.turretLigament =
                turretRiserLigament.append(
                        new LoggedMechanismLigament2d(
                                "Intake Wheel",
                                0.1,
                                90.0,
                                4,
                                new Color8Bit(Color.kRed)));

        LoggedMechanismRoot2d shooterRoot = mech2d.getRoot("Shooter Root", Constants.DRIVE_BASE_WIDTH * 2.5,
                                Constants.INITIAL_ROBOT_HEIGHT);

                LoggedMechanismLigament2d shooterRiserLigament = shooterRoot.append(
                                new LoggedMechanismLigament2d(
                                                "Shooter Riser", 0.25, 90, 5, new Color8Bit(Color.kDarkGray)));

                this.shooterTiltLigament = shooterRiserLigament.append(
                                new LoggedMechanismLigament2d(
                                                "Shooter Tilt",
                                                0.5,
                                                -45.0,
                                                4,
                                                new Color8Bit(Color.kPurple)));

                this.shooterLigament = shooterTiltLigament.append(
                                new LoggedMechanismLigament2d(
                                                "Shooter Wheel",
                                                0.15,
                                                -45.0,
                                                4,
                                                new Color8Bit(Color.kOrange)));
                LoggedMechanismRoot2d intakeDeployerRoot = mech2d.getRoot("Intake Deployment Root",
                                Constants.DRIVE_BASE_WIDTH * 2.5, Constants.INITIAL_ROBOT_HEIGHT);

                LoggedMechanismLigament2d intakeDeploymentLigament = intakeDeployerRoot.append(
                                new LoggedMechanismLigament2d(
                                                "Intake Deployment", 0.4, 45, 4, new Color8Bit(Color.kAquamarine)));
                this.intakeDeploymentLigament = intakeDeploymentLigament.append(
                                new LoggedMechanismLigament2d(
                                                "Intake Deployment",
                                                0.5,
                                                0,
                                                4,
                                                new Color8Bit(Color.kBrown)));

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

        public LoggedMechanismLigament2d getHopperLigament() {
                return hopperLigament;
        }

        public LoggedMechanismLigament2d getFeederLigament() {
                return feederLigament;
        }

    public LoggedMechanismLigament2d getClimberLigament() {
        return climberLigament;
}

        public LoggedMechanismLigament2d getAnglerLigament() {
                return anglerLigament;
        }

        public LoggedMechanismLigament2d getShooterTiltLigament() {
                return shooterTiltLigament;
        }

        public LoggedMechanismLigament2d getShooterLigament() {
                return shooterLigament;
        }

    public LoggedMechanismLigament2d getTurretLigament() {
        return turretLigament;
    }

        public LoggedMechanismLigament2d getIntakeDeploymentLigament() {
                return intakeDeploymentLigament;
        }

        public void logMechanism() {
                Logger.recordOutput("Mechanism2d/", mech2d);
        }

        public void close() {
                mech2d.close();
        }


}
