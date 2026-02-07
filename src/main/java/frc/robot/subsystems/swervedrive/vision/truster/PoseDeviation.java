package frc.robot.subsystems.swervedrive.vision.truster;

import edu.wpi.first.math.Vector;
import edu.wpi.first.math.numbers.N3;

/** POJO containing the vision standard deviation */
public class PoseDeviation {
  private final Vector<N3> visionStd;

  public PoseDeviation(Vector<N3> visionStd) {
    this.visionStd = visionStd;
  }

  public Vector<N3> getVisionStd() {
    return visionStd;
  }
}
