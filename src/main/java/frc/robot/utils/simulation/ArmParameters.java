package frc.robot.utils.simulation;

import edu.wpi.first.math.geometry.Rotation2d;

public class ArmParameters {
    public String name = "Default Arm";
    public double armGearing = 0;
    public double armInertia = 0;
    public double armLength = 0;
    public Rotation2d armMinAngle = Rotation2d.fromRadians(0);
    public Rotation2d armMaxAngle = Rotation2d.fromRadians(0);
    public boolean armSimulateGravity = true;
}
