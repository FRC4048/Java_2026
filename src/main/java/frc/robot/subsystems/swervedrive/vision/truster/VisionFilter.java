package frc.robot.subsystems.swervedrive.vision.truster;

import edu.wpi.first.math.geometry.Pose3d;

import java.util.LinkedHashMap;
import java.util.Queue;

public interface VisionFilter {
  LinkedHashMap<VisionMeasurement, FilterResult> filter(Queue<VisionMeasurement> measurements, Pose3d cameraPose);
}
