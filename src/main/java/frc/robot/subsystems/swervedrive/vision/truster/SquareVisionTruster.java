package frc.robot.subsystems.swervedrive.vision.truster;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.Vector;
import edu.wpi.first.math.numbers.N3;

public class SquareVisionTruster extends DistanceVisionTruster {

  private final double constant;

  public SquareVisionTruster(Vector<N3> initialSTD, double constant) {
    super(initialSTD);
    this.initialSTD = initialSTD;
    this.constant = constant;
  }

  @Override
  public Vector<N3> calculateTrust(VisionMeasurement measurement) {
    double std = Math.pow(measurement.distanceFromTag() - 0.4572, 2) * constant; //TODO: No magic numbers
    return initialSTD.plus(VecBuilder.fill(std, std, std));
  }
}
