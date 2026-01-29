package frc.robot.utils.calculations;

import frc.robot.Constants;

public class TurretCalculations {

    /* 
     * 
     * This is a utility class that takes the robot position and provides the pan angle. 
     * The pan angle is the angle that the turret should be from the horizontal in radians.
     * For example, when the turret is facing directly forward (with respect to the robot), the pan
     * angle would be pi/2 radians (1.57). 
     * 
     * This class takes the robot's x and y positions: robotPosX and robotPosY. These are the
     * distances (in meters) from the origin. The origin is defined as the corner on the alliance 
     * side of the field closest to the chute.

     * 
     */

    
    // All degrees are in radians, all distances are in meters, all velocities in m/s

    // Target values -- what we're trying to find
    private double panAngle; // angle between the turret and the right side of the robot

    // Given values -- from robot pose
    private double robotPosX;
    private double robotPosY;
    private double robotRotation; // angle between the horizontal (by the alliance side chute) and the robot

    // Given values -- from Constants file
    // gives the x and y positions of the hub (in meters)
    private static final double hubPosX = Constants.HUB_X_POSITION;
    private static final double hubPosY = Constants.HUB_Y_POSITION;

    // gives the turret's pan angle assuming the robot is facing directly to the right 
    // basically, it doesn't account for the robot's rotation
    private double panAngleUnadjusted;

    public TurretCalculations(double robotPosX, double robotPosY, double robotRotation) {
        this.robotPosX = robotPosX;
        this.robotPosY = robotPosY;
        this.robotRotation = robotRotation;
        doTheMath();
    }

    private void doTheMath() {

        /*
         * This finds the unadjusted pan angle (assuming there is no robot rotation) using
         * trigonometry. We take the arctangent of the y-distance beween the robot and the hub
         * and the x-distance between the robot and the hub, giving us the unadjusted pan
         * angle. The function atan2 ensures the sign of the angle is correct based on the signs
         * of the input numbers.
         * 
         */
        panAngleUnadjusted = Math.atan2(hubPosY - robotPosY, hubPosX - robotPosX);

        /*
         * Adjusts the pan angle to account for the robot's current rotation. We subtract the
         * angle of the robot's rotation from the unadjusted angle of the turret to find the 
         * pan angle, which is the proper angle of the turret adjusted for the robot's rotation.
         */
        panAngle = panAngleUnadjusted - robotRotation;
        
    }

    // returns the pan angle
    public double getPanAngle() {
        return panAngle;
    }

}
