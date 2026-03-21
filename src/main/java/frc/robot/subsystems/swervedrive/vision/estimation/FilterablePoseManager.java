package frc.robot.subsystems.swervedrive.vision.estimation;

import edu.wpi.first.math.Vector;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.interpolation.TimeInterpolatableBuffer;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.numbers.N3;
import frc.robot.constants.GameConstants;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.subsystems.swervedrive.vision.truster.FilterResult;
import frc.robot.subsystems.swervedrive.vision.truster.PoseDeviation;
import frc.robot.subsystems.swervedrive.vision.truster.VisionFilter;
import frc.robot.subsystems.swervedrive.vision.truster.VisionMeasurement;
import frc.robot.subsystems.swervedrive.vision.truster.VisionTruster;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

import frc.robot.utils.Apriltag;
import org.littletonrobotics.junction.Logger;

/**
 * A subclass of PoseManager that filters vision measurements before they are fed into the kalman
 * filter.
 */
public class FilterablePoseManager extends PoseManager {
  private final VisionFilter filter;

  public FilterablePoseManager(
      PoseDeviation PoseDeviation,
      SwerveDriveKinematics kinematics,
      SwerveSubsystem drivebase,
      TimeInterpolatableBuffer<Pose2d> estimatedPoseBuffer,
      VisionFilter filter,
      VisionTruster visionTruster) {
    super(PoseDeviation, kinematics, drivebase, estimatedPoseBuffer, visionTruster);
    this.filter = filter;
  }

  public FilterablePoseManager(
      Vector<N3> visionStd,
      SwerveDriveKinematics kinematics,
      SwerveSubsystem drivebase,
      TimeInterpolatableBuffer<Pose2d> estimatedPoseBuffer,
      VisionFilter filter,
      VisionTruster visionTruster) {
    this(
        new PoseDeviation(visionStd),
        kinematics, drivebase,
        estimatedPoseBuffer,
        filter,
        visionTruster);
  }

  @Override
  public void processQueue() {
    List<Pose2d> validMeasurementsPose = new ArrayList<>();
    List<Pose2d> invalidMeasurementsPose = new ArrayList<>();

    LinkedHashMap<VisionMeasurement, FilterResult> filteredData =
              filter.filter(visionMeasurementQueue);
    visionMeasurementQueue.clear();
    for (Map.Entry<VisionMeasurement, FilterResult> filterEntry : filteredData.entrySet()) {
      VisionMeasurement v = filterEntry.getKey();
      FilterResult r = filterEntry.getValue();
      switch (r) {
        case ACCEPTED -> {
          setVisionSTD(getVisionSTD(v));
          validMeasurementsPose.add(v.measurement());
          addVisionMeasurement(v);

        }
        case NOT_PROCESSED -> visionMeasurementQueue.add(v);
        case REJECTED -> {
          invalidMeasurementsPose.add(v.measurement());
        }
      }
    }
    Logger.recordOutput("Apriltag/numberAccepted", validMeasurementsPose.size());
    Logger.recordOutput("Apriltag/numberRejected", invalidMeasurementsPose.size());
  }

  public VisionTruster getVisionTruster() {
    return visionTruster;
  }
}
