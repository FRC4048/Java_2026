package frc.robot.apriltags;

public class SimTCPServer extends TCPApriltagServer {
    public SimTCPServer(int port) { //port doesnt matter at all
        super(port);
    }
    @Override
    public void run() {}
}