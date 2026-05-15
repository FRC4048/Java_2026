package frc.robot.commands.testing;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.constants.Constants;
import frc.robot.subsystems.AnglerSubsystem;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.HopperSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

/**
 * Manual dashboard-driven command for shot testing.
 * Runs for 30 seconds, then ends so default commands can resume.
 */
public class RunDashboardShotTest extends LoggableCommand {

    public static final String ANGLER_TARGET_POSITION_KEY = "RunDashboardShotTest/TargetPosition";
    public static final String SHOOTER_TARGET_RPM_KEY = "RunDashboardShotTest/TargetRPMShooter";
    public static final String HOPPER_TARGET_SPEED_KEY = "RunDashboardShotTest/TargetRPMHopper";
    private static final double TEST_DURATION_SECONDS = 60.0;

    private final AnglerSubsystem anglerSubsystem;
    private final ShooterSubsystem shooterSubsystem;
    private final HopperSubsystem hopperSubsystem;
    private final FeederSubsystem feederSubsystem;
    private final Timer timer = new Timer();

    public RunDashboardShotTest(
            AnglerSubsystem anglerSubsystem,
            ShooterSubsystem shooterSubsystem,
            HopperSubsystem hopperSubsystem,
            FeederSubsystem feederSubsystem) {
        this.hopperSubsystem = hopperSubsystem;
        this.feederSubsystem = feederSubsystem;
        this.anglerSubsystem = anglerSubsystem;
        this.shooterSubsystem = shooterSubsystem;
        addRequirements(anglerSubsystem, shooterSubsystem, hopperSubsystem, feederSubsystem);
    }

    @Override
    public void initialize() {
        timer.restart();
    }

    @Override
    public void execute() {
        double anglerAngle = SmartDashboard.getNumber(ANGLER_TARGET_POSITION_KEY, Constants.ANGLER_ANGLE_LOW);
        double shooterRpm = SmartDashboard.getNumber(SHOOTER_TARGET_RPM_KEY, 0.0);
        double hopperSpeed = SmartDashboard.getNumber(HOPPER_TARGET_SPEED_KEY, 0.0);

        anglerSubsystem.setAngle(anglerAngle);
        hopperSubsystem.setSpeed(hopperSpeed);
        feederSubsystem.setSpeed(Constants.FEEDER_SPEED);
        shooterSubsystem.setPidVelocity(shooterRpm);
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
        hopperSubsystem.stopMotors();
        feederSubsystem.stopMotors();
    }
}
