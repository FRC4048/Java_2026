package frc.robot.commands.testing;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.constants.Constants;
import frc.robot.subsystems.AnglerSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

/**
 * Manual dashboard-driven command for shot testing.
 * Runs for 30 seconds, then ends so default commands can resume.
 */
public class RunDashboardShotTest extends LoggableCommand {

    public static final String ANGLER_TARGET_POSITION_KEY = "angler/TargetPosition";
    public static final String TURRET_TARGET_POSITION_KEY = "turret/TargetPosition";
    public static final String SHOOTER_TARGET_RPM_KEY = "shooter/TargetRPM";
    private static final double TEST_DURATION_SECONDS = 30.0;

    private final AnglerSubsystem anglerSubsystem;
    private final TurretSubsystem turretSubsystem;
    private final ShooterSubsystem shooterSubsystem;
    private final Timer timer = new Timer();

    public RunDashboardShotTest(
            AnglerSubsystem anglerSubsystem,
            TurretSubsystem turretSubsystem,
            ShooterSubsystem shooterSubsystem) {
        this.anglerSubsystem = anglerSubsystem;
        this.turretSubsystem = turretSubsystem;
        this.shooterSubsystem = shooterSubsystem;
        addRequirements(anglerSubsystem, turretSubsystem, shooterSubsystem);
    }

    @Override
    public void initialize() {
        timer.restart();
    }

    @Override
    public void execute() {
        double anglerAngle = SmartDashboard.getNumber(ANGLER_TARGET_POSITION_KEY, Constants.ANGLER_ANGLE_LOW);
        double turretAngle = SmartDashboard.getNumber(TURRET_TARGET_POSITION_KEY, Constants.TURRET_HOME_ANGLE);
        double shooterRpm = SmartDashboard.getNumber(SHOOTER_TARGET_RPM_KEY, 0.0);

        anglerSubsystem.setPosition(anglerAngle);
        turretSubsystem.setPosition(turretAngle);

//        if (shooterRpm > 0.0) {
        shooterSubsystem.setPidVelocity(shooterRpm);
//        } else {
//            shooterSubsystem.stopMotors();
//        }
    }

    @Override
    public boolean isFinished() {
        return timer.hasElapsed(TEST_DURATION_SECONDS);
    }

    @Override
    public void end(boolean interrupted) {
        timer.stop();
        anglerSubsystem.stopMotors();
        shooterSubsystem.stopMotors();
        turretSubsystem.stopMotors();
    }
}
