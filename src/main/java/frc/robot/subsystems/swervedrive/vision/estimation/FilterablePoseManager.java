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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

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
    for (int i=0; i < Constants.NUMBER_OF_CAMERAS; i++) {
      Queue<VisionMeasurement> visionMeasurementQueue = visionMeasurmentQueueMap.get(i);
      LinkedHashMap<VisionMeasurement, FilterResult> filteredData =
        filter.filter(visionMeasurementQueue);
    visionMeasurementQueue.clear();
    List<Pose2d> validMeasurements = new ArrayList<>();
    List<Pose2d> invalidMeasurements = new ArrayList<>();
    for (Map.Entry<VisionMeasurement, FilterResult> entry : filteredData.entrySet()) {
      VisionMeasurement v = entry.getKey();
      FilterResult r = entry.getValue();
      switch (r) {
        case ACCEPTED -> {
          setVisionSTD(visionTruster.calculateTrust(v));
          validMeasurements.add(v.measurement());
          addVisionMeasurement(v);
        }
        case NOT_PROCESSED -> visionMeasurementQueue.add(v);
        case REJECTED -> {
          invalidMeasurements.add(v.measurement());
        }
      }
    }
    Logger.recordOutput("Apriltag/acceptedMeasurementsCamera" + i, validMeasurements.toArray(Pose2d[]::new));
    Logger.recordOutput(
        "Apriltag/rejectedMeasurementsCamera" + i, invalidMeasurements.toArray(Pose2d[]::new));
    }
    
  }

  public VisionTruster getVisionTruster() {
    return visionTruster;
  }
}
