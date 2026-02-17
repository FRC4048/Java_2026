package frc.robot.apriltags;

public class SimApriltagIO extends TCPApriltagIo {
    public SimApriltagIO(String name, ApriltagInputs inputs, SimTCPServer server) {
        super(name, inputs, server);
    }
}