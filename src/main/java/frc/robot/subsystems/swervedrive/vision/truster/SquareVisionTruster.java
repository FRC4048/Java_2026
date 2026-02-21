package frc.robot.subsystems.swervedrive.vision.truster;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.Vector;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.numbers.N3;

public class SquareVisionTruster extends DistanceVisionTruster {

  private final double constant;

  public SquareVisionTruster(Vector<N3> initialSTD, double constant) {
    super(initialSTD);
    this.initialSTD = initialSTD;
    this.constant = constant;
  }

  @Override
  public Vector<N3> calculateTrust(VisionMeasurement measurement, Pose3d cameraPose) {
    Pose3d adjPose = measurement.tag().tag().getPose().relativeTo(cameraPose);
    double cosIncidenceAngle = (-adjPose.getX()*Math.cos(adjPose.getRotation().getZ())-adjPose.getY()*Math.sin(adjPose.getRotation().getZ()))/(adjPose.getTranslation().getNorm());
    double std = Math.pow(measurement.distanceFromTag(), 2) * constant / cosIncidenceAngle;
    return initialSTD.plus(VecBuilder.fill(std, std, std*10000));
  }
}
