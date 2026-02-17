package frc.robot.subsystems.swervedrive.parser;

import edu.wpi.first.math.Vector;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.numbers.N3;
import frc.robot.subsystems.swervedrive.SwerveDriveCustom;
import swervelib.SwerveDrive;
import swervelib.imu.SwerveIMU;
import swervelib.parser.SwerveDriveConfiguration;
import swervelib.parser.SwerveModuleConfiguration;
import swervelib.parser.json.ModuleJson;
import swervelib.parser.SwerveParserWithImu;

import java.io.File;
import java.io.IOException;

public class SwerveParserWithImuCustom extends SwerveParserWithImu {
    private SwerveIMU imu;
    public SwerveParserWithImuCustom(File directory, SwerveIMU imu) throws IOException {
        super(directory, imu);
    }
    public SwerveDriveCustom createSwerveDrive(double maxSpeed, Pose2d initialPose, Vector<N3> stateStdDevs, Vector<N3> visionMeasurementStdDev) {
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

        return new SwerveDriveCustom(
                swerveDriveConfiguration,
                controllerPropertiesJson.createControllerConfiguration(swerveDriveConfiguration, maxSpeed),
                maxSpeed,
                initialPose,
                stateStdDevs,
                visionMeasurementStdDev);
    }

}
