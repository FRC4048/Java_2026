package frc.robot.subsystems;

import org.littletonrobotics.junction.Logger;

import frc.robot.RobotContainer;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import frc.robot.constants.enums.ShootingState;
import frc.robot.constants.enums.ShootingState.ShootState;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;

public class ControllerSubsystem extends SubsystemBase {

    private static final double STOP_DELAY_SECONDS = 0.5;

    // Placeholder target poses until real field target values are finalized
    private static final Pose2d HUB_TARGET_POSE = new Pose2d(4.0, 4.0, Rotation2d.kZero);
    private static final Pose2d SHUTTLE_TARGET_POSE = new Pose2d(1.0, 7.0, Rotation2d.kZero);

    private static final String MANUAL_POSE_X_KEY = "controller/ManualPoseX";
    private static final String MANUAL_POSE_Y_KEY = "controller/ManualPoseY";
    private static final String USING_MANUAL_POSE_KEY = "controller/UsingManualPose";
    private static final String CURRENT_SHOOT_STATE_KEY = "controller/CurrentShootState";
    private static final String DISTANCE_METERS_KEY = "controller/CalculatedDistanceMeters";
    private static final String TARGET_ANGLER_ANGLE_KEY = "controller/TargetAnglerAngleDegrees";
    private static final String TARGET_SHOOTER_VELOCITY_KEY = "controller/TargetShooterVelocity";
    private static final String TARGET_TURRET_ANGLE_KEY = "controller/TargetTurretAngleDegrees";
    private static final String TARGET_FEEDER_SPEED_KEY = "controller/TargetFeederSpeed";
    private static final String TARGET_HOPPER_SPEED_KEY = "controller/TargetHopperSpeed";

    // Placeholder fixed-state settings.
    private static final ShotTargets STOPPED_TARGETS =
            new ShotTargets(Constants.ANGLER_ANGLE_LOW, 0.0, 0.0, 0.0, false, false);
    private static final ShotTargets FIXED_TARGETS =
            new ShotTargets(10.0, 120.0, 5.0, 0.0, true, true);
    private static final ShotTargets FIXED_2_TARGETS =
            new ShotTargets(22.0, 180.0, -5.0, 0.0, true, true);

    // Placeholder pose-driven profiles.
    private static final PoseControlProfile HUB_PROFILE =
            new PoseControlProfile(HUB_TARGET_POSE, 32.0, 230.0, 14.0);
    private static final PoseControlProfile SHUTTLE_PROFILE =
            new PoseControlProfile(SHUTTLE_TARGET_POSE, 16.0, 90.0, -14.0);

    private final SwerveSubsystem drivebase;
    private final RobotContainer robotContainer;
    private final Timer stopDelayTimer = new Timer();

    private ShootState previousState;
    private ShotTargets activeTargets;
    private boolean driverActivatedShooting = false;


    public ControllerSubsystem(SwerveSubsystem drivebase, RobotContainer robotContainer) {
        this.drivebase = drivebase;
        this.robotContainer = robotContainer;
        this.previousState = getCurrentShootState();
        this.activeTargets = STOPPED_TARGETS;

        SmartDashboard.putNumber(MANUAL_POSE_X_KEY, 0.0);
        SmartDashboard.putNumber(MANUAL_POSE_Y_KEY, 0.0);
    }

    @Override
    public void periodic() {
        Pose2d robotPose = getRobotPose();
        ShootState currentState = getCurrentShootState();

        updateStopDelayState(currentState);
        updateTargets(currentState, robotPose);

        SmartDashboard.putString(CURRENT_SHOOT_STATE_KEY, currentState.toString());
        SmartDashboard.putNumber(DISTANCE_METERS_KEY, activeTargets.distanceMeters);
        SmartDashboard.putNumber(TARGET_ANGLER_ANGLE_KEY, activeTargets.anglerAngleDegrees);
        SmartDashboard.putNumber(TARGET_SHOOTER_VELOCITY_KEY, activeTargets.shooterVelocityRpm);
        SmartDashboard.putNumber(TARGET_TURRET_ANGLE_KEY, activeTargets.turretAngleDegrees);
        SmartDashboard.putBoolean(TARGET_FEEDER_SPEED_KEY, activeTargets.feederSpin);
        SmartDashboard.putBoolean(TARGET_HOPPER_SPEED_KEY, activeTargets.hopperSpin);

        Logger.recordOutput(CURRENT_SHOOT_STATE_KEY, currentState.toString());
        Logger.recordOutput(DISTANCE_METERS_KEY, activeTargets.distanceMeters);
        Logger.recordOutput(TARGET_ANGLER_ANGLE_KEY, activeTargets.anglerAngleDegrees);
        Logger.recordOutput(TARGET_SHOOTER_VELOCITY_KEY, activeTargets.shooterVelocityRpm);
        Logger.recordOutput(TARGET_TURRET_ANGLE_KEY, activeTargets.turretAngleDegrees);
        Logger.recordOutput(TARGET_FEEDER_SPEED_KEY, activeTargets.feederSpin);
        Logger.recordOutput(TARGET_HOPPER_SPEED_KEY, activeTargets.hopperSpin);
        
        previousState = currentState;
    }

    private ShootState getCurrentShootState() {
        return robotContainer.getShootingState().getShootState();
    }

    private Pose2d getRobotPose() {
        SmartDashboard.putBoolean(USING_MANUAL_POSE_KEY, shouldUseManualPose());
        Logger.recordOutput(USING_MANUAL_POSE_KEY, shouldUseManualPose());
        if (shouldUseManualPose()) {
            return getManualPose();
        }
        return drivebase.getPose();
    }

    private boolean shouldUseManualPose() {
        return (Constants.currentMode == Constants.Mode.SIM) || Constants.TESTBED;
    }

    private Pose2d getManualPose() {
        double x = SmartDashboard.getNumber(MANUAL_POSE_X_KEY, 0.0);
        double y = SmartDashboard.getNumber(MANUAL_POSE_Y_KEY, 0.0);
        return new Pose2d(x, y, Rotation2d.kZero);
    }

    private void updateStopDelayState(ShootState currentState) {
        if (currentState == ShootState.STOPPED && previousState != ShootState.STOPPED) {
            stopDelayTimer.restart();
        }
    }

    private void updateTargets(ShootState state, Pose2d robotPose) {
        switch (state) {
            case STOPPED -> updateStoppedTargets();
            case FIXED -> useShotTargets(FIXED_TARGETS);
            case FIXED_2 -> useShotTargets(FIXED_2_TARGETS);
            case SHOOTING_HUB -> useShotTargets(calculateTargetsFromPose(HUB_PROFILE, robotPose));
            case SHUTTLING -> useShotTargets(calculateTargetsFromPose(SHUTTLE_PROFILE, robotPose));
        }
    }

    private void updateStoppedTargets() {
        //This makes the shooter wait half a second before stopping
        double shooterVelocity = stopDelayTimer.hasElapsed(STOP_DELAY_SECONDS)
                ? 0.0
                : activeTargets.shooterVelocityRpm;

        activeTargets = new ShotTargets(
                STOPPED_TARGETS.anglerAngleDegrees,
                shooterVelocity,
                STOPPED_TARGETS.turretAngleDegrees,
                STOPPED_TARGETS.distanceMeters,
                STOPPED_TARGETS.feederSpin,
                STOPPED_TARGETS.hopperSpin);
    }

    private void useShotTargets(ShotTargets shotTargets) {
        boolean driverEnabled = driverActivatedShootingEnabled();
        activeTargets = new ShotTargets(
                shotTargets.anglerAngleDegrees,
                shotTargets.shooterVelocityRpm,
                shotTargets.turretAngleDegrees,
                shotTargets.distanceMeters,
                driverEnabled,
                driverEnabled);
    }

    private ShotTargets calculateTargetsFromPose(PoseControlProfile profile, Pose2d robotPose) {
        double computedDistanceMeters = calculateDistanceMeters(robotPose, profile.targetPose);
        double anglerAngleDegrees = calculateAnglerAngleDegrees(computedDistanceMeters, profile);
        double shooterVelocity = calculateShooterVelocity(computedDistanceMeters, profile);
        double turretAngleDegrees = calculateTurretAngleDegrees(robotPose, profile);
        return new ShotTargets(anglerAngleDegrees, shooterVelocity, turretAngleDegrees, computedDistanceMeters, true, true);
    }

    private double calculateDistanceMeters(Pose2d robotPose, Pose2d targetPose) {
        return robotPose.getTranslation().getDistance(targetPose.getTranslation());
    }

    private double calculateAnglerAngleDegrees(double computedDistanceMeters, PoseControlProfile profile) {
        // TODO: Replace with distance angler angle calculation.
        return profile.defaultAnglerAngleDegrees;
    }

    private double calculateShooterVelocity(double computedDistanceMeters, PoseControlProfile profile) {
        // TODO: Replace with distance shooter velocity calculation.
        return profile.defaultShooterVelocityRpm;
    }

    private double calculateTurretAngleDegrees(Pose2d robotPose, PoseControlProfile profile) {
        // TODO: Replace with distance turret angle calculation.
        return profile.defaultTurretAngleDegrees;
    }

    //Getters for all the subsystems to set posistion.
    public double getTargetAnglerAngleDegrees() {
        return activeTargets.anglerAngleDegrees;
    }

    public double getTargetShooterVelocityRpm() {
        return activeTargets.shooterVelocityRpm;
    }

    public double getTargetTurretAngleDegrees() {
        return activeTargets.turretAngleDegrees;
    }

    public double getDistanceMeters() {
        return activeTargets.distanceMeters;
    }

    public boolean shouldFeederSpin() {
        return activeTargets.feederSpin;
    }

    public boolean shouldHopperSpin() {
        return activeTargets.hopperSpin;
    }

    public void setDriverActivatedShooting(boolean set) {
        driverActivatedShooting = set;
    }

    public boolean driverActivatedShootingEnabled() {
        return driverActivatedShooting;
    }


    //Class to save all the fixed targets
    private static final class ShotTargets {
        private final double anglerAngleDegrees;
        private final double shooterVelocityRpm;
        private final double turretAngleDegrees;
        private final double distanceMeters;
        private final boolean feederSpin;
        private final boolean hopperSpin;

        private ShotTargets(
                double anglerAngleDegrees,
                double shooterVelocityRpm,
                double turretAngleDegrees,
                double distanceMeters,
                boolean feederSpin,
                boolean hopperSpin) {
            this.anglerAngleDegrees = anglerAngleDegrees;
            this.shooterVelocityRpm = shooterVelocityRpm;
            this.turretAngleDegrees = turretAngleDegrees;
            this.distanceMeters = distanceMeters;
            this.feederSpin = feederSpin;
            this.hopperSpin = hopperSpin;
        }
    }

    //Class to save all the target poses and each subsystem position at that point so we can calculate true values later on
    private static final class PoseControlProfile {
        private final Pose2d targetPose;
        private final double defaultAnglerAngleDegrees;
        private final double defaultShooterVelocityRpm;
        private final double defaultTurretAngleDegrees;

        private PoseControlProfile(
                Pose2d targetPose,
                double defaultAnglerAngleDegrees,
                double defaultShooterVelocityRpm,
                double defaultTurretAngleDegrees) {
            this.targetPose = targetPose;
            this.defaultAnglerAngleDegrees = defaultAnglerAngleDegrees;
            this.defaultShooterVelocityRpm = defaultShooterVelocityRpm;
            this.defaultTurretAngleDegrees = defaultTurretAngleDegrees;
        }
    }
}
