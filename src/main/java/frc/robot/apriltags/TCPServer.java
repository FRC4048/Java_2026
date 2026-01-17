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

public abstract class TCPServer<T> extends Thread {

  private final int port;
  private ServerSocket serverSocket;
  private Socket clientSocket;
  private DataInputStream dataInputStream;
  private BufferedInputStream bufferedInputStream;
  private boolean running = false;
  private final LinkedTransferQueue<T> readings = new LinkedTransferQueue<>();

  /**
   * @param port for network communication. Port must match between client and server (range
   *     5800-5810 (rio limitations))
   */
  public TCPServer(int port) {
    this.port = port;
  }

  @Override
  public void run() {
    try {
      this.serverSocket = new ServerSocket(port);
      this.serverSocket.setSoTimeout(Constants.SERVER_SOCKET_CONNECTION_TIMEOUT);
      this.clientSocket = null;
    } catch (IOException e2) {
      DriverStation.reportError("Could not start server on port " + port, true);
      this.serverSocket = null;
    }
    running = true;
    while (running) {
      if (serverSocket == null) {
        escape();
        continue;
      }
      if (this.clientSocket == null) {
        clientSocket = waitForClient();
        if (clientSocket == null) {
          continue;
        }
        try {
          bufferedInputStream = new BufferedInputStream(clientSocket.getInputStream());
          dataInputStream = new DataInputStream(bufferedInputStream);
        } catch (IOException e) {
          DriverStation.reportError("Could not fetch client Input Stream", true);
          dataInputStream = null;
          continue;
        }
      }
      try {
        readings.add(extractFromStream(dataInputStream));
      } catch (IOException e) {
        DriverStation.reportError("Problem while reading tcp stream data", true);
        clientSocket = null;
        bufferedInputStream = null;
        dataInputStream = null;
      }
    }
  }

  protected abstract T extractFromStream(DataInputStream stream) throws IOException;

  private Socket waitForClient() {
    Socket s;
    try {
      s = serverSocket.accept();
      CommandLogger.get().logMessage("TCP Client Connection Status", true);
    } catch (IOException e1) {
      CommandLogger.get().logMessage("TCP Client Connection Status", false);
      s = null;
    }
    try {
      Thread.sleep(Constants.SERVER_SOCKET_ATTEMPT_DELAY);
    } catch (InterruptedException e) {
      DriverStation.reportError("TCP Server thread interrupted: " + e.getMessage(), true);
    }
    return s;
  }

  public void escape() {
    this.running = false;
    try {
      if (bufferedInputStream != null) {
        bufferedInputStream.close();
      }
      if (dataInputStream != null) {
        dataInputStream.close();
      }
      if (clientSocket != null) {
        clientSocket.close();
      }
      if (serverSocket != null) {
        serverSocket.close();
      }
    } catch (IOException e) {
      DriverStation.reportError("Could not release thread resources!", true);
    } finally {
      CommandLogger.get().logMessage("TCP Client Connection Status", false);
    }
  }

  public Queue<T> flush() {
    Queue<T> queue = new LinkedList<>();
    readings.drainTo(queue);
    return queue;
  }
}