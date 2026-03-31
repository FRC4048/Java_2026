package frc.robot.subsystems;

import edu.wpi.first.math.geometry.*;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Robot;
import frc.robot.constants.enums.ShootingState;
import frc.robot.utils.math.TurretCalculations;

import java.util.ArrayList;

import org.dyn4j.UnitConversion;
import org.littletonrobotics.junction.Logger;

import frc.robot.RobotContainer;
import frc.robot.commands.intakeDeployment.ToggleDeployment;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.constants.Constants;
import frc.robot.constants.enums.DeploymentState;
import frc.robot.constants.enums.ShootingState.ShootState;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import swervelib.simulation.ironmaple.simulation.SimulatedArena;
import swervelib.simulation.ironmaple.simulation.seasonspecific.rebuilt2026.RebuiltFuelOnFly;
import swervelib.simulation.ironmaple.utils.FieldMirroringUtils;

import javax.naming.ldap.Control;

public class ControllerSubsystem extends SubsystemBase {

    private static final double STOP_DELAY_SECONDS = 0.5;
    private static final double SHOOT_DELAY_SECONDS = 0.5;

    // Placeholder target poses until real field target values are finalized
    private static final Pose2d BLUE_HUB_TARGET_POSE = Constants.BLUE_HUB_POS;
    private static final Pose2d RED_HUB_TARGET_POSE = Constants.RED_HUB_POS;
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
            false,false);
    //3.25 meters away
    private static final ShotTargets FIXED_TARGETS = new ShotTargets(21.16, -2945.21, 0, 3.25, true, true,true);
    private static final ShotTargets FIXED_2_TARGETS = new ShotTargets(22.0, 180.0, -5.0, 0.0, true, true,true);

    // Placeholder pose-driven profiles.
    private static final PoseControlProfile BLUE_HUB_PROFILE = new PoseControlProfile(BLUE_HUB_TARGET_POSE, 32.0, 230.0,
            14.0);
    private static final PoseControlProfile RED_HUB_PROFILE = new PoseControlProfile(RED_HUB_TARGET_POSE, 32.0, 230.0,
            14.0);
    private static final PoseControlProfile RED_SHUTTLE_PROFILE = new PoseControlProfile(RED_HUB_TARGET_POSE, 45.0, 90.0,
            -14.0);
    private static final PoseControlProfile BLUE_SHUTTLE_PROFILE = new PoseControlProfile(BLUE_HUB_TARGET_POSE, 45.0, 90.0,
            -14.0);

    private final SwerveSubsystem drivebase;
    private final IntakeDeployerSubsystem intakeDeployer;
    private final RobotContainer robotContainer;
    private final Timer stopDelayTimer = new Timer();
    private final Timer shootDelayTimer = new Timer();

    private ShootState previousState;
    private ShotTargets activeTargets;
    private boolean driverActivatedShooting = false;
    private final TurretSubsystem turret;
    private double lastShot;

    public ControllerSubsystem(SwerveSubsystem drivebase, IntakeDeployerSubsystem intakeDeployer, RobotContainer robotContainer, TurretSubsystem turret) {
        this.drivebase = drivebase;
        this.robotContainer = robotContainer;
        this.previousState = getCurrentShootState();
        this.activeTargets = STOPPED_TARGETS;
        this.intakeDeployer = intakeDeployer;
        this.turret = turret;

        SmartDashboard.putNumber(MANUAL_POSE_X_KEY, 0.0);
        SmartDashboard.putNumber(MANUAL_POSE_Y_KEY, 0.0);
        SmartDashboard.putNumber(MANUAL_POSE_R_KEY, 0.0);
    }

    @Override
    public void periodic() {
        Pose2d robotPose = getRobotPose();
        ShootState currentState = getCurrentShootState();
        ChassisSpeeds robotSpeeds = getRobotSpeeds();
        updateStopDelayState(currentState);
        updateShootDelayState(currentState);
        updateTargets(currentState, robotPose, robotSpeeds);
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
        if (Logger.getTimestamp()-lastShot>1000000/Constants.PIECES_PER_SECOND) {
            lastShot = Logger.getTimestamp();
            if (robotContainer.getShootingState().getShootState() != ShootingState.ShootState.STOPPED) {
                Translation2d target = (Robot.allianceColor().get().equals(DriverStation.Alliance.Blue)? BLUE_HUB_TARGET_POSE: RED_HUB_TARGET_POSE).getTranslation();
                SimulatedArena.getInstance()
                        .addGamePieceProjectile(new RebuiltFuelOnFly(
                                drivebase.getSimulationPose().get().getTranslation(),
                                new Translation2d(), // shooter offet from center
                                drivebase.getFieldVelocity(),
                                Rotation2d.fromDegrees(turret.getLastAngle()-180).plus(drivebase.getHeading()),
                                Units.Meters.of(0.4), // initial height of the ball, in meters
                                Units.MetersPerSecond.of(calculateShotVelocity(activeTargets.shooterVelocityRpm)), // initial velocity, in m/s
                                Units.Degrees.of(90-activeTargets.anglerAngleDegrees)) // shooter angle
                                .withTargetPosition(()-> new Translation3d(target.getX(), target.getY(), 1.82))
                                        // Set the tolerance: x: ±0.5m, y: ±1.2m, z: ±0.3m (this is the size of the speaker's "mouth")
                                .withTargetTolerance(new Translation3d(0.4, 0.4, 0.15))
                                .disableBecomesGamePieceOnFieldAfterTouchGround()
                                .withProjectileTrajectoryDisplayCallBack(
                                        (poses) -> Logger.recordOutput("successfulShotsTrajectory", poses.toArray(Pose3d[]::new)),
                                        (poses) -> Logger.recordOutput("missedShotsTrajectory", poses.toArray(Pose3d[]::new))));

            }
        }
    }

    private ShootState getCurrentShootState() {
        return robotContainer.getShootingState().getShootState();
    }
    private double calculateShotVelocity(double rpm) {
        double gearRatio = 1; //MotorRotations/WheelRotations
        double radiusWheel = 0.123825;
        double efficiencyConstant = 0.42;
        return -rpm*Math.PI/gearRatio/60*radiusWheel*efficiencyConstant;
    }
    private ChassisSpeeds getRobotSpeeds() {
        return drivebase.getFieldVelocity();
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

    private void updateTargets(ShootState state, Pose2d robotPose, ChassisSpeeds robotSpeeds) {
        if(!activeTargets.intakeDeploy && intakeDeployer.getDeploymentState() == DeploymentState.DOWN){
            new ToggleDeployment(intakeDeployer, this).schedule();
        }
        switch (state) {
            case STOPPED -> updateStoppedTargets();
            case FIXED -> useShotTargets(FIXED_TARGETS);
            case FIXED_2 -> useShotTargets(FIXED_2_TARGETS);
            case SHOOTING_HUB -> {
                if (Robot.allianceColor().isEmpty()) {
                    // No color, do nothing...
                    useShotTargets(FIXED_TARGETS);
                } else if (Robot.allianceColor().get().equals(DriverStation.Alliance.Blue)) {
                    useShotTargets(calculateTargetsFromPose(state, BLUE_HUB_PROFILE, robotPose, robotSpeeds));
                } else if (Robot.allianceColor().get().equals(DriverStation.Alliance.Red)) {
                    useShotTargets(calculateTargetsFromPose(state, RED_HUB_PROFILE, robotPose, robotSpeeds));
                } else {
                    // Unknown color, do nothing...
                    useShotTargets(FIXED_TARGETS);
                }
            }
            case SHUTTLING -> {
                if (Robot.allianceColor().isEmpty()) {
                    useShotTargets(FIXED_TARGETS);
                } else if (Robot.allianceColor().get().equals(DriverStation.Alliance.Blue)) {
                    useShotTargets(calculateTargetsFromPose(state,BLUE_SHUTTLE_PROFILE, robotPose, robotSpeeds));
                } else if (Robot.allianceColor().get().equals(DriverStation.Alliance.Red)) {
                    useShotTargets(calculateTargetsFromPose(state, RED_SHUTTLE_PROFILE, robotPose, robotSpeeds));
                } else {
                    useShotTargets(FIXED_TARGETS);
                }
            }
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
            shooterVelocityRpm = Constants.TURRET_OUT_OF_RANGE_FLOP_RPM;
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
                activeTargets.anglerAngleDegrees,
                shooterVelocityRpm, // Shooter starts half a second before everything else
                activeTargets.turretAngleDegrees,
                activeTargets.distanceMeters,
                false,
                false,
                activeTargets.intakeDeploy);

        }

    }

    private boolean isTurretTargetOutOfRange(double turretAngleDegrees) {
        return turretAngleDegrees < Constants.TURRET_MIN_ANGLE
                || turretAngleDegrees > Constants.TURRET_MAX_ANGLE;
    }

    private ShotTargets calculateTargetsFromPose(ShootState state,PoseControlProfile profile, Pose2d robotPose, ChassisSpeeds robotSpeeds) {
        Twist2d momentumAdjustment = getMomentumAdjustment(robotPose, Constants.ACCOUNT_FOR_ANGULAR_MOMENTUM, robotSpeeds,
                equalizeFlightTime(calculateDistanceMeters(state, robotPose, profile.targetPose), robotPose, profile.targetPose, robotSpeeds));
        PoseControlProfile adjustedProfile = new PoseControlProfile(profile.targetPose, profile.defaultAnglerAngleDegrees, profile.defaultShooterVelocityRpm, profile.defaultTurretAngleDegrees);
        adjustedProfile.targetPose = profile.targetPose.exp(momentumAdjustment);
        Logger.recordOutput("adjustedAimPoint", adjustedProfile.targetPose);
        double computedDistanceMeters = calculateDistanceMeters(state, robotPose, adjustedProfile.targetPose);
        boolean shootHub = profile == BLUE_HUB_PROFILE || profile == RED_HUB_PROFILE;
        double anglerAngleDegrees = calculateAnglerAngleDegrees(state, computedDistanceMeters, adjustedProfile);
        double shooterVelocity = calculateShooterVelocity(state, computedDistanceMeters, adjustedProfile);
        double turretAngleDegrees = calculateTurretAngleDegrees(state, robotPose, adjustedProfile);
        return new ShotTargets(anglerAngleDegrees, shooterVelocity, turretAngleDegrees, computedDistanceMeters, true,
                true, true);
    }

    private double calculateDistanceMeters(ShootState state,Pose2d robotPose, Pose2d targetPose) {
        double distance = robotPose.getTranslation()
                .getDistance(targetPose.getTranslation());
        if(state == ShootState.SHOOTING_HUB){
            if (distance > Constants.MAX_HUB_DISTANCE) {
                return Constants.MAX_HUB_DISTANCE;
            } else if (distance < Constants.MIN_HUB_DISTANCE) {
                return Constants.MIN_HUB_DISTANCE;
            } else {
                return distance;
            }
        }else{
            return distance;
        }
    }

    private Pose2d robotPosePredictionCalculation(Pose2d targetPose, Pose2d robotPose) {
        double flightTime = calculateFlightTime(calculateDistanceMeters(ShootState.SHOOTING_HUB,robotPose,targetPose));
        Pose2d robotPoseTransform = new Pose2d(robotPose.getTranslation(), new Rotation2d());
        Pose2d predictedTransform = robotPoseTransform
                        .plus(new Transform2d(
                        drivebase.getFieldVelocity().vxMetersPerSecond * flightTime,
                        drivebase.getFieldVelocity().vyMetersPerSecond * flightTime,
                        new Rotation2d()));
        Pose2d predictedPose = new Pose2d(predictedTransform.getTranslation(), robotPose.getRotation());
        if(Constants.DEBUG){
            Logger.recordOutput("Predicted pose", predictedPose);
        }
        return predictedPose;
    }
    // Linear regression through 3 static shot distance vs time points.
    private double calculateFlightTime(double computedDistanceMeters) {
        return 0.208*computedDistanceMeters + 0.647;
    }

    // Since t(x)=mx+b (previous function), we can solve for x
    // t = m*(d - v_robot * t)+b
    // t + v_robot * t = m * d + b
    // t = (m * d + b) / (v_robot + 1)
    private double equalizeFlightTime(double initialDistanceMeters, Pose2d robotPose, Pose2d target, ChassisSpeeds robotSpeeds) {
        return (0.208*initialDistanceMeters+0.647)/(0.208*ChassisSpeeds.fromFieldRelativeSpeeds(robotSpeeds, target.getTranslation().minus(robotPose.getTranslation()).getAngle()).vxMetersPerSecond+1);
    }

    private double calculateAnglerAngleDegrees(ShootState state, double computedDistanceMeters, PoseControlProfile profile) {
        double distance = (UnitConversion.METER_TO_FOOT * computedDistanceMeters) - Constants.COMPUTATED_DISTANCE_OFFSET;
        return 0.169 * distance * distance
                    - 1.73 * distance +20.4;
    }

    private double calculateShooterVelocity(ShootState state, double computedDistanceMeters, PoseControlProfile profile) {
        double distance = (UnitConversion.METER_TO_FOOT * computedDistanceMeters) - Constants.COMPUTATED_DISTANCE_OFFSET;
        if (state == ShootState.SHOOTING_HUB) {
            return (8.46 * distance * distance
                    - 237 * distance
                    - 1380);
        }else if(state == ShootState.SHUTTLING){
            return (((-distance*distance) - 5 * distance) - 2800);
        }
        System.out.println("usingDefault");
        return profile.defaultShooterVelocityRpm;
    }

    private double calculateTurretAngleDegrees(ShootState state, Pose2d robotPose, PoseControlProfile profile) {
        if(state == ShootState.SHOOTING_HUB || state == ShootState.SHUTTLING){
            return Math.floor(
                    Math.toDegrees(TurretCalculations.calculateTurretAngle(robotPose,profile.targetPose,
                            Robot.allianceColor().get() == DriverStation.Alliance.Blue)));
        }
            return profile.defaultTurretAngleDegrees;
    }
    public Twist2d getMomentumAdjustment(Pose2d robotPose, boolean useAngularMomentumAdjustment, ChassisSpeeds robotSpeeds, double timeOfFlight) {
        // calculate the change per second of the turret's position relative to the center due to robot rotation. This
        // is basically angular speed. Result is in robot coordinate system.
        ChassisSpeeds adjustSpeeds;
        if (useAngularMomentumAdjustment) {
            Translation2d angularVelocityAdjustment = Constants.TURRET_OFFSET.times(robotSpeeds.omegaRadiansPerSecond).getTranslation();


            // take the robot's current speed (relative to field)
            // convert the turret's angular velocity from robot-relative to field-relative
            // since the rotation velocity is perpendicular to the robot x-axis, so we need to rotate it by 90 degrees
            // which is why we use -Y,X instead of X,Y for the conversion.
            // add the two to get the effective speed of the turret/projectile.
            adjustSpeeds = (new ChassisSpeeds(robotSpeeds.vxMetersPerSecond, robotSpeeds.vyMetersPerSecond, 0)).plus
                    (ChassisSpeeds.fromRobotRelativeSpeeds(-angularVelocityAdjustment.getY(), angularVelocityAdjustment.getX(), 0.0, robotPose.getRotation()));
        } else {
            adjustSpeeds = (new ChassisSpeeds(robotSpeeds.vxMetersPerSecond, robotSpeeds.vyMetersPerSecond, 0));
        }
        // convert the effective speed to a twist (displacement over time)
        // positive timeOfFlight tells us how much we move
        // negative timeOfFlight tells us how much we need to move to compensate for our movement
        return adjustSpeeds.toTwist2d(-timeOfFlight);
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
        private Pose2d targetPose;
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
