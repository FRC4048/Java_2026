package frc.robot.subsystems.swervedrive.vision.truster;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.Vector;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.numbers.N3;
import frc.robot.utils.Apriltag;

public class SquareVisionTruster extends DistanceVisionTruster {

  private final double constant;

  public SquareVisionTruster(Vector<N3> initialSTD, double constant) {
    super(initialSTD);
    this.initialSTD = initialSTD;
    this.constant = constant;
  }

  @Override
  public Vector<N3> calculateTrust(VisionMeasurement measurement, Pose3d cameraPose) {
    Translation3d adjPose2 = cameraPose.relativeTo(Apriltag.of(measurement.tagId()).getPose()).getTranslation();
    double distanceTimesCosIncidenceAngle = adjPose2.getX()/adjPose2.getNorm();
    double std = Math.pow(measurement.distanceFromTag(), 2) * constant / distanceTimesCosIncidenceAngle;
    return initialSTD.plus(VecBuilder.fill(std, std, std*10000));
  }
}
