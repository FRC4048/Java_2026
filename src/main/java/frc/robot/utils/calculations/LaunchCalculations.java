package frc.robot.utils.calculations;

import frc.robot.constants.GameConstants;

public class LaunchCalculations {

    /*
     * The intention of this class is to provide the launch angle and initial velocity given
     * the robot's distance from the hub. We will test this on the robot, and if the physics
     * don't work out practically, we will use this class to give initial test values for the
     * robot. We will run these test values to obtain 4 points experimentally. Then, we will
     * use interpolation to generate a function that will then give the correct launch angle
     * and velocity according to our current distance from the robot. 
     */

    // The origin is defined as the bottom right corner of the blue alliance
    
    // All angles are in radians, all distances are in meters, all velocities in m/s

    public static double calculateShooterVelocity(double robotPosX, double robotPosY, double launchAngle, boolean isBlueAlliance) {
        
        // Target value -- what we're trying to find
        double shooterVelocity; // Initial velocity of the shooter -- related to flywheel speed

        // calculates the position of the turret with respect to the origin using the robot center 
        // and the constant distance between the robot center and the turret.
        double turretPosX = robotPosX + GameConstants.X_DISTANCE_BETWEEN_ROBOT_AND_TURRET;
        double turretPosY = robotPosY + GameConstants.Y_DISTANCE_BETWEEN_ROBOT_AND_TURRET;

        // Given values -- from Constants file
        double hubHeight = GameConstants.HUB_HEIGHT; // height of the top of the hub
        double shooterHeight = GameConstants.SHOOTER_HEIGHT; // height of shooter
        double hubPosX; 
        double hubPosY;

        double deltaDistance; // distance between the robot and the hub
        double deltaHeight; // height difference between shooter and hub
        
        if (isBlueAlliance) {
            // hub position determined by which alliance robot is on
            hubPosX = GameConstants.BLUE_HUB_X_POSITION;
            hubPosY = GameConstants.BLUE_HUB_Y_POSITION;
        }
        else {
            hubPosX = GameConstants.RED_HUB_X_POSITION;
            hubPosY = GameConstants.RED_HUB_Y_POSITION;
        }
        
        // Compute distance between hub and robot with Pythagorean Theorem
        deltaDistance = Math.sqrt(Math.pow(hubPosY - turretPosY, 2) 
        + Math.pow(hubPosX - turretPosX, 2));

        // Compute the change in height between the shooter and the hub
        deltaHeight = hubHeight - shooterHeight;

        /*
        *  Compute the shooter velocity. It is a physics formula that computes the initial velocity given
        *  the launch angle, the distance between the robot and the hub, the height change, and
        *  the gravitational constant g. This formula has been tested in projectile motion simulators.
        *  It starts with kinematic equations, then you set up a system of two of the kinematic equationsm
        *  eliminate time, and solve for the initial velocity.
        */
        shooterVelocity = Math.sqrt(
            ( 
                (GameConstants.GRAVITY) * Math.pow(deltaDistance, 2) * Math.pow(1 / Math.cos(launchAngle), 2)
            )
            /
            2 * (
                deltaDistance * Math.tan(launchAngle) - deltaHeight
            )
        );

        return shooterVelocity;

    }

}
