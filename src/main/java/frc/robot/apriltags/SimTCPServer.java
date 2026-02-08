package frc.robot.apriltags;

import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.constants.Constants;
import frc.robot.utils.logging.commands.CommandLogger;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;

public class SimTCPServer extends TCPApriltagServer {
    public SimTCPServer(int port) {
        super(port);
    }
    @Override
    public void run() {}
}