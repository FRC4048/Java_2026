package frc.robot.constants.enums;

public class ShootingState {

    public enum ShootState {

        STOPPED,        // Shooting motor does not run
        FIXED,          // Shoot from a known location
        FIXED_2,        // Shoot from another known location
        SHOOTING_HUB,   // Auto-aim into the hub
        SHUTTLING,     // Auto-aim into alliance zone
        AUTO_AIM        //auto aim without hopper and feeder

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
