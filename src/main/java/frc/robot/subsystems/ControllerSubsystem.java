package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Robot;
import frc.robot.utils.math.TurretCalculations;

import java.util.ArrayList;

import org.dyn4j.UnitConversion;
import org.littletonrobotics.junction.Logger;

import frc.robot.RobotContainer;
import frc.robot.commands.intakeDeployment.SetDeploymentState;
import frc.robot.commands.intakeDeployment.ToggleDeployment;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.constants.Constants;
import frc.robot.constants.enums.ShootingState.ShootState;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.math.TurretCalculations;
import org.dyn4j.UnitConversion;
import org.littletonrobotics.junction.Logger;

public class ControllerSubsystem extends SubsystemBase {

    private static final double STOP_DELAY_SECONDS = 0.5;
    private static final double SHOOT_DELAY_SECONDS = 1.5;

    // Placeholder target poses until real field target values are finalized
    private static final Pose2d BLUE_HUB_TARGET_POSE = new Pose2d(Constants.BLUE_HUB_X_POSITION,
            Constants.BLUE_HUB_Y_ADJUSTED_POSITION, Rotation2d.kZero);
    private static final Pose2d RED_HUB_TARGET_POSE = new Pose2d(Constants.RED_HUB_X_POSITION,
            Constants.RED_HUB_Y_ADJUSTED_POSITION, Rotation2d.kZero);
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
    private boolean isadjustedPosition = false;
    // Placeholder fixed-state settings.
    private static final ShotTargets STOPPED_TARGETS = new ShotTargets(Constants.ANGLER_ANGLE_LOW, 0.0, 0.0, 0.0, false,
            false, false);
    //3.25 meters away
    private static final ShotTargets FIXED_TARGETS = new ShotTargets(21.16, -2945.21, 0, 3.25, true, true, true);
    private static final ShotTargets FIXED_2_TARGETS = new ShotTargets(22.0, 180.0, -5.0, 0.0, true, true, true);

    // Placeholder pose-driven profiles.
    private static final PoseControlProfile BLUE_HUB_PROFILE = new PoseControlProfile(BLUE_HUB_TARGET_POSE, 32.0, 230.0,
            14.0);
    private static final PoseControlProfile RED_HUB_PROFILE = new PoseControlProfile(RED_HUB_TARGET_POSE, 32.0, 230.0,
            14.0);
    private static final PoseControlProfile RED_SHUTTLE_PROFILE = new PoseControlProfile(RED_HUB_TARGET_POSE, 37.0, 90.0,
            -14.0);
    private static final PoseControlProfile BLUE_SHUTTLE_PROFILE = new PoseControlProfile(BLUE_HUB_TARGET_POSE, 37.0, 90.0,
            -14.0);

    private final SwerveSubsystem drivebase;
    private final IntakeDeployerSubsystem intakeDeployer;
    private final RobotContainer robotContainer;
    private final Timer stopDelayTimer = new Timer();
    private final Timer shootDelayTimer = new Timer();

    private ShootState previousState;
    private ShotTargets activeTargets;
    private boolean driverActivatedShooting = false;

    public ControllerSubsystem(SwerveSubsystem drivebase, IntakeDeployerSubsystem intakeDeployer, RobotContainer robotContainer) {
        this.drivebase = drivebase;
        this.robotContainer = robotContainer;
        this.previousState = getCurrentShootState();
        this.activeTargets = STOPPED_TARGETS;
        this.intakeDeployer = intakeDeployer;

        SmartDashboard.putNumber(MANUAL_POSE_X_KEY, 0.0);
        SmartDashboard.putNumber(MANUAL_POSE_Y_KEY, 0.0);
        SmartDashboard.putNumber(MANUAL_POSE_R_KEY, 0.0);
    }

    @Override
    public void periodic() {
        Pose2d robotPose = getRobotPose();
        ShootState currentState = getCurrentShootState();
        updateStopDelayState(currentState);
        updateShootDelayState(currentState);
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
        boolean useManualPose = shouldUseManualPose();
        SmartDashboard.putBoolean(USING_MANUAL_POSE_KEY, useManualPose);
        Logger.recordOutput(USING_MANUAL_POSE_KEY, useManualPose);
        if (useManualPose) {
            return getManualPose();
        }
        return drivebase.getPose();
    }

    private boolean shouldUseManualPose() {
        // Uncomment to control manually
        // return Constants.TESTBED || drivebase == null;
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

    private void updateShootDelayState(ShootState currentState) {
        if (currentState != ShootState.STOPPED && previousState != currentState) {
            shootDelayTimer.restart();
        }
    }

    private void updateTargets(ShootState state, Pose2d robotPose) {
//        if (!activeTargets.intakeDeploy && intakeDeployer.getDeploymentState() == DeploymentState.DOWN) {
//            new SetDeploymentState(intakeDeployer, DeploymentState.STOPPED).schedule();
//        }
        switch (state) {
            case STOPPED -> updateStoppedTargets();
            case FIXED -> useShotTargets(FIXED_TARGETS);
            case FIXED_2 -> useShotTargets(FIXED_2_TARGETS);
            case SHOOTING_HUB -> {
                if (Robot.allianceColor().isEmpty()) {
                    // No color, do nothing...
                    useShotTargets(FIXED_TARGETS);
                } else if (Robot.allianceColor().get().equals(DriverStation.Alliance.Blue)) {
                    useShotTargets(calculateTargetsFromPose(state, BLUE_HUB_PROFILE, robotPosePredictionCalculation(BLUE_HUB_PROFILE.targetPose, robotPose)));
                } else if (Robot.allianceColor().get().equals(DriverStation.Alliance.Red)) {
                    useShotTargets(calculateTargetsFromPose(state, RED_HUB_PROFILE, robotPosePredictionCalculation(RED_HUB_PROFILE.targetPose, robotPose)));
                } else {
                    // Unknown color, do nothing...
                    useShotTargets(FIXED_TARGETS);
                }
            }
            case SHUTTLING -> {
                if (Robot.allianceColor().isEmpty()) {
                    useShotTargets(FIXED_TARGETS);
                } else if (Robot.allianceColor().get().equals(DriverStation.Alliance.Blue)) {
                    useShotTargets(calculateTargetsFromPose(state, BLUE_SHUTTLE_PROFILE, robotPose));
                } else if (Robot.allianceColor().get().equals(DriverStation.Alliance.Red)) {
                    useShotTargets(calculateTargetsFromPose(state, RED_SHUTTLE_PROFILE, robotPose));
                } else {
                    useShotTargets(FIXED_TARGETS);
                }
            }
            case AUTO_AIM ->{ if (Robot.allianceColor().isEmpty()) {
                    useShotTargets(FIXED_TARGETS);
                } else if (Robot.allianceColor().get().equals(DriverStation.Alliance.Blue)) {
                    useShotTargets(calculateTargetsFromPose(state, BLUE_HUB_PROFILE, robotPosePredictionCalculation(BLUE_HUB_PROFILE.targetPose,robotPose)));
                } else if (Robot.allianceColor().get().equals(DriverStation.Alliance.Red)) {
                    useShotTargets(calculateTargetsFromPose(state, RED_HUB_PROFILE, robotPosePredictionCalculation(RED_HUB_PROFILE.targetPose,robotPose)));
                } else {
                    useShotTargets(FIXED_TARGETS);
                }}
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
                STOPPED_TARGETS.hopperSpin,
                STOPPED_TARGETS.intakeDeploy);
    }

    private void useShotTargets(ShotTargets shotTargets) {
        double shooterVelocityRpm = shotTargets.shooterVelocityRpm;
        if (isTurretTargetOutOfRange(shotTargets.turretAngleDegrees) && shooterVelocityRpm != 0.0) {
        //    shooterVelocityRpm = Constants.TURRET_OUT_OF_RANGE_FLOP_RPM;
        }

        // This makes everything wait until after the shooter has run for half a second before starting
        if (shootDelayTimer.hasElapsed(SHOOT_DELAY_SECONDS)) {
            activeTargets = new ShotTargets(
                    shotTargets.anglerAngleDegrees,
                    shooterVelocityRpm,
                    shotTargets.turretAngleDegrees,
                    shotTargets.distanceMeters,
                    shotTargets.hopperSpin,
                    shotTargets.feederSpin,
                    shotTargets.intakeDeploy);
        } else {
            activeTargets = new ShotTargets(
                    shotTargets.anglerAngleDegrees,
                    shooterVelocityRpm, // Shooter starts half a second before everything else
                    shotTargets.turretAngleDegrees,
                    shotTargets.distanceMeters,
                    false,
                    false,
                    activeTargets.intakeDeploy);
        }

    }

    private boolean isTurretTargetOutOfRange(double turretAngleDegrees) {
        return turretAngleDegrees < Constants.TURRET_MIN_ANGLE
                || turretAngleDegrees > Constants.TURRET_MAX_ANGLE;
    }

    private ShotTargets calculateTargetsFromPose(ShootState state, PoseControlProfile profile, Pose2d robotPose) {
        double computedDistanceMeters = calculateDistanceMeters(state, robotPose, profile.targetPose);
        double anglerAngleDegrees = calculateAnglerAngleDegrees(state, computedDistanceMeters, profile);
        double shooterVelocity = calculateShooterVelocity(state, computedDistanceMeters, profile);
        double turretAngleDegrees = calculateTurretAngleDegrees(state, robotPose, profile);
        return new ShotTargets(anglerAngleDegrees, shooterVelocity, turretAngleDegrees, computedDistanceMeters, state != ShootState.AUTO_AIM,
                state != ShootState.AUTO_AIM, true);
    }

    private double calculateDistanceMeters(ShootState state, Pose2d robotPose, Pose2d targetPose) {
        double distance = robotPose.getTranslation()
                .getDistance(targetPose.getTranslation());
        if (state == ShootState.SHOOTING_HUB) {
            if (distance > Constants.MAX_HUB_DISTANCE) {
                return Constants.MAX_HUB_DISTANCE;
            } else if (distance < Constants.MIN_HUB_DISTANCE) {
                return Constants.MIN_HUB_DISTANCE;
            } else {
                return distance;
            }
        } else {
            return distance;
        }
    }

    private Pose2d robotPosePredictionCalculation(Pose2d targetPose, Pose2d robotPose) {
        double flightTime = calculateFlightTime(calculateDistanceMeters(ShootState.SHOOTING_HUB, robotPose, targetPose));
        Pose2d robotPoseTransform = new Pose2d(robotPose.getTranslation(), new Rotation2d());
        Pose2d predictedTransform = robotPoseTransform
                .plus(new Transform2d(
                        drivebase.getFieldVelocity().vxMetersPerSecond * flightTime,
                        drivebase.getFieldVelocity().vyMetersPerSecond * flightTime,
                        new Rotation2d()));
        Pose2d predictedPose = new Pose2d(predictedTransform.getTranslation(), robotPose.getRotation());
        if (Constants.DEBUG) {
            Logger.recordOutput("Predicted pose", predictedPose);
        }
        return predictedPose;
    }

    //Flight time derived from testing videos
    private double calculateFlightTime(double computedDistanceMeters) {
        return 0.208 * computedDistanceMeters + 0.647;
    }

    private double calculateAnglerAngleDegrees(ShootState state, double computedDistanceMeters, PoseControlProfile profile) {
        if (state == ShootState.SHOOTING_HUB || state == ShootState.AUTO_AIM) {
            double distance = (UnitConversion.METER_TO_FOOT * computedDistanceMeters) - Constants.COMPUTATED_DISTANCE_OFFSET;
            return 0.169 * distance * distance
                    - 1.73 * distance
                    + 20.4;
        }
        return profile.defaultAnglerAngleDegrees;
    }

    private double calculateShooterVelocity(ShootState state, double computedDistanceMeters, PoseControlProfile profile) {
        double distance = (UnitConversion.METER_TO_FOOT * computedDistanceMeters) - Constants.COMPUTATED_DISTANCE_OFFSET;
        if (state == ShootState.SHOOTING_HUB) {
            return (8.46 * distance * distance
                    - 237 * distance
                    - 1380);
        } else if (state == ShootState.SHUTTLING) {
            return (((-distance * distance) - 5 * distance) - 2800);
        }
        return profile.defaultShooterVelocityRpm;
    }

    private double calculateTurretAngleDegrees(ShootState state, Pose2d robotPose, PoseControlProfile profile) {
        if (state == ShootState.SHOOTING_HUB || state == ShootState.SHUTTLING) {
            return Math.floor(
                    Math.toDegrees(TurretCalculations.calculateTurretAngle(state, robotPose.getX(), robotPose.getY(),
                            robotPose.getRotation().getRadians(),
                            Robot.allianceColor().get() == DriverStation.Alliance.Blue, isadjustedPosition)));
        }
        return profile.defaultTurretAngleDegrees;
    }

    public void toggleAdjustedPosition(){
        if(isadjustedPosition){
            isadjustedPosition = false;
        }else{
            isadjustedPosition = true;
        }
    }
    // Getters for all the subsystems to set posistion.
    public double getTargetAnglerAngleDegrees() {
        return activeTargets.anglerAngleDegrees;
    }

    public double getTargetShooterVelocityRpm() {
        //return activeTargets.shooterVelocityRpm * 0.9; use in squishy mode
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

    public boolean canIntakeDeploy() {
        return activeTargets.intakeDeploy;
    }

    public void setActivatedShooting(boolean set) {
        driverActivatedShooting = set;
    }

    public boolean isActivatedShootingEnabled() {
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
        private final boolean intakeDeploy;

        private ShotTargets(
                double anglerAngleDegrees,
                double shooterVelocityRpm,
                double turretAngleDegrees,
                double distanceMeters,
                boolean feederSpin,
                boolean hopperSpin, boolean intakeDeploy) {
            this.anglerAngleDegrees = anglerAngleDegrees;
            this.shooterVelocityRpm = shooterVelocityRpm;
            this.turretAngleDegrees = turretAngleDegrees;
            this.distanceMeters = distanceMeters;
            this.feederSpin = feederSpin;
            this.hopperSpin = hopperSpin;
            this.intakeDeploy = intakeDeploy;
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
