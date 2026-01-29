package frc.robot.utils.calculations;

import frc.robot.Constants;

public class LaunchCalculations {

    /*
     * The intention of this class is to provide the launch angle and initial velocity given
     * the robot's distance from the hub. We will test this on the robot, and if the physics
     * don't work out practically, we will use this class to give initial test values for the
     * robot. We will run these test values to obtain 4 points experimentally. Then, we will
     * use interpolation to generate a function that will then give the correct launch angle
     * and velocity according to our current distance from the robot. 
     */

    /* 
    IMPORTANT: when using this class, make sure you always check that safeShooting = true.
    There is a method for this: getSafeShooting(). This will return true if the robot is within
    a "safe shooting distance", based on where the shooter can safely fire the fuels without
    anythign launching too far or getting stuck. You should ensure that you only launch
    fuel if this method returns true.

    We have two different angles: 65 degrees and 80 degrees. This works for shooting anywhere from
    .75m from the hub to 8m from the hub. These ranges should be sufficient for shooting basically
    anywhere we want on the field. The 65 degree angle is effective from 2m from the hub to
    8m from the hub. As a rule, we will use the 65 degree angle if we are between 3.5m from the hub
    and 8m from the hub. We will use the 80 degree angle if we are between .75m from the hub and 
    3.5m from the hub. This has been tested in projectile motion simulators.

    There is a 3 meter overlap where both angles work effectively (from 2m to 5m). Since the turret
    cannot instantly change its launch angle, this buffer zone is built in for time for the robot
    to alter its launch angle. The intermediate launch angle of the robot will result in a 
    computation for its initial velocity that accounts for the transition period, when the robot
    is changing its launch angle. You should never change the launch angle immediately, but it
    should be a slow process that takes place over the 3 meter buffer zone that both angles can
    work effectively. This means that any angle between 65 degrees and 80 degrees will work in
    this buffer zone.

    */

    // The origin is defined as the corner on the alliance side of the field closest to the chute
    
    // All angles are in radians, all distances are in meters, all velocities in m/s

    // Target values -- what we're trying to find
    private double launchAngle; // This is the angle between the shooter and the horizontal
    private double shooterVelocity; // Initial velocity of the shooter -- related to flywheel speed

    // Given values -- from robot pose
    private double robotPosX; // x distance between robot and origin (meters)
    private double robotPosY; // y distance between robot and origin (meters)

    // Given values -- from Constants file
    private static final double hubHeight = Constants.HUB_HEIGHT; // height of the top of the hub
    private static final double shooterHeight = Constants.SHOOTER_HEIGHT; // height of shooter
    private static final double hubPosX = Constants.HUB_X_POSITION; // x distance between hub and origin (meters)
    private static final double hubPosY = Constants.HUB_Y_POSITION; // y distance between hub and origin (meters)
    private static final double g = 9.81; // gravity constant

    /*
     * Because the foam balls will be affected by air resistance, we need to account for drag.
     * This is done not with physics, but wth an experimental scalar. We will find two drag constants
     * experimentally: K and Q. The constant K is a constant scalar. The constant Q is proportional
     * to the distance between the robot and hub. Then, we use linear interpolation to find the
     * robot's velocity accounted for drag: the ideal velocity is mutliplied by (K + Qx) to find
     * the actual velocity.
     */

    // Experimental drag values -- from Constants file
    private static final double dragK = Constants.DRAG_K; // constant scalar
    private static final double dragQ = Constants.DRAG_Q; // value proportional to distance

    // Intermediate values;
    private double deltaDistance; // distance between the robot and the hub
    private double deltaHeight; // height difference between shooter and hub
    private double idealShooterVelocity; // velocity of the shooter with drag unaccounted for

    // safety toggle -- see documentation above
    private boolean safeShooting;

    /*
     * This is the constructor for the launch calculations with no launch angle given, allowing
     * the class to calculate the launch angle automatically. This does not accoint for the
     * buffer zone.
     */
    public LaunchCalculations(double robotPosX, double robotPosY) {
        this.robotPosX = robotPosX;
        this.robotPosY = robotPosY;
        safeShooting = true; // by default, safeShooting is true, and is turned false if it becomes unsafe

        // Compute distance between hub and robot with Pythagorean Theorem
        deltaDistance = Math.sqrt(Math.pow(hubPosY - robotPosY, 2) 
        + Math.pow(hubPosX - robotPosX, 2));

        launchAngle = getAngle(); // sets launch angle automatically based on robot distance
        doTheMath();
    }

    /*
     * This is an overloaded constructor that includes a launch angle. This is if you want to
     * manually set the launch angle, like if the robot is within the buffer zone.
     */
    public LaunchCalculations(double RobotPosX, double RobotPosY, double launchAngle) {
        this.robotPosX = robotPosX;
        this.robotPosY = robotPosY;
        safeShooting = true; // by default, safeShooting is true, and is turned false if it becomes unsafe
        
        // Compute distance between hub and robot with Pythagorean Theorem
        deltaDistance = Math.sqrt(Math.pow(hubPosY - robotPosY, 2) 
        + Math.pow(hubPosX - robotPosX, 2));

        this.launchAngle = launchAngle; // sets the launch angle manually based on the parameter
        doTheMath();
    }

    /*
     * This function does the physics calculations. It is called from the constructor for initial
     * calculations. It is also called from setLaunchAngle(), so that when the launch angle
     * is changed manually, the calculations will recompute.
     */
    private void doTheMath() {
     
        // Compute the change in height between the shooter and the hub
        deltaHeight = hubHeight - shooterHeight;

        /*
        *  Computes the ideal shooter velocity. This is the speed of the shooter without considering
        *  the effects of drag. It is a physics formula that computes the initial velocity given
        *  the launch angle, the distance between the robot and the hub, the height change, and
        *  the gravitational constant g. This formula has been tested in projectile motion simulators.
        *  It starts with kinematic equations, then you set up a system of two of the kinematic equationsm
        *  eliminate time, and solve for the initial velocity.
        */
        idealShooterVelocity = Math.sqrt(
            ( 
                (g) * Math.pow(deltaDistance, 2) * Math.pow(1 / Math.cos(launchAngle), 2)
            )
            /
            2 * (
                deltaDistance * Math.tan(launchAngle) - deltaHeight
            )
        );

        // accounts for drag using linear interpolation and the drag constants K and Q found experimentally
        // see above documentation for more information
        shooterVelocity = idealShooterVelocity * (dragK + dragQ * deltaDistance);
        
    }

    /*
     * Gets the angle automatically based on the distance between the robot and the hub. It sets
     * safeShooting to false if the robot is not in the safe shooting range, which is between .75
     * and 8 meters. Then, if if the robot is between .75m and 3.5m from the hub, we return a launch
     * angle of 65 degrees (1.39626 radians). If the launch angle is between 3.5m and 8m from the hub, we return a launch
     * angle of 80 degrees (1.13446 radians).
     */
    private double getAngle() {
        if ( (deltaDistance < .75) || (deltaDistance > 8)) {
            safeShooting = false; // means the robot is NOT within safe shooting range
        }
        else if ( (deltaDistance < 3.5) ) {
            return 1.39626; // returns 65 degrees
        }
        return 1.13446; // returns 80 degrees
    }

    // returns the current launch angle of the shooter
    public double getLaunchAngle() {
        return launchAngle;
    }

    // sets the launch angle of the shooter to a specified angle
    public void setLaunchAngle(double launchAngle) {
        this.launchAngle = launchAngle; 
        doTheMath();
    }

    // returns the speed of the shooter
    public double getShooterSpeed() {
        return shooterVelocity;
    }

    // returns whether it is safe to shoot, meaning robot is within safe shooting range
    // ALWAYS CHECK THIS before shooting
    public boolean getSafeShooting() {
        return safeShooting;
    }

}
