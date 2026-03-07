package frc.robot.utils.math;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.constants.GameConstants;
import org.littletonrobotics.junction.Logger;

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


    // pan angle is what we are trying to found - the angle between the turret and the right side of the robot
    // panAngleUnadjusted gives the turret's pan angle assuming the robot is facing directly to the right 
    // basically, it doesn't account for the robot's rotation
    // pan angle is what we are trying to found - the angle between the turret and the right side of the robot
    // hubPosX and hubPosY are given values from the constants file -- gives the x and y positions of the hub (in meters)
    // turretPosX and turretPosY is the field position of the turret
    // robotPosX and robotPosY are given values from the robot pose, the center of the robot
    // robotRotation is the angle between the horizontal (by the alliance side chute) and the robot
    public static double calculateTurretAngle(double robotPosX, double robotPosY, double robotRotation, boolean isBlueAlliance) {
        
        // calculates the position of the turret with respect to the origin using the robot center 
        // and the constant distance between the robot center and the turret.
        double turretPosX = robotPosX + GameConstants.X_DISTANCE_BETWEEN_ROBOT_AND_TURRET;
        double turretPosY = robotPosY + GameConstants.Y_DISTANCE_BETWEEN_ROBOT_AND_TURRET;

        double hubPosX;
        double hubPosY;

        if (isBlueAlliance) {
            // hub position determined by which alliance robot is on
            hubPosX = GameConstants.BLUE_HUB_X_POSITION;
            hubPosY = GameConstants.BLUE_HUB_Y_POSITION;
        } else {
            hubPosX = GameConstants.RED_HUB_X_POSITION;
            hubPosY = GameConstants.RED_HUB_Y_POSITION;
        }
        Logger.recordOutput("Hub Pose", new Pose2d(new Translation2d(hubPosX,hubPosY), new Rotation2d())); 
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
         * pan angle, which is the proper angle of the turret adjusted for the robot's rotation.
         */
        double panAngle = panAngleUnadjusted - robotRotation;
        return panAngle;
    }

}
