package frc.robot.subsystems.swervedrive.vision.truster;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.interpolation.TimeInterpolatableBuffer;
import frc.robot.constants.Constants;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Queue;

/**
 * Vision filter implementation <br>
 * Keeps track of robot position over time <br>
 * Processes Vision measurements in batches of two <br>
 * Only allows vision measurements to be processed if their delta change in position is close to the
 * delta change in position of the robot odometry
 */
public abstract class BasicVisionFilter implements VisionFilter, VisionTransformer {

  private final TimeInterpolatableBuffer<Pose2d> poseBuffer;

  public BasicVisionFilter(TimeInterpolatableBuffer<Pose2d> poseBuffer) {
    this.poseBuffer = poseBuffer;
  }

  @Override
  public LinkedHashMap<VisionMeasurement, FilterResult> filter(
      Queue<VisionMeasurement> measurements) {
    LinkedHashMap<VisionMeasurement, FilterResult> resultMap = new LinkedHashMap<>();
    VisionMeasurement m1 = measurements.poll();
    VisionMeasurement m2 = measurements.peek();
    boolean processing = true;
    do {
      /*
      -------------------------------------------------------
      Handle Null Case
      -------------------------------------------------------
      */
      if (m1 == null) {
        processing = false;
        if (m2 != null) {
          resultMap.put(m2, FilterResult.NOT_PROCESSED);
        }
        continue;
      } else if (m2 == null) {
        resultMap.put(m1, FilterResult.NOT_PROCESSED);
        processing = false;
        continue;
      }
      /*
      -------------------------------------------------------
      Filter poses
      -------------------------------------------------------
      */
      Pose2d vision1Pose = getVisionPose(m1);
      Pose2d vision2Pose = getVisionPose(m2);
      boolean valid1 =
          filterVision(vision1Pose, vision2Pose, m1.timeOfMeasurement(), m2.timeOfMeasurement());
      resultMap.put(m1, valid1 ? FilterResult.ACCEPTED : FilterResult.REJECTED);
      m1 = measurements.poll();
      m2 = measurements.peek();

    } while (processing);
    return resultMap;
  }

  private boolean filterVision(Pose2d m1Pose, Pose2d m2Pose, double m1Time, double m2Time) {
    Optional<Pose2d> odomPoseAtVis1 = poseBuffer.getSample(m1Time);
    Optional<Pose2d> odomPoseAtVis2 = poseBuffer.getSample(m2Time);
    if (odomPoseAtVis1.isEmpty() || odomPoseAtVis2.isEmpty()) {
      return false;
    }
    if (!inBounds(m1Pose) || !inBounds(m2Pose)) {
      return false;
    }
    double odomDiff1To2 =
        odomPoseAtVis1.get().getTranslation().getDistance(odomPoseAtVis2.get().getTranslation());
    double visionDiff1To2 = m1Pose.getTranslation().getDistance(m2Pose.getTranslation());
    double diff = Math.abs(odomDiff1To2 - visionDiff1To2);
    //return Math.abs(diff) <= Constants.VISION_CONSISTANCY_THRESHOLD.get();
    return true;
  }
  
  private boolean inBounds(Pose2d pose2d) {
    return pose2d.getX() > 0 && pose2d.getX() < 20 && pose2d.getY() > 0 && pose2d.getY() < 20;
  }
}
