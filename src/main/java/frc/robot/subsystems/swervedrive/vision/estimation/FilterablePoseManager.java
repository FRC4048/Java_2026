package frc.robot.subsystems.swervedrive.vision.estimation;

import edu.wpi.first.math.Vector;
import edu.wpi.first.math.geometry.Pose2d;
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

import org.littletonrobotics.junction.Logger;

/**
 * A subclass of PoseManager that filters vision measurements before they are fed into the kalman
 * filter.
 */
public class FilterablePoseManager extends PoseManager {
  private final VisionFilter filter;
  private final VisionTruster visionTruster;

  public FilterablePoseManager(
      PoseDeviation PoseDeviation,
      SwerveDriveKinematics kinematics,
      SwerveSubsystem drivebase,
      TimeInterpolatableBuffer<Pose2d> estimatedPoseBuffer,
      VisionFilter filter,
      VisionTruster visionTruster) {
    super(PoseDeviation, kinematics, drivebase, estimatedPoseBuffer);
    this.filter = filter;
    this.visionTruster = visionTruster;
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
    List<VisionMeasurement> validMeasurements = new ArrayList<>();
    List<VisionMeasurement> invalidMeasurements = new ArrayList<>();
    for (Queue<VisionMeasurement> queue : visionMeasurementQueueMap.values()) {
      List<VisionMeasurement> validMeasurementsAtTag = new ArrayList<>();
      List<VisionMeasurement> invalidMeasurementsAtTag = new ArrayList<>();
      LinkedHashMap<VisionMeasurement, FilterResult> filteredData =
              filter.filter(queue);
      queue.clear();
      for (Map.Entry<VisionMeasurement, FilterResult> entry : filteredData.entrySet()) {
        VisionMeasurement v = entry.getKey();
        FilterResult r = entry.getValue();
        switch (r) {
          case ACCEPTED -> {
            setVisionSTD(visionTruster.calculateTrust(v));
            validMeasurements.add(v);
            validMeasurementsAtTag.add(v);
            addVisionMeasurement(v);
          }
          case NOT_PROCESSED -> queue.add(v);
          case REJECTED -> {
            invalidMeasurements.add(v);
            invalidMeasurementsAtTag.add(v);
          }
        }
      }
      Logger.recordOutput("Apriltag/validMeasurementsAtTag"+, validMeasurementsAtTag.toArray(VisionMeasurement[]::new));
      Logger.recordOutput("Apriltag/invalidMeasurementsAtTag", invalidMeasurementsAtTag.toArray(VisionMeasurement[]::new));
    }
    Logger.recordOutput("Apriltag/acceptedMeasurements", validMeasurements.toArray(VisionMeasurement[]::new));
    Logger.recordOutput(
        "Apriltag/rejectedMeasurements", invalidMeasurements.toArray(VisionMeasurement[]::new));
  }

  public VisionTruster getVisionTruster() {
    return visionTruster;
  }
}
