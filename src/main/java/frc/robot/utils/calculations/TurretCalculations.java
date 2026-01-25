package frc.robot.utils.calculations;

import frc.robot.Constants;

public class TurretCalculations {

    /* 
    IMPORTANT: when using this class, make sure you always check that safeShooting = true
    and always use a buffer when alternating tilt angle (ask Joseph for more about that)
    */

    // The origin is defined as the corner on the alliance side of the field closest to the chute
    
    // All degrees are in radians, all distances are in meters, all velocities in m/s

    // Target values -- what we're trying to find
    private double tiltAngle;
    private double panAngle;
    private double shooterVelocity;

    // Given values -- from robot pose
    private double robotPosX;
    private double robotPosY;
    private double robotRotation;

    // Given values -- from Constants file
    private static final double hubHeight = Constants.HUB_HEIGHT;
    private static final double shooterHeight = Constants.SHOOTER_HEIGHT;
    private static final double hubPosX = Constants.HUB_X_POSITION;
    private static final double hubPosY = Constants.HUB_Y_POSITION;
    private static final double g = 9.81;

    // Experimental drag values -- from Constants file
    private static final double dragK = Constants.DRAG_K;
    private static final double dragQ = Constants.DRAG_Q;

    // Intermediate values;
    private double deltaDistance;
    private double deltaHeight;
    private double panAngleUnadjusted;
    private double idealShooterVelocity;

    // safety toggle
    private boolean safeShooting;

    public TurretCalculations(double robotPosX, double robotPosY, double robotRotation) {
        this.robotPosX = robotPosX;
        this.robotPosY = robotPosY;
        this.robotRotation = robotRotation;
        safeShooting = true;
        doTheMath();
    }

    private void doTheMath() {
     
        // Compute the change in height
        deltaHeight = hubHeight - shooterHeight;

        // Compute distance between hub and robot with Pythagorean Theorem
        deltaDistance = Math.sqrt(Math.pow(hubPosY - robotPosY, 2) 
        + Math.pow(hubPosX - robotPosX, 2));

        // find unadjusted pan angle
        panAngleUnadjusted = Math.atan2(hubPosY - robotPosY, hubPosX - robotPosX);

        // adjust pan angle to account for robot rotation
        panAngle = panAngleUnadjusted - robotRotation;

        // find correct angle for the given level
        tiltAngle = getAngle();

        // Compute ideal shooter speed
        idealShooterVelocity = Math.sqrt(
            ( 
                (g) * Math.pow(deltaDistance, 2) * Math.pow(1 / Math.cos(tiltAngle), 2)
            )
            /
            2 * (
                deltaDistance * Math.tan(tiltAngle) - deltaHeight
            )
        );

        // account for drag
        shooterVelocity = idealShooterVelocity * (dragK + dragQ * deltaDistance);
        
    }

    private double getAngle() {
        if ( (deltaDistance < .75) || (deltaDistance > 8)) {
            safeShooting = false;
        }
        else if ( (deltaDistance < 3.5) ) {
            return 1.39626;
        }
        return 1.13446;
    }

    public double getTiltAngle() {
        return tiltAngle;
    }

    public double getPanAngle() {
        return panAngle;
    }

    public double getShooterSpeed() {
        return shooterVelocity;
    }

    public boolean getSafeShooting() {
        return safeShooting;
    }

}
