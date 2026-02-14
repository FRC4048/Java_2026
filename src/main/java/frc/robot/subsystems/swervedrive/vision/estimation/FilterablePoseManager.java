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
import frc.robot.subsystems.swervedrive.vision.VisionInputs;

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
  public static final String LOGGING_NAME = "Vision";

  public FilterablePoseManager(String name, VisionInputs inputs,
      PoseDeviation PoseDeviation,
      SwerveDriveKinematics kinematics,
      SwerveSubsystem drivebase,
      TimeInterpolatableBuffer<Pose2d> estimatedPoseBuffer,
      VisionFilter filter,
      VisionTruster visionTruster) {
    super(name,inputs,PoseDeviation, kinematics, drivebase, estimatedPoseBuffer);
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
    this(LOGGING_NAME, new VisionInputs(),
        new PoseDeviation(visionStd),
        kinematics, drivebase,
        estimatedPoseBuffer,
        filter,
        visionTruster);
  }
  @Override
  public void updateInputs(VisionInputs inputs) {
    LinkedHashMap<VisionMeasurement, FilterResult> filteredData =
        filter.filter(visionMeasurementQueue);
    int queueSize = filteredData.size();
    visionMeasurementQueue.clear();
    inputs.filterResults = new FilterResult[queueSize];
    inputs.distanceFromTag = new double[queueSize];
    inputs.timestamp = new double[queueSize];
    inputs.position = new Pose2d[queueSize];
    inputs.serverTime = new double[queueSize];
    List<VisionMeasurement> validMeasurements = new ArrayList<>();
    List<VisionMeasurement> invalidMeasurements = new ArrayList<>();
    Object[] filteredDataList = filteredData.entrySet().toArray();
    for (int i=0; i<filteredData.size(); i++) {
      Map.Entry<VisionMeasurement, FilterResult> entry = (Map.Entry<VisionMeasurement, FilterResult>) filteredDataList[i];
      VisionMeasurement v = entry.getKey();
      FilterResult r = entry.getValue();
      switch (r) {
        case ACCEPTED -> {
          setVisionSTD(visionTruster.calculateTrust(v));
          validMeasurements.add(v);
          addVisionMeasurement(v);
        }
        case NOT_PROCESSED -> visionMeasurementQueue.add(v);
        case REJECTED -> {
          invalidMeasurements.add(v);
        }
      }
      inputs.filterResults[i] = r;
      inputs.distanceFromTag[i] = v.distanceFromTag();
      inputs.timestamp[i] = v.timeOfMeasurement();
      inputs.serverTime[i] = v.timeOfMeasurement();
      inputs.position[i] = v.measurement();
    }
    Logger.recordOutput("Apriltag/acceptedMeasurements", validMeasurements.toArray(VisionMeasurement[]::new));
    Logger.recordOutput(
        "Apriltag/rejectedMeasurements", invalidMeasurements.toArray(VisionMeasurement[]::new));
  }

  public VisionTruster getVisionTruster() {
    return visionTruster;
  }
}
