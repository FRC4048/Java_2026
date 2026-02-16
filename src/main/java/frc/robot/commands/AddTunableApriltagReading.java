package frc.robot.commands;

import org.littletonrobotics.junction.Logger;

import frc.robot.apriltags.ApriltagReading;
import frc.robot.subsystems.ApriltagSubsystem;
import frc.robot.utils.logging.LoggedTunableNumber;
import frc.robot.utils.logging.commands.LoggableCommand;

import java.util.Random;

public class AddTunableApriltagReading extends LoggableCommand {
    private final ApriltagSubsystem april;
    private LoggedTunableNumber posX;
    private LoggedTunableNumber posY;
    private LoggedTunableNumber poseYaw;
    private LoggedTunableNumber distanceToTag;
    private LoggedTunableNumber apriltagNumber;
    private LoggedTunableNumber latency;
    private LoggedTunableNumber numReadings;
    Random random = new Random();
    public AddTunableApriltagReading(ApriltagSubsystem april) {
        this.april = april;
        posX = new LoggedTunableNumber("SimAprilTagX", 1);
        posY = new LoggedTunableNumber("SimAprilTagY", 1);
        poseYaw = new LoggedTunableNumber("SimAprilTagYaw", 0);
        distanceToTag = new LoggedTunableNumber("SimAprilTagDistanceToTag", 0.1);
        apriltagNumber = new LoggedTunableNumber("SimAprilTagApriltagNum", 1);
        latency = new LoggedTunableNumber("SimAprilTagLatency", 0);
        numReadings = new LoggedTunableNumber("NumReadingsPerTick", 1);

    }
    @Override
    public void execute() {
        for (int i=0; i<numReadings.get(); i++) {
            april.addSimReading(new ApriltagReading(posX.getAsDouble() + random.nextGaussian()*0.05, posY.getAsDouble()+ random.nextGaussian()*0.05,
                    poseYaw.getAsDouble()+ random.nextGaussian()*0.05, distanceToTag.getAsDouble(), (int) apriltagNumber.getAsDouble(),
                    latency.getAsDouble(), Logger.getTimestamp()/1000.0));
        }
    }
}
