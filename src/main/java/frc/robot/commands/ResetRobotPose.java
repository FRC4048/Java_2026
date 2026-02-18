package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.LoggedTunableNumber;
import frc.robot.utils.logging.commands.LoggableCommand;

public class ResetRobotPose extends LoggableCommand {
    private final SwerveSubsystem drivebase;
    private final LoggedTunableNumber posX = new LoggedTunableNumber("Robot X Position", 3);
    private final LoggedTunableNumber posY = new LoggedTunableNumber("Robot Y Position", 3);
    private final LoggedTunableNumber poseYaw = new LoggedTunableNumber("Robot Yaw", 0);
    public ResetRobotPose(SwerveSubsystem drivebase) {
        this.drivebase = drivebase;
    }
    @Override
    public void initialize() {
        for (int i=0; i<100; i++) {
            drivebase.resetOdometry(new Pose2d(posX.get(), posY.get(), new Rotation2d(poseYaw.get())));
        }
    }
    @Override
    public boolean isFinished() {
        return true;
    }
}
