package frc.robot.apriltags;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.Vector;
import edu.wpi.first.math.geometry.*;
import edu.wpi.first.math.numbers.N3;
import frc.robot.constants.Constants;
import frc.robot.subsystems.swervedrive.vision.truster.VisionMeasurement;
import frc.robot.subsystems.swervedrive.vision.truster.VisionTruster;
import frc.robot.utils.Apriltag;
import frc.robot.utils.logging.io.BaseIoImpl;
import java.util.Queue;
import java.util.Random;
import java.util.function.Supplier;

import org.littletonrobotics.junction.Logger;

public class SimApriltagIO extends TCPApriltagIo {
    public SimApriltagIO(String name, ApriltagInputs inputs, SimTCPServer server) {
        super(name, inputs, server);
    }
}