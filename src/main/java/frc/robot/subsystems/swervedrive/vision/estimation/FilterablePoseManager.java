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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.littletonrobotics.junction.Logger;

/**
 * A subclass of PoseManager that filters vision measurements before they are fed into the kalman
 * filter.
 */
public class FilterablePoseManager extends PoseManager {
  private final VisionFilter filter;
  private final VisionTruster visionTruster;
  private double lastTime = 0;
  private int numAccepted = 0;
  private int numRejected = 0;
  private double timeElapsed = 0;


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
    if (Logger.getTimestamp() - lastTime > 100) {
      timeElapsed = (Logger.getTimestamp() - lastTime) / 1000000.0;
      lastTime = Logger.getTimestamp();
      numAccepted = 0;
      numRejected = 0;
    }
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
          numAccepted++;
        }
        case NOT_PROCESSED -> visionMeasurementQueue.add(v);
        case REJECTED -> {
          numRejected++;
          invalidMeasurements.add(v.measurement());
        }
      }
    }
    Logger.recordOutput("Apriltag/acceptedMeasurements", validMeasurements.toArray(Pose2d[]::new));
    Logger.recordOutput(
        "Apriltag/rejectedMeasurements", invalidMeasurements.toArray(Pose2d[]::new));
    Logger.recordOutput("Apriltag/acceptedRate", numAccepted/timeElapsed);
    Logger.recordOutput("Apriltag/rejectedRate", numRejected/timeElapsed);
  }

  public VisionTruster getVisionTruster() {
    return visionTruster;
  }
}
