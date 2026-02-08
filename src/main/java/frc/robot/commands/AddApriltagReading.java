package frc.robot.commands;

import frc.robot.apriltags.ApriltagIO;
import frc.robot.apriltags.ApriltagReading;
import frc.robot.subsystems.ApriltagSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class AddApriltagReading extends LoggableCommand {
    private final ApriltagSubsystem april;
    private final ApriltagReading reading;
    public AddApriltagReading(ApriltagSubsystem april, ApriltagReading reading) {
        this.april = april;
        this.reading = reading;
    }

    @Override
    public void initialize() {
        april.addSimReading(reading);
    }
    @Override
    public boolean isFinished() {
        return true;
    }
}
