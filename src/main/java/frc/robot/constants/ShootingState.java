package frc.robot.constants;

public class ShootingState {

    public enum ShootState {

        STOPPED,        // Shooting motor does not run
        FIXED,          // Shooting motor runs at a constant speed
        SHOOTING_HUB,   // Used for shooting into the hub
        SHUTTLING       // Used for shuttling into our alliance zone

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
