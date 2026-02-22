package frc.robot.utils.math;

import edu.wpi.first.math.geometry.Pose3d;

import static java.lang.Math.PI;
import static java.lang.Math.abs;

public class ObjectUtils {
    // TODO: Implement obstruction
    public static boolean canSee(Pose3d objectPose, Pose3d cameraPose, double HorizontalFOV, double VerticalFOV) {
        Pose3d adjPose = objectPose.relativeTo(cameraPose);
        double horizontalAngle = Math.atan2(adjPose.getY(), adjPose.getX());
        double verticalAngle = Math.asin(adjPose.getZ()/adjPose.getTranslation().getNorm());
        double tagAngle = adjPose.getRotation().getZ()+PI;
        double diffAngle = abs(horizontalAngle-tagAngle);
        if (diffAngle>=2*PI) {
            diffAngle-=2*PI;
        }
        if (diffAngle >= PI/2 && diffAngle <=3*PI/2) {
            return false;
        }
        return abs(horizontalAngle) < HorizontalFOV/2 && abs(verticalAngle) < VerticalFOV/2;// TODO: Maybe change Later By Implementing Math Calculations (Linear Algebra)
    }
}
