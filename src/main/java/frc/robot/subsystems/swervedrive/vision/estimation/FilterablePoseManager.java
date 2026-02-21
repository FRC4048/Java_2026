package frc.robot.subsystems.swervedrive.vision.estimation;

import edu.wpi.first.math.Vector;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.interpolation.TimeInterpolatableBuffer;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.numbers.N3;
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
  private record MeasurementRecord(Apriltag tag, double timestamp, FilterResult result) { }
  public record VisionLog(VisionMeasurement measurement, FilterResult result) {}
  private final ConcurrentLinkedDeque<MeasurementRecord> lastSecondMeasurements = new ConcurrentLinkedDeque<>();

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
    double currentTime = Logger.getTimestamp()/1000000.0;
    double oneSecondAgo = currentTime - 1.0;
    lastSecondMeasurements.removeIf(record -> record.timestamp < oneSecondAgo);
    List<VisionLog> log = new ArrayList<>();
    List<Pose2d> validMeasurementsPose = new ArrayList<>();
    List<Pose2d> invalidMeasurementsPose = new ArrayList<>();;
    List<Pose3d> acceptedTagsPose = new ArrayList<>();

    LinkedHashMap<VisionMeasurement, FilterResult> filteredData =
              filter.filter(visionMeasurementQueue);
    visionMeasurementQueue.clear();
    for (Map.Entry<VisionMeasurement, FilterResult> filterEntry : filteredData.entrySet()) {
      VisionMeasurement v = filterEntry.getKey();
      Apriltag tag = v.tag().tag();
      FilterResult r = filterEntry.getValue();
      log.add(new VisionLog(v, r));
      lastSecondMeasurements.add(new MeasurementRecord(tag, v.timeOfMeasurement(),r));
      switch (r) {
        case ACCEPTED -> {
          setVisionSTD(visionTruster.calculateTrust(v));
          validMeasurementsPose.add(v.measurement());
          addVisionMeasurement(v);
          acceptedTagsPose.add(tag.getPose());

        }
        case NOT_PROCESSED -> visionMeasurementQueue.add(v);
        case REJECTED -> {
          invalidMeasurementsPose.add(v.measurement());
        }
      }
    }
    Logger.recordOutput("Apriltag/acceptedMeasurementsPose", validMeasurementsPose.toArray(Pose2d[]::new));
    Logger.recordOutput("Apriltag/rejectedMeasurementsPose", invalidMeasurementsPose.toArray(Pose2d[]::new));
    Logger.recordOutput("Apriltag/numberAcceptedLastSecond", lastSecondMeasurements.stream().filter(record -> record.result == FilterResult.ACCEPTED).count());
    Logger.recordOutput("Apriltag/numberNotProcessedLastSecond", lastSecondMeasurements.stream().filter(record -> record.result == FilterResult.NOT_PROCESSED).count());
    Logger.recordOutput("Apriltag/numberRejectedLastSecond", lastSecondMeasurements.stream().filter(record -> record.result == FilterResult.REJECTED).count());
    Logger.recordOutput("Apriltag/acceptedTagPose", acceptedTagsPose.toArray(Pose3d[]::new));
    Logger.recordOutput("Apriltag/Log", log.toArray(VisionLog[]::new));
  }

  public VisionTruster getVisionTruster() {
    return visionTruster;
  }
}
