package frc.robot.commands.angler;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.constants.Constants;
import frc.robot.constants.ShootingState;
import frc.robot.constants.ShootingState.ShootState;
import frc.robot.subsystems.AnglerSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

import java.util.function.Supplier;

/**
 * Default command that aims the angler based on robot pose and shooting state.
 *
 * NOTE: All numbers here are placeholders for initial testing and should be
 * tuned on the real robot.
 */
public class AimAngler extends LoggableCommand {

    private final AnglerSubsystem angler;
    private final Supplier<Pose2d> poseSupplier;
    private final ShootingState shootingState;

    //TODO: Test and change numbers for real robot
    
    // Where we want to aim relative to the field for each shooting mode.
    private static final Pose2d HUB_TARGET_POSE = new Pose2d(4.0, 4.0, Rotation2d.fromDegrees(0));
    private static final Pose2d SHUTTLE_TARGET_POSE = new Pose2d(1.0, 7.0, Rotation2d.fromDegrees(0));

    private static final String MANUAL_POSE_X_KEY = "angler/ManualPoseX";
    private static final String MANUAL_POSE_Y_KEY = "angler/ManualPoseY";
    private static final String CALC_ROTATIONS_KEY = "angler/CalculatedRotations";
    private static final String CALC_DISTANCE_KEY = "angler/CalculatedDistanceMeters";
    private static final String USING_MANUAL_POSE_KEY = "angler/UsingManualPose";

    private static final AimConfig HUB_CONFIG = new AimConfig(
            HUB_TARGET_POSE,
            0.00,
            6,
            Constants.ANGLER_LOW_ROTATIONS,
            Constants.ANGLER_HIGH_ROTATIONS);

    private static final AimConfig SHUTTLE_CONFIG = new AimConfig(
            SHUTTLE_TARGET_POSE,
            0,
            6,
            Constants.ANGLER_LOW_ROTATIONS,
            Constants.ANGLER_HIGH_ROTATIONS);

    public AimAngler(AnglerSubsystem angler, Supplier<Pose2d> poseSupplier, ShootingState shootingState) {
        this.angler = angler;
        this.poseSupplier = poseSupplier;
        this.shootingState = shootingState;
        addRequirements(angler);

        SmartDashboard.putNumber(MANUAL_POSE_X_KEY, 0.0);
        SmartDashboard.putNumber(MANUAL_POSE_Y_KEY, 0.0);
    }

    @Override
    public void execute() {
        Pose2d robotPose = poseSupplier.get();
        boolean usingManual = shouldUseManual(robotPose);
        if (usingManual) {
            robotPose = getManualPose();
        }

        SmartDashboard.putBoolean(USING_MANUAL_POSE_KEY, usingManual);
        ShootState state = shootingState.getShootState();
        handleState(state, robotPose);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    private void handleState(ShootState state, Pose2d robotPose) {
        switch (state) {
            case STOPPED -> {
            }
            case FIXED -> angler.setPosition(Constants.ANGLER_FIXED_ROTATIONS);
            case SHOOTING_HUB -> setAimFromConfig(HUB_CONFIG, robotPose);
            case SHUTTLING -> setAimFromConfig(SHUTTLE_CONFIG, robotPose);
        }
    }

    private void setAimFromConfig(AimConfig config, Pose2d robotPose) {
        double distanceMeters = robotPose.getTranslation().getDistance(config.targetPose.getTranslation());
        double rotations = config.baseRotations + (config.rotationsPerMeter * distanceMeters);
        double clampedRotations = MathUtil.clamp(rotations, config.minRotations, config.maxRotations);

        SmartDashboard.putNumber(CALC_DISTANCE_KEY, distanceMeters);
        SmartDashboard.putNumber(CALC_ROTATIONS_KEY, clampedRotations);
        angler.setPosition(clampedRotations);
    }

    private boolean shouldUseManual(Pose2d robotPose) {
        return (Constants.currentMode == Constants.Mode.SIM) || Constants.TESTBED;
    }

    private Pose2d getManualPose() {
        double x = SmartDashboard.getNumber(MANUAL_POSE_X_KEY, 0.0);
        double y = SmartDashboard.getNumber(MANUAL_POSE_Y_KEY, 0.0);
        return new Pose2d(x, y, Rotation2d.fromDegrees(0));
    }

    private static final class AimConfig {
        private final Pose2d targetPose;
        private final double baseRotations; //The baseline setpoint when the robot is right at the target
        private final double rotationsPerMeter; //How much to change the angler setpoint per meter of distance from targetpose
        private final double minRotations; //The min allowed setpoint (should allign with rev limit switch)
        private final double maxRotations; //The max allowed setpoint (should allign with forw limit switch)

        private AimConfig(
                Pose2d targetPose,
                double baseRotations,
                double rotationsPerMeter,
                double minRotations,
                double maxRotations) {
            this.targetPose = targetPose;
            this.baseRotations = baseRotations;
            this.rotationsPerMeter = rotationsPerMeter;
            this.minRotations = minRotations;
            this.maxRotations = maxRotations;
        }
    }
}
