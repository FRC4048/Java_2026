package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.logging.io.motor.SparkMaxIo;

public class AngularSubsystem extends SubsystemBase {

    public static final String LOGGING_NAME = "AngularSubsystem";

    private final SparkMaxIo io;

    public AngularSubsystem(SparkMaxIo io) {
        this.io = io;
    }

    @Override
    public void periodic() {
        io.periodic();
    }

    public void setPosition(double targetRotations) {
        io.setPosition(targetRotations);
    }

    public void stopMotors() {
        io.stopMotor();
    }
}
