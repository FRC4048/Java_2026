package frc.robot.utils.math;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.robot.constants.GameConstants;

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
    public static double calculateTurretAngle(Pose2d robotPos,Pose2d adjTargetPose, boolean isBlueAlliance) {

        Pose2d turretPos = robotPos.transformBy(GameConstants.TURRET_OFFSET);

        Pose2d hubPos = adjTargetPose;

        /*
         * This finds the unadjusted pan angle (assuming there is no robot rotation) using
         * trigonometry. We take the arctangent of the y-distance beween the robot and the hub
         * and the x-distance between the robot and the hub, giving us the unadjusted pan
         * angle. The function atan2 ensures the sign of the angle is correct based on the signs
         * of the input numbers.
         * 
         */
        Translation2d hubRelativeToRobot = hubPos.relativeTo(turretPos).getTranslation();
        double panAngleUnadjusted = hubRelativeToRobot.getAngle().getRadians();

        /*
         * Adjusts the pan angle to account for the robot's current rotation. We subtract the
         * angle of the robot's rotation from the unadjusted angle of the turret to find the 
         * pan angle, which is the proper angle of the turret adjusted for the robot's rotation
         * and the fact that the turret 0 angle in in the back of the robot.
         */
        double panAngle = panAngleUnadjusted - (robotPos.getRotation().getRadians() + Math.PI);

        // normalize angle between -PI and PI
        double normalizedPanAngle = panAngle - 2 * Math.PI * Math.floor((panAngle + Math.PI) / (2 * Math.PI));
        return normalizedPanAngle;
    }

}
