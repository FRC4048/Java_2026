package frc.robot.subsystems.swervedrive.vision.estimation;

import edu.wpi.first.math.Vector;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.interpolation.TimeInterpolatableBuffer;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.numbers.N3;
import frc.robot.constants.Constants;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.subsystems.swervedrive.vision.truster.FilterResult;
import frc.robot.subsystems.swervedrive.vision.truster.PoseDeviation;
import frc.robot.subsystems.swervedrive.vision.truster.VisionFilter;
import frc.robot.subsystems.swervedrive.vision.truster.VisionMeasurement;
import frc.robot.subsystems.swervedrive.vision.truster.VisionTruster;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

import frc.robot.utils.Apriltag;
import org.littletonrobotics.junction.Logger;

/**
 * A subclass of PoseManager that filters vision measurements before they are fed into the kalman
 * filter.
 */
public class FilterablePoseManager extends PoseManager {
  private record AcceptedMeasurementRecord(int tagId, double timestamp) {
  }

  private final VisionFilter filter;
  private final ConcurrentLinkedDeque<AcceptedMeasurementRecord> lastSecondAcceptedMeasurements = new ConcurrentLinkedDeque<>();

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
    lastSecondAcceptedMeasurements.removeIf(record -> record.timestamp < oneSecondAgo);

    List<VisionMeasurement> validMeasurements = new ArrayList<>();
    List<VisionMeasurement> invalidMeasurements = new ArrayList<>();
    List<Pose2d> validMeasurementsPose = new ArrayList<>();
    List<Pose2d> invalidMeasurementsPose = new ArrayList<>();
    List<Integer> validTags = new ArrayList<>();
    List<Integer> invalidTags = new ArrayList<>();
    for (Map.Entry<Integer, Queue<VisionMeasurement>> queueEntry : visionMeasurementQueueMap.entrySet()) {
      int tagId = queueEntry.getKey();
      Queue<VisionMeasurement> queue = queueEntry.getValue();
      List<VisionMeasurement> validMeasurementsAtTag = new ArrayList<>();
      List<VisionMeasurement> invalidMeasurementsAtTag = new ArrayList<>();
      LinkedHashMap<VisionMeasurement, FilterResult> filteredData =
              filter.filter(queue, drivebase.getCameraPose());
      queue.clear();
      for (Map.Entry<VisionMeasurement, FilterResult> filterEntry : filteredData.entrySet()) {
        VisionMeasurement v = filterEntry.getKey();
        FilterResult r = filterEntry.getValue();
        switch (r) {
          case ACCEPTED -> {
            setVisionSTD(visionTruster.calculateTrust(v, drivebase.getCameraPose()));
            validMeasurements.add(v);
            validMeasurementsPose.add(v.measurement());
            validMeasurementsAtTag.add(v);
            validTags.add(tagId);
            addVisionMeasurement(v);
            lastSecondAcceptedMeasurements.add(new AcceptedMeasurementRecord(tagId, v.timeOfMeasurement()));
          }
          case NOT_PROCESSED -> queue.add(v);
          case REJECTED -> {
            invalidMeasurements.add(v);
            invalidMeasurementsPose.add(v.measurement());
            invalidTags.add(tagId);
            invalidMeasurementsAtTag.add(v);
          }
        }
      }
      Logger.recordOutput("Apriltag/validMeasurementsAtTag"+tagId, validMeasurementsAtTag.toArray(VisionMeasurement[]::new));
      Logger.recordOutput("Apriltag/invalidMeasurementsAtTag"+tagId, invalidMeasurementsAtTag.toArray(VisionMeasurement[]::new));
    }
    Logger.recordOutput("Apriltag/acceptedMeasurements", validMeasurements.toArray(VisionMeasurement[]::new));
    Logger.recordOutput("Apriltag/rejectedMeasurements", invalidMeasurements.toArray(VisionMeasurement[]::new));
    Logger.recordOutput("Apriltag/acceptedMeasurementsPose", validMeasurementsPose.toArray(Pose2d[]::new));
    Logger.recordOutput("Apriltag/rejectedMeasurementsPose", invalidMeasurementsPose.toArray(Pose2d[]::new));
    Logger.recordOutput("Apriltag/acceptedTagIds",validTags.stream().mapToInt(i -> i).toArray());
    Logger.recordOutput("Apriltag/rejectedTagIds",invalidTags.stream().mapToInt(i -> i).toArray());
    Logger.recordOutput("Apriltag/numberAcceptedLastSecond", lastSecondAcceptedMeasurements.size());
    Logger.recordOutput("Apriltag/TagIdsAcceptedLastSecond", lastSecondAcceptedMeasurements.stream()
            .map(record -> record.tagId)
            .distinct()
            .mapToInt(i -> i)
            .toArray());
    if (Constants.currentMode == Constants.Mode.SIM) {
      Logger.recordOutput("Apriltag/acceptedTagPoseNow",
              validTags.stream()
              .map(tagId -> Apriltag.of(tagId).getPose().toPose2d())
              .toArray(Pose2d[]::new));
      Logger.recordOutput("Apriltag/acceptedTagPoseLastSecond",
              lastSecondAcceptedMeasurements.stream().distinct()
                      .map(record -> Apriltag.of(record.tagId).getPose().toPose2d())
                      .toArray(Pose2d[]::new));
    }
  }

  public VisionTruster getVisionTruster() {
    return visionTruster;
  }
}
