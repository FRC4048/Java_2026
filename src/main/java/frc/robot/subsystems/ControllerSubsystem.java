package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Robot;
import frc.robot.utils.math.TurretCalculations;

import java.util.ArrayList;

import org.dyn4j.UnitConversion;
import org.littletonrobotics.junction.Logger;

import frc.robot.RobotContainer;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import frc.robot.constants.enums.ShootingState.ShootState;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;

public class ControllerSubsystem extends SubsystemBase {

    private static final double STOP_DELAY_SECONDS = 0.5;
    private ArrayList<Pose2d> lastPoses = new ArrayList<>();

    // Placeholder target poses until real field target values are finalized
    private static final Pose2d BLUE_HUB_TARGET_POSE = new Pose2d(Constants.BLUE_HUB_X_POSITION,
            Constants.BLUE_HUB_Y_POSITION, Rotation2d.kZero);
    private static final Pose2d RED_HUB_TARGET_POSE = new Pose2d(Constants.RED_HUB_X_POSITION,
            Constants.RED_HUB_Y_POSITION, Rotation2d.kZero);
    private static final Pose2d SHUTTLE_TARGET_POSE = new Pose2d(1.0, 7.0, Rotation2d.kZero);

    private static final String MANUAL_POSE_X_KEY = "controller/ManualPoseX";
    private static final String MANUAL_POSE_Y_KEY = "controller/ManualPoseY";
    private static final String MANUAL_POSE_R_KEY = "controller/ManualPoseRotation";
    private static final String USING_MANUAL_POSE_KEY = "controller/UsingManualPose";
    private static final String CURRENT_SHOOT_STATE_KEY = "controller/CurrentShootState";
    private static final String DISTANCE_METERS_KEY = "controller/CalculatedDistanceMeters";
    private static final String TARGET_ANGLER_ANGLE_KEY = "controller/TargetAnglerAngleDegrees";
    private static final String TARGET_SHOOTER_VELOCITY_KEY = "controller/TargetShooterVelocity";
    private static final String TARGET_TURRET_ANGLE_KEY = "controller/TargetTurretAngleDegrees";
    private static final String TARGET_FEEDER_SPEED_KEY = "controller/TargetFeederSpeed";
    private static final String TARGET_HOPPER_SPEED_KEY = "controller/TargetHopperSpeed";

    // Placeholder fixed-state settings.
    private static final ShotTargets STOPPED_TARGETS = new ShotTargets(Constants.ANGLER_ANGLE_LOW, 0.0, 0.0, 0.0, false,
            false);
    private static final ShotTargets FIXED_TARGETS = new ShotTargets(10.0, 120.0, 5.0, 0.0, true, true);
    private static final ShotTargets FIXED_2_TARGETS = new ShotTargets(22.0, 180.0, -5.0, 0.0, true, true);

    // Placeholder pose-driven profiles.
    private static final PoseControlProfile BLUE_HUB_PROFILE = new PoseControlProfile(BLUE_HUB_TARGET_POSE, 32.0, 230.0,
            14.0);
    private static final PoseControlProfile RED_HUB_PROFILE = new PoseControlProfile(RED_HUB_TARGET_POSE, 32.0, 230.0,
            14.0);
    private static final PoseControlProfile SHUTTLE_PROFILE = new PoseControlProfile(SHUTTLE_TARGET_POSE, 16.0, 90.0,
            -14.0);

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
        SmartDashboard.putNumber(MANUAL_POSE_R_KEY, 0.0);
    }

    @Override
    public void periodic() {
        Pose2d robotPose = getRobotPose();
        ShootState currentState = getCurrentShootState();
        updateStopDelayState(currentState);
        updateTargets(currentState, robotPose);
        if (Constants.DEBUG) {
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
        }
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
        // This can be confusing in case you're on the robot and have disabled the
        // drivetrain
        // Uncomment to control manually
        return false;
    }

    private Pose2d getManualPose() {
        double x = SmartDashboard.getNumber(MANUAL_POSE_X_KEY, 0.0);
        double y = SmartDashboard.getNumber(MANUAL_POSE_Y_KEY, 0.0);
        double r = SmartDashboard.getNumber(MANUAL_POSE_R_KEY, 0.0);
        Logger.recordOutput("Manual Pose", new Pose2d(new Translation2d(x, y), new Rotation2d(r)));
        return new Pose2d(x, y, new Rotation2d(r));
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
            case SHOOTING_HUB -> {
                if (Robot.allianceColor().isEmpty()) {
                    // No color, do nothing...
                    useShotTargets(FIXED_TARGETS);
                } else if (Robot.allianceColor().get().equals(DriverStation.Alliance.Blue)) {
                    useShotTargets(calculateTargetsFromPose(BLUE_HUB_PROFILE, robotPose));
                } else if (Robot.allianceColor().get().equals(DriverStation.Alliance.Red)) {
                    useShotTargets(calculateTargetsFromPose(RED_HUB_PROFILE, robotPose));
                } else {
                    // Unknown color, do nothing...
                    useShotTargets(FIXED_TARGETS);
                }
            }

            case SHUTTLING -> useShotTargets(calculateTargetsFromPose(SHUTTLE_PROFILE, robotPose));
        }
    }

    private void updateStoppedTargets() {
        // This makes the shooter wait half a second before stopping
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
        return new ShotTargets(anglerAngleDegrees, shooterVelocity, turretAngleDegrees, computedDistanceMeters, true,
                true);
    }

    private double calculateDistanceMeters(Pose2d robotPose, Pose2d targetPose) {
        double distance = robotPosePredicitionCalculation(robotPose).getTranslation().getDistance(targetPose.getTranslation());
        if(distance > Constants.MAX_HUB_DISTANCE){
            return  Constants.MAX_HUB_DISTANCE;
        }else if(distance < Constants.MIN_HUB_DISTANCE){
            return Constants.MIN_HUB_DISTANCE;
        }else{
            return distance;
        }
    }

    private Pose2d robotPosePredicitionCalculation(Pose2d robotPose) {
        if(lastPoses.size() > 2){
            lastPoses.remove(0);
        }
        Pose2d robotTransform = new Pose2d(robotPose.getTranslation(), new Rotation2d());
        Pose2d predictedTransform = robotTransform.transformBy(new Transform2d(new Translation2d(drivebase.getFieldVelocity().vxMetersPerSecond * Constants.PREDICTION_TIME ,drivebase.getFieldVelocity().vyMetersPerSecond * Constants.PREDICTION_TIME), new Rotation2d()));
        Pose2d predictedPose = new Pose2d(predictedTransform.getTranslation(), robotPose.getRotation());
        Logger.recordOutput("Predicted pose", predictedPose);
        lastPoses.add(predictedPose);
        return predictedPose;
    }
    private double distanceBetweenPreviousPoses(ArrayList<Pose2d> storePoses){
        
        if(storePoses.size() > 2){
        return storePoses.get(1).getTranslation().getDistance(storePoses.get(0).getTranslation());
        }
        return 0;
    }
    private double calculateAnglerAngleDegrees(double computedDistanceMeters, PoseControlProfile profile) {
        if ((profile == BLUE_HUB_PROFILE) || (profile == RED_HUB_PROFILE)) {
            double distance = (3.281 * computedDistanceMeters) - 2;
            return 0.169 * distance * distance
                    - 1.73 * distance
                    + 20.4;
        }
        return profile.defaultAnglerAngleDegrees;
    }

    private double calculateShooterVelocity(double computedDistanceMeters, PoseControlProfile profile) {
        if ((profile == BLUE_HUB_PROFILE) || (profile == RED_HUB_PROFILE)) {
            double distanceBetweenLastPoses = distanceBetweenPreviousPoses(lastPoses) * 100;
            double distance = (3.81 * computedDistanceMeters) - 2;
            return (8.46 * distance * distance
                    - 237 * distance
                    - 1_380) - (500 * distanceBetweenLastPoses > 0 ? distanceBetweenLastPoses : 0);
        }
        return profile.defaultShooterVelocityRpm;
    }

    private double calculateTurretAngleDegrees(Pose2d robotPose, PoseControlProfile profile) {
        return Math.floor(Math.toDegrees(TurretCalculations.calculateTurretAngle(robotPose.getX(), robotPose.getY(),
                robotPose.getRotation().getRadians(),
                DriverStation.getAlliance().get() == DriverStation.Alliance.Blue)));
    }

    // Getters for all the subsystems to set posistion.
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

    // Class to save all the fixed targets
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

    // Class to save all the target poses and each subsystem position at that point
    // so we can calculate true values later on
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
