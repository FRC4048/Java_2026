package frc.robot.commands;

import edu.wpi.first.epilogue.Logged;
import frc.robot.apriltags.ApriltagReading;
import frc.robot.subsystems.ApriltagSubsystem;
import frc.robot.utils.logging.LoggedTunableNumber;
import frc.robot.utils.logging.commands.LoggableCommand;

public class AddGarbageReading extends LoggableCommand {
    private final ApriltagSubsystem april;
    private LoggedTunableNumber posX;
    private LoggedTunableNumber posY;
    private LoggedTunableNumber poseYaw;
    private LoggedTunableNumber distanceToTag;
    private LoggedTunableNumber apriltagNumber;
    private LoggedTunableNumber latency;
    private LoggedTunableNumber measurementTime;
    public AddGarbageReading(ApriltagSubsystem april) {
        this.april = april;
        posX = new LoggedTunableNumber("SimAprilTagX", 0);
        posY = new LoggedTunableNumber("SimAprilTagY", 0);
        poseYaw = new LoggedTunableNumber("SimAprilTagYaw", 0);
        distanceToTag = new LoggedTunableNumber("SimAprilTagDistanceToTag", 0);
        apriltagNumber = new LoggedTunableNumber("SimAprilTagApriltagNum", 0);
        latency = new LoggedTunableNumber("SimAprilTagLatency", 0);
        measurementTime = new LoggedTunableNumber("SimAprilTagMeasurementTime", 0);
    }

    @Override
    public void execute() {

            april.addSimReading(new ApriltagReading(Math.random()*100, Math.random()*100, Math.random()*100, Math.random()*100, (int) (Math.random()*32+1), Math.random()*100, Math.random()*100));

    }
    @Override
    public boolean isFinished() {
        return false;
    }
}
