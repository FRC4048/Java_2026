package frc.robot.subsystems.swervedrive.vision.estimation;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.Vector;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.interpolation.TimeInterpolatableBuffer;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.RobotMode;
import frc.robot.constants.Constants;
import frc.robot.subsystems.ApriltagSubsystem;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.subsystems.swervedrive.vision.truster.*;
import frc.robot.utils.Apriltag;
import frc.robot.utils.math.ArrayUtils;

import java.util.Arrays;
import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.Logger;

/**
 * Class in charge of feeding odometry and apriltag measurements from their respective IOs into a
 * {@link PoseManager} which outputs a robot position
 */
public class PoseEstimator {
 /*  private final SwerveModule frontLeft;
  private final SwerveModule frontRight;
  private final SwerveModule backLeft;
  private final SwerveModule backRight;*/
 // private final LoggableSystem<LoggableIO<ApriltagInputs>, ApriltagInputs> apriltagSystem;
  private int invalidCounter = 0;
  private final ApriltagSubsystem apriltagSystem;

  /* standard deviation of robot states, the lower the numbers arm, the more we trust odometry */

  /* standard deviation of vision readings, the lower the numbers arm, the more we trust vision */
  //  private static final Vector<N3> visionMeasurementStdDevs1 = VecBuilder.fill(0.5, 0.5, 0.5);

  /* the rate at which variance of vision measurements increases as distance from the tag increases*/

  /* standard deviation of vision readings, the lower the numbers arm, the more we trust vision */
  private final FilterablePoseManager poseManager;

  public PoseEstimator(
      /*SwerveModule frontLeftMotor,
      SwerveModule frontRightMotor,
      SwerveModule backLeftMotor,
      SwerveModule backRightMotor,*/
      SwerveDriveKinematics kinematics,
      SwerveSubsystem drivebase,
      double initGyroValueDeg,
      ApriltagSubsystem apriltagSystem) {
   /*this.frontLeft = frontLeftMotor;
    this.frontRight = frontRightMotor;
    this.backLeft = backLeftMotor;
    this.backRight = backRightMotor;*/
    this.apriltagSystem = apriltagSystem;//create new april tag object here;
    /*OdometryMeasurement initMeasurement =
        new OdometryMeasurement(
            new SwerveModulePosition[] {
              frontLeft.getPosition(),
              frontRight.getPosition(),
              backLeft.getPosition(),
              backRight.getPosition(),
            },
            initGyroValueDeg);*/
    TimeInterpolatableBuffer<Pose2d> m1Buffer =
        TimeInterpolatableBuffer.createBuffer(Constants.POSE_BUFFER_STORAGE_TIME);
    this.poseManager =
        new FilterablePoseManager(
            Constants.INITIAL_VISION_STD_DEVS,
            kinematics,
            drivebase,
            m1Buffer,
            new BasicVisionFilter(m1Buffer) {
              @Override
              public Pose2d getVisionPose(VisionMeasurement measurement) {
                return measurement.measurement();
              }
            },
            new SquareVisionTruster(Constants.INITIAL_VISION_STD_DEVS, Constants.VISION_STD_DEV_CONST));
  }


  /**
   * updates odometry, should be called in periodic
   *
   * @see SwerveDrivePoseEstimator#update(Rotation2d, SwerveModulePosition[])
   */
  public void updatePosition(Pose2d pose) {
    if (!Robot.getMode().equals(RobotMode.DISABLED)) {
      poseManager.addOdomMeasurement(pose, Logger.getTimestamp());
    }
  }

  private boolean validAprilTagPose(double[] measurement) {
    return !ArrayUtils.allMatch(measurement, -1.0) && measurement.length == 3;
  
  }
  private void updateVision(int... invalidApriltagNumbers) {
    long start = System.currentTimeMillis();
    if (Constants.ENABLE_VISION && Robot.getMode() != RobotMode.DISABLED) {
      for (int i = 0; i < apriltagSystem.getIO().getInputs().timestamp.length; i++) {
        double[] pos =
            new double[] {
              apriltagSystem.getIO().getInputs().posX[i],
              apriltagSystem.getIO().getInputs().posY[i],
              apriltagSystem.getIO().getInputs().poseYaw[i]
            };
        if (validAprilTagPose(pos)
            && !ArrayUtils.contains(
                invalidApriltagNumbers, apriltagSystem.getIO().getInputs().apriltagNumber[i])) {
          VisionMeasurement measurement = getVisionMeasurement(pos, i);
          poseManager.registerVisionMeasurement(measurement, apriltagSystem.getIO().getInputs().apriltagNumber[i]);
        } else {
          invalidCounter++;
          Logger.recordOutput("Apriltag/ValidationFailureCount", invalidCounter);
        }
      }
    }
    long end = System.currentTimeMillis();
    Logger.recordOutput("RegisteringVisionTimeMillis", end - start);
    poseManager.processQueue();
  }

  private VisionMeasurement getVisionMeasurement(double[] pos, int index) {
    double serverTime = apriltagSystem.getIO().getInputs().serverTime[index];
    //double timestamp = 0; // latency is not right we are assuming zero
    double timestamp = apriltagSystem.getIO().getInputs().timestamp[index];
    Pose2d visionPose = new Pose2d(pos[0], pos[1], Rotation2d.fromDegrees(pos[2]));
    double distanceFromTag = apriltagSystem.getIO().getInputs().distanceToTag[index];
    return new VisionMeasurement(visionPose, distanceFromTag, timestamp/1000);
  }

  /**
   * Collects Apriltag measurement(s) from the IO and checks their validity. If they are valid they
   * are sent to the {@link PoseManager} for further processing
   */
  public void updateVision() {
    updateVision(0);
  }

  public void updateVision(Apriltag focusedTag) {
    int[] invalidTags =
        Arrays.stream(Apriltag.values())
            .filter(a -> a != focusedTag)
            .mapToInt(Apriltag::number)
            .toArray();
    updateVision(invalidTags);
  }

  /**
   * @param radians robot angle to reset odometry to
   * @param translation2d robot field position to reset odometry to
   * @see SwerveDrivePoseEstimator#resetPosition(Rotation2d, SwerveModulePosition[], Pose2d)
   */
  /*public void resetOdometry(double radians, Translation2d translation2d) {
    OdometryMeasurement initMeasurement =
        new OdometryMeasurement(
            new SwerveModulePosition[] {
              frontLeft.getPosition(),
              frontRight.getPosition(),
              backLeft.getPosition(),
              backRight.getPosition(),
            },
            (radians * 180) / Math.PI);
    poseManager.resetPose(initMeasurement, translation2d);
    field.setRobotPose(poseManager.getEstimatedPosition());
  }*/

  @AutoLogOutput
  public Pose2d getEstimatedPose() {
    return poseManager.getEstimatedPosition();
  }

  public FilterablePoseManager getPoseManager() {
    return poseManager;
  }

  public void addMockVisionMeasurement() {
    poseManager.registerVisionMeasurement(
        new VisionMeasurement(getEstimatedPose(), 0, Logger.getTimestamp() / 1e6),1);
  }

  public VisionTruster getVisionTruster() {
    return poseManager.getVisionTruster();
  }
}
