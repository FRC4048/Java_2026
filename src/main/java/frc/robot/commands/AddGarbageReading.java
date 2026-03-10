package frc.robot.commands;

import edu.wpi.first.epilogue.Logged;
import frc.robot.apriltags.ApriltagReading;
import frc.robot.subsystems.ApriltagSubsystem;
import frc.robot.utils.logging.LoggedTunableNumber;
import frc.robot.utils.logging.commands.LoggableCommand;

public class AddGarbageReading extends LoggableCommand {
    private final ApriltagSubsystem april;
    public AddGarbageReading(ApriltagSubsystem april) {
        this.april = april;
        addRequirements(april);
    }

    @Override
    public void execute() {

            april.addSimReading(new ApriltagReading(Math.random()*10, Math.random()*10, Math.random()*10,
                    Math.random()*10,0,  (int) (Math.random()*32+1), Math.random()*10, 0, Math.random()*10));

    }
    @Override
    public boolean isFinished() {
        return false;
    }
}
