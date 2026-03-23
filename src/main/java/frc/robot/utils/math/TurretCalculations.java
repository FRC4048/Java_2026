package frc.robot.utils.math;


import org.littletonrobotics.junction.Logger;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.constants.Constants;
import frc.robot.constants.GameConstants;
import frc.robot.constants.enums.ShootingState.ShootState;

public class TurretCalculations {

    /* 
     * 
     * This is a utility class that takes the robot position and provides the pan angle. 
     * The pan angle is the angle that the turret should be from the horizontal in radians.
     * For example, when the turret is facing directly forward (with respect to the robot), the pan
     * angle would be pi/2 radians (1.57). 
     * 
     * This class takes the robot's x and y positions: robotPosX and robotPosY. These are the
     * distances (in meters) from the origin. The origin is defined as the bottom right corner 
     * of the blue alliance.
     * 
     */

    // All angles are in radians, all distances are in meters, all velocities in m/s


    // Calculates the turret angle needed to point at the hub
    // Turret 0 degrees is back of the robotand the angle increases when turning clockwise.
    // WPILIB field: origin is blue right corner. +X is forward, +y is left, +rotation is CCW
    // all positions are in meters
    // robotRotation is in radians
    // Returns - turret angle in radians
    public static double calculateTurretAngle(ShootState state, double robotPosX, double robotPosY, double robotRotation, boolean isBlueAlliance) {
        
        // Get turret offset from robot center
        double turretOffsetX = GameConstants.X_DISTANCE_BETWEEN_ROBOT_AND_TURRET;
        double turretOffsetY = GameConstants.Y_DISTANCE_BETWEEN_ROBOT_AND_TURRET;

        // Rotate the offset by robot rotation (offset rotates with robot)
        double rotatedOffsetX = turretOffsetX * Math.cos(robotRotation) - turretOffsetY * Math.sin(robotRotation);
        double rotatedOffsetY = turretOffsetX * Math.sin(robotRotation) + turretOffsetY * Math.cos(robotRotation);

        // Calculate actual turret position on field
        double turretPosX = robotPosX + rotatedOffsetX;
        double turretPosY = robotPosY + rotatedOffsetY;

        double hubPosX;
        double hubPosY;
    if(state == ShootState.SHOOTING_HUB) {
        if (isBlueAlliance) {
            // hub position determined by which alliance robot is on
            hubPosX = Constants.BLUE_HUB_X_POSITION;
            hubPosY = Constants.BLUE_HUB_Y_POSITION;
        } else {
            hubPosX = Constants.RED_HUB_X_POSITION;
            hubPosY = Constants.RED_HUB_Y_POSITION;
        }
        }else{
            //Sets the hubPosX to be either on the blue or red side 
            //Sets the hubPoseY to be lower or upper depending on the Y position of the robot
            hubPosX = isBlueAlliance ? Constants.BLUE_HUB_X_POSITION : Constants.RED_HUB_X_POSITION;
            hubPosY = robotPosY > (Constants.FIELD_WIDTH / 2) ? Constants.SHUTTLING_TARGET_HIGHER_Y_POSITION : Constants.SHUTTLING_TARGET_LOWER_Y_POSITION;
        } 
            /*
             * This finds the unadjusted pan angle (assuming there is no robot rotation) using
             * trigonometry. We take the arctangent of the y-distance beween the robot and the hub
             * and the x-distance between the robot and the hub, giving us the unadjusted pan
             * angle. The function atan2 ensures the sign of the angle is correct based on the signs
             * of the input numbers.
             * 
             */
            double panAngleUnadjusted = Math.atan2(hubPosY - turretPosY, hubPosX - turretPosX);

        /*
         * Adjusts the pan angle to account for the robot's current rotation. We subtract the
         * angle of the robot's rotation from the unadjusted angle of the turret to find the 
         * pan angle, which is the proper angle of the turret adjusted for the robot's rotation
         * and the fact that the turret 0 angle in in the back of the robot.
         */
        double panAngle = panAngleUnadjusted - (robotRotation + Math.PI);

        // normalize angle between -PI and PI
        double normalizedPanAngle = panAngle - 2 * Math.PI * Math.floor((panAngle + Math.PI) / (2 * Math.PI));
        return normalizedPanAngle;
    }

}
