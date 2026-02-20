package frc.robot.apriltags;

import edu.wpi.first.math.geometry.*;
import frc.robot.constants.Constants;
import frc.robot.utils.Apriltag;
import frc.robot.utils.logging.io.BaseIoImpl;
import java.util.Queue;

import org.littletonrobotics.junction.Logger;

public class SimApriltagIO extends TCPApriltagIo {
    public SimApriltagIO(String name, ApriltagInputs inputs, SimTCPServer server) {
        super(name, inputs, server);
    }
}