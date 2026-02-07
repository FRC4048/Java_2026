package frc.robot.subsystems.swervedrive.vision.truster;

import edu.wpi.first.math.Vector;
import edu.wpi.first.math.numbers.N3;

public abstract class DistanceVisionTruster implements VisionTruster {
  protected Vector<N3> initialSTD;

  public DistanceVisionTruster(Vector<N3> initialSTD) {
    this.initialSTD = initialSTD;
  }

  public void setInitialSTD(Vector<N3> initialSTD) {
    this.initialSTD = initialSTD;
  }
}
