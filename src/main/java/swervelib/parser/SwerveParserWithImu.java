package swervelib.parser;

import edu.wpi.first.math.geometry.Pose2d;
import swervelib.SwerveDrive;
import swervelib.imu.SwerveIMU;
import swervelib.parser.json.ModuleJson;

import java.io.File;
import java.io.IOException;

/**
 * A subclass of {@link SwerveParser} that allows setting an external IMU.
 */
public class SwerveParserWithImu extends SwerveParser {
    private SwerveIMU imu;

    /**
     * Construct a swerve parser. Will throw an error if there is a missing file.
     *
     * @param directory Directory with swerve configurations.
     * @throws IOException if a file doesn't exist.
     */
    public SwerveParserWithImu(File directory, SwerveIMU imu) throws IOException {
        super(directory);
        this.imu = imu;
    }

    /**
     * Create {@link SwerveDrive} from configuration.
     * Use the parsed JSON configuration, and, if an IMU has been set externally, use it instead of the
     * one configured in the JSON.
     * This method is a copy of the one in the superclass with the IMU-related changes.
     *
     * @param maxSpeed    Maximum speed of the robot in meters per second for normal+angular acceleration in
     *                    {@link swervelib.SwerveController} of the robot
     * @param initialPose {@link Pose2d} initial pose.
     * @return {@link SwerveDrive} instance.
     */
    public SwerveDrive createSwerveDrive(double maxSpeed, Pose2d initialPose) {
        SwerveModuleConfiguration[] moduleConfigurations =
                new SwerveModuleConfiguration[moduleJsons.length];
        for (int i = 0; i < moduleConfigurations.length; i++) {
            ModuleJson module = moduleJsons[i];
            moduleConfigurations[i] =
                    module.createModuleConfiguration(
                            pidfPropertiesJson.angle,
                            pidfPropertiesJson.drive,
                            physicalPropertiesJson.createPhysicalProperties(),
                            swerveDriveJson.modules[i]);
        }

        SwerveIMU imuToUse = (imu != null) ? imu : swerveDriveJson.imu.createIMU();
        SwerveDriveConfiguration swerveDriveConfiguration =
                new SwerveDriveConfiguration(
                        moduleConfigurations,
                        imuToUse,
                        swerveDriveJson.invertedIMU,
                        physicalPropertiesJson.createPhysicalProperties());

        return new SwerveDrive(
                swerveDriveConfiguration,
                controllerPropertiesJson.createControllerConfiguration(swerveDriveConfiguration, maxSpeed),
                maxSpeed,
                initialPose);
    }
}
