package frc.robot.commands;

import edu.wpi.first.math.Vector;
import edu.wpi.first.math.numbers.N3;
import org.littletonrobotics.junction.Logger;

import frc.robot.apriltags.ApriltagReading;
import frc.robot.subsystems.ApriltagSubsystem;
import frc.robot.utils.logging.LoggedTunableNumber;
import frc.robot.utils.logging.commands.LoggableCommand;
import java.util.Random;
import frc.robot.constants.Constants;

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
        for (int j=1; j<=Constants.NUMBER_OF_CAMERAS.get(); j++) {
                for (int i=0; i<numReadings.get(); i++) {
                    Vector<N3> variance = april.getVariance();
                    double varX = random.nextGaussian()*variance.get(0);
                    double varY = random.nextGaussian()*variance.get(1);
                    double varYaw = random.nextGaussian()*variance.get(2);
                        april.addSimReading(new ApriltagReading(posX.getAsDouble() + varX, posY.getAsDouble()+ varY,
                                poseYaw.getAsDouble()+ varYaw, distanceToTag.getAsDouble()+ random.nextGaussian()*0.05, (int) apriltagNumber.getAsDouble(),
                                latency.getAsDouble(), Logger.getTimestamp()/1000.0, j));
                }
        }
    }
}
