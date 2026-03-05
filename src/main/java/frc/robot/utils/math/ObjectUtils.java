package frc.robot.utils.math;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.Vector;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.numbers.N3;

import static java.lang.Math.PI;
import static java.lang.Math.abs;

public class ObjectUtils {
    // TODO: Implement obstruction
    public static boolean canSee(Pose3d objectPose, Pose3d cameraPose, double HorizontalFOV, double VerticalFOV) {
        Pose3d adjPose = objectPose.relativeTo(cameraPose);
        double horizontalAngle = Math.atan2(adjPose.getY(), adjPose.getX());
        double verticalAngle = Math.asin(adjPose.getZ()/adjPose.getTranslation().getNorm());
        Translation3d adjPose2 = cameraPose.relativeTo(objectPose).getTranslation();
        double distanceToCamera = adjPose2.getNorm();
        double cosIncidence = adjPose2.getX() / distanceToCamera;
        if (cosIncidence<=0) {
            return false;
        }
        return abs(horizontalAngle) < HorizontalFOV/2 && abs(verticalAngle) < VerticalFOV/2;// TODO: Maybe change Later By Implementing Math Calculations (Linear Algebra)
    }
}
