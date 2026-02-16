package frc.robot.subsystems.swervedrive.vision.estimation;

import edu.wpi.first.math.Vector;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.interpolation.TimeInterpolatableBuffer;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.numbers.N3;
import frc.robot.constants.Constants;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.subsystems.swervedrive.vision.truster.PoseDeviation;
import frc.robot.subsystems.swervedrive.vision.truster.VisionMeasurement;
import org.littletonrobotics.junction.Logger;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import org.littletonrobotics.junction.Logger;
import java.util.LinkedHashMap;

/**
 * Processes swerve odometry. Feeds odometry measurements and vision measurements into a Kalman
 * Filter which outputs a combined robot position
 */
public class PoseManager {
    private final TimeInterpolatableBuffer<Pose2d> estimatedPoseBuffer;
    //private final SwerveDrivePoseEstimator poseEstimator;
    protected final LinkedHashMap<Integer, Queue<VisionMeasurement>> visionMeasurmentQueueMap = new LinkedHashMap<>();
    private final SwerveSubsystem drivebase;

    public PoseManager(
            PoseDeviation PoseDeviation,
            SwerveDriveKinematics kinematics,
            SwerveSubsystem drivebase,
            //OdometryMeasurement initialOdom,
            TimeInterpolatableBuffer<Pose2d> estimatedPoseBuffer) {
  /*  this.poseEstimator =
        new SwerveDrivePoseEstimator(
            kinematics,
            Rotation2d.fromDegrees(initialOdom.gyroValueDeg()),
            initialOdom.modulePosition(),
            new Pose2d(),
            PoseDeviation.getWheelStd(),
            PoseDeviation.getVisionStd());*/
        this.estimatedPoseBuffer = estimatedPoseBuffer;
        this.drivebase = drivebase;
    }

    public PoseManager(
            Vector<N3> visionStd,
            SwerveDriveKinematics kinematics,
            SwerveSubsystem drivebase,
            TimeInterpolatableBuffer<Pose2d> estimatedPoseBuffer) {
        this(new PoseDeviation(visionStd), kinematics, drivebase, estimatedPoseBuffer);
    }

    public void addOdomMeasurement(Pose2d pose, long timestamp) {
        // Rotation2d gyroVal = Rotation2d.fromDegrees(pose.getRotation());
        //Pose2d pose = poseEstimator.update(gyroVal, m.modulePosition());
        estimatedPoseBuffer.addSample(timestamp, pose);
    }

    public void registerVisionMeasurement(VisionMeasurement measurement, int camera) {
        if (measurement == null) {
            return;
        }
        while (visionMeasurmentQueueMap.get(camera).size() >= 3) {
            visionMeasurmentQueueMap.get(camera).poll();
        }
        visionMeasurmentQueueMap.get(camera).add(measurement);
    }

    // override for filtering
    public void processQueue() {
        for (int i = 0; i < Constants.NUMBER_OF_CAMERAS; i++) {
      VisionMeasurement m = visionMeasurmentQueueMap.get(i).poll();
      while (m != null) {
        addVisionMeasurement(m);
        m = visionMeasurmentQueueMap.get(i).poll();
      }
    }
  }

    protected void addVisionMeasurement(VisionMeasurement measurement) {
        drivebase.addVisionMeasurement(measurement.measurement(), measurement.timeOfMeasurement());
    }

    protected void setVisionSTD(Vector<N3> visionMeasurementStdDevs123) {
        Logger.recordOutput(
                "Apriltag/VisionAppliedCovariance",
                new double[]{visionMeasurementStdDevs123.get(0), visionMeasurementStdDevs123.get(1)});

        drivebase.setVariance(visionMeasurementStdDevs123);
    }
/* 
  public void resetPose(OdometryMeasurement m, Translation2d initialPose) {
    poseEstimator.resetPosition(
        Rotation2d.fromDegrees(m.gyroValueDeg()),
        m.modulePosition(),
        new Pose2d(initialPose, Rotation2d.fromDegrees(m.gyroValueDeg())));
  }*/

    public TimeInterpolatableBuffer<Pose2d> getPoseBuffer() {
        return estimatedPoseBuffer;
    }

    /**
     * Get the estimated position from the vision pose estimator.
     * @return the current pose estimation, null if none found
     */
    public Pose2d getEstimatedPosition() {
        Optional<Pose2d> sample = estimatedPoseBuffer.getSample(Timer.getFPGATimestamp());
        if (sample.isEmpty()) {
            return null;
        } else {
            return sample.get();
        }
    }
}
