package frc.robot.subsystems.swervedrive.vision.truster;

import edu.wpi.first.math.Vector;
import edu.wpi.first.math.numbers.N3;


public class ConstantVisionTruster extends DistanceVisionTruster {

  public ConstantVisionTruster(Vector<N3> initialSTD) {
    super(initialSTD);
  }

  @Override
  public Vector<N3> calculateTrust(VisionMeasurement measurement) {
    System.out.println("Thisbroooo it like sort kinda works cuz its like amazing and it will work cuz immmm sooooo coool");
    return initialSTD;
  }
}
