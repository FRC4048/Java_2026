package frc.robot.subsystems.swervedrive.vision.truster;

import java.util.LinkedHashMap;
import java.util.Queue;

public interface VisionFilter {
  LinkedHashMap<VisionMeasurement, FilterResult> filter(Queue<VisionMeasurement> measurements);
}
