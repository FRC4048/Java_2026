package frc.robot.commands.turret;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.constants.GameConstants;
import frc.robot.constants.enums.ShootingState;
import frc.robot.constants.enums.ShootingState.ShootState;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;
import frc.robot.utils.math.TurretCalculations;
import java.util.function.Supplier;

/**
 * Default command that aims the turret based on robot pose and shooting state.
 *
 */
public class AimTurret extends LoggableCommand {

    private final TurretSubsystem turret;
    private final Supplier<Pose2d> poseSupplier;
    private final ShootingState shootingState;
    private final boolean isBlueAlliance;

    public AimTurret(TurretSubsystem turret, Supplier<Pose2d> poseSupplier, ShootingState shootingState) {
        this.turret = turret;
        this.poseSupplier = poseSupplier;
        this.shootingState = shootingState;
        this.isBlueAlliance = isBlue();
        addRequirements(turret);
    }

    @Override
    public void execute() {
        Pose2d robotPose = poseSupplier.get();
        ShootState state = shootingState.getShootState();
        handleState(state, robotPose, isBlueAlliance);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    private void handleState(ShootState state, Pose2d robotPose, boolean isBlueAlliance) {
        switch (state) {
            case STOPPED: turret.stopMotors();
            // set to fixed turret angle at the various fixed states
            case FIXED: new SetTurretAngle(turret, GameConstants.FIXED_TURRET_ANGLE_1);
            case FIXED_2: new SetTurretAngle(turret, GameConstants.FIXED_TURRET_ANGLE_2);
            // auto aim to hub
            case SHOOTING_HUB: AutoShoot(turret, robotPose, isBlueAlliance);
            // auto aim to shuttle site
            case SHUTTLING: ShuttleShoot(turret, robotPose, isBlueAlliance);
        }
    }

    /** Moves turret to position automatically calculated based on angle from the hub.
     * 
     * @param turret
     * @param robotPose
     * @param isBlueAlliance
     */
    private void AutoShoot(TurretSubsystem turret, Pose2d robotPose, boolean isBlueAlliance) {
        double targetAngle = TurretCalculations.calculateTurretAngle(robotPose.getX(), robotPose.getY(), robotPose.getRotation().getRadians(), isBlueAlliance);
        new SetTurretAngle(turret, targetAngle);
    }

    /** Moves turret to shuttling position based on angle from shuttling position
     * 
     * @param turret
     * @param robotPose
     * @param isBlueAlliance
      */
    private void ShuttleShoot(TurretSubsystem turret, Pose2d robotPose, boolean isBlueAlliance) {
        double targetAngle = TurretCalculations.calculateTurretShuttleAngle(robotPose.getX(), robotPose.getY(), robotPose.getRotation().getRadians(), isBlueAlliance);
        new SetTurretAngle(turret, targetAngle);
    }

     /**
     * Checks if the alliance is blue, defaults to false if alliance isn't available.
     *
     * @return true if the blue alliance, false if red. Defaults to false if none is available.
     */
    private boolean isBlue() {
        var alliance = DriverStation.getAlliance();
        return alliance.isPresent() ? alliance.get() == DriverStation.Alliance.Blue : false;
    }

}