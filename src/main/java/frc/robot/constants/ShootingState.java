package frc.robot.constants;

public class ShootingState {

    public enum ShootState {

        STOPPED,        // Shooting motor does not run
        MANUAL_1,       // Shoot from a known location
        MANUAL_2,       // Shoot from another known location
        SHOOTING_HUB,   // Auto-aim into the hub
        SHUTTLING       // Auti aim into alliance zone

    }

    private ShootState shootState;

    public ShootingState(ShootState shootState) {
        this.shootState = shootState;
    }

    public ShootState getShootState() {
        return shootState;
    }

    public void setShootState(ShootState newState) {
        shootState = newState;
    }

}
