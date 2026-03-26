package frc.robot.subsystems.swervedrive.vision.estimation;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.Vector;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.interpolation.TimeInterpolatableBuffer;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.constants.Constants;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.subsystems.swervedrive.vision.truster.PoseDeviation;
import frc.robot.subsystems.swervedrive.vision.truster.VisionMeasurement;
import frc.robot.subsystems.swervedrive.vision.truster.VisionTruster;
import org.littletonrobotics.junction.Logger;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

/**
 * Processes swerve odometry. Feeds odometry measurements and vision measurements into a Kalman
 * Filter which outputs a combined robot position
 */
public class PoseManager {
    private final TimeInterpolatableBuffer<Pose2d> estimatedPoseBuffer;
    //private final SwerveDrivePoseEstimator poseEstimator;
    protected final Queue<VisionMeasurement> visionMeasurementQueue = new LinkedList<>();
    private final SwerveSubsystem drivebase;
    protected final VisionTruster visionTruster;

    public PoseManager(
            PoseDeviation PoseDeviation,
            SwerveDriveKinematics kinematics,
            SwerveSubsystem drivebase,
            //OdometryMeasurement initialOdom,
            TimeInterpolatableBuffer<Pose2d> estimatedPoseBuffer,
            VisionTruster visionTruster) {
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
        this.visionTruster = visionTruster;
    }

    public PoseManager(
            Vector<N3> visionStd,
            SwerveDriveKinematics kinematics,
            SwerveSubsystem drivebase,
            TimeInterpolatableBuffer<Pose2d> estimatedPoseBuffer,
            VisionTruster visionTruster) {
        this(new PoseDeviation(visionStd), kinematics, drivebase, estimatedPoseBuffer, visionTruster);
    }

    public void addOdomMeasurement(Pose2d pose, long timestamp) {
        // Rotation2d gyroVal = Rotation2d.fromDegrees(pose.getRotation());
        //Pose2d pose = poseEstimator.update(gyroVal, m.modulePosition());
        estimatedPoseBuffer.addSample(timestamp, pose);
    }

    public void registerVisionMeasurement(VisionMeasurement measurement) {
        if (measurement == null) {
            return;
        }
        while (visionMeasurementQueue.size() >= 3) {
            visionMeasurementQueue.poll();
        }
        visionMeasurementQueue.add(measurement);
    }

    // override for filtering
    public void processQueue() {
        VisionMeasurement m = visionMeasurementQueue.poll();
        while (m != null) {
            setVisionSTD(getVisionSTD(m));
            addVisionMeasurement(m);
            m = visionMeasurementQueue.poll();
        }
    }

    protected Vector<N3> getVisionSTD(VisionMeasurement measurement) {
        double StdDev = measurement.stdDev();
        if (Constants.DEBUG) {
            Logger.recordOutput("Apriltag/UsingRioStdDev", Constants.USE_CAMERA_APRILTAG_STD_DEV);
        }

        if (Constants.USE_CAMERA_APRILTAG_STD_DEV) {
            return VecBuilder.fill(StdDev, StdDev, 1000);
        }
        return visionTruster.calculateTrust(measurement);
    }

    protected void addVisionMeasurement(VisionMeasurement measurement) {
        Logger.recordOutput("Apriltag/VisionPoseSentToSwerve", measurement.measurement());
        Logger.recordOutput("Apriltag/VisionTimestampSentToSwerve", measurement.timeOfMeasurement());
        drivebase.addVisionMeasurement(measurement.measurement(), measurement.timeOfMeasurement());
    }

    protected void setVisionSTD(Vector<N3> visionMeasurementStdDevs) {
        Logger.recordOutput(
                "Apriltag/VisionAppliedCovariance",
                new double[] {
                        visionMeasurementStdDevs.get(0),
                        visionMeasurementStdDevs.get(1),
                        visionMeasurementStdDevs.get(2)
                });

        drivebase.setVariance(visionMeasurementStdDevs);
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

    public Rotation2d getRotation() {
        return drivebase.getHeading();
    }
}
