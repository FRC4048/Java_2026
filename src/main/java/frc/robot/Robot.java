// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoSource;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.autochooser.FieldLocation;
import frc.robot.constants.Constants;
import frc.robot.utils.diag.Diagnostics;
import frc.robot.utils.logging.TimeoutLogger;
import frc.robot.utils.logging.commands.CommandLogger;
import org.littletonrobotics.junction.LogFileUtil;
import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;
import org.littletonrobotics.junction.wpilog.WPILOGReader;
import org.littletonrobotics.junction.wpilog.WPILOGWriter;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The methods in this class are called automatically corresponding to each mode, as described in
 * the TimedRobot documentation. If you change the name of this class or the package after creating
 * this project, you must also update the Main.java file in the project.
 */
public class Robot extends LoggedRobot {
    private Command autonomousCommand;
    private static final Diagnostics diagnostics = new Diagnostics();
    private final RobotContainer robotContainer;
    private static final AtomicReference<RobotMode> mode = new AtomicReference<>(RobotMode.DISABLED);
    private double timerTime;
    private String autonomousWinner;
    private boolean hubActive;
    private static Alliance autoWinner;
    private final Timer hubTimer = new Timer();

    private static Optional<DriverStation.Alliance> allianceColor = Optional.empty();

    final CommandXboxController driverXbox = new CommandXboxController(0);

    /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     */
    public Robot() {
        // Set up data receivers & replay source
        switch (Constants.currentMode) {
            case REAL:
                // Running on a real robot, log to a USB stick ("/U/logs")
                Logger.addDataReceiver(new WPILOGWriter());
                Logger.addDataReceiver(new NT4Publisher());
                break;

            case SIM:
                // Running a physics simulator, log to NT
                Logger.addDataReceiver(new NT4Publisher());
                Logger.addDataReceiver(new WPILOGWriter());
                break;

            case REPLAY:
                // Replaying a log, set up replay source
                setUseTiming(false); // Run as fast as possible
                String logPath = LogFileUtil.findReplayLog();
                Logger.setReplaySource(new WPILOGReader(logPath));
                Logger.addDataReceiver(new WPILOGWriter(LogFileUtil.addPathSuffix(logPath, "_sim")));
                break;
        }

        // Start AdvantageKit logger
        Logger.start();
        CommandLogger.get().init();

        UsbCamera camera = CameraServer.startAutomaticCapture("DriverCam", 0);
        camera.setConnectionStrategy(VideoSource.ConnectionStrategy.kKeepOpen);
        CameraServer.addCamera(camera);

        // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
        // autonomous chooser on the dashboard.
        robotContainer = new RobotContainer();
    }

    public static RobotMode getMode() {
        return mode.get();
    }

    /**
     * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
     * that you want ran during disabled, autonomous, teleoperated and test.
     *
     * <p>This runs after the mode specific periodic functions, but before LiveWindow and
     * SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic() {
        // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
        // commands, running already-scheduled commands, removing finished or interrupted commands,
        // and running subsystem periodic() methods.  This must be called from the robot's periodic
        // block in order for anything in the Command-based framework to work.
        if (getMode() != RobotMode.TEST) {
            // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
            // commands, running already-scheduled commands, removing finished or interrupted commands,
            // and running subsystem periodic() methods.  This must be called from the robot's periodic
            // block in order for anything in the Command-based framework to work.
            CommandScheduler.getInstance().run();
        }

        Logger.recordOutput("shootingState", robotContainer.getShootingState().getShootState().toString());
        if (Constants.currentMode.equals(Constants.Mode.SIM)) {
            robotContainer.getRobotVisualizer().logMechanism();
        }

        if (Constants.ENABLE_LOGGING) {
            CommandLogger.get().log();
        }

        // Gets the alliance color.
        if (DriverStation.isDSAttached() && allianceColor.isEmpty()) {
            allianceColor = DriverStation.getAlliance();
        }

        if (Constants.DEBUG) {
            SmartDashboard.putNumber("driverXbox.getLeftY()", driverXbox.getLeftY());
            SmartDashboard.putNumber("driverXbox::getRightX", driverXbox.getRightX());
            if (!Constants.TESTBED) {
                Logger.recordOutput("MyPose", robotContainer.getDriveBase().getPose());
                    SmartDashboard.putNumber("Robot X", robotContainer.getDriveBase().getPose().getX());
            SmartDashboard.putNumber("Robot Y", robotContainer.getDriveBase().getPose().getY());
        // Puts data on the elastic dashboard
                SmartDashboard.putString("Alliance Color", Robot.allianceColorString());
                SmartDashboard.putBoolean("Hub Active?", hubActive());
            }

            // Puts data on the elastic dashboard
            SmartDashboard.putString("Alliance Color", Robot.allianceColorString());
            SmartDashboard.putBoolean("Hub Active?", hubActive());
        }
        SmartDashboard.putString("Selected Action",
                robotContainer.getAutoChooser().getCommandDescription());
    }

  /** This function is called once each time the robot enters Disabled mode. */
    @Override
    public void disabledInit() {
        mode.set(RobotMode.DISABLED);
        Logger.recordOutput("Timeouts/totalTimeouts", TimeoutLogger.getTotalTimeouts());
    }

    @Override
    public void disabledPeriodic() {
    }

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
    @Override
    public void autonomousInit() {
        //m_autonomousCommand = m_robotContainer.getAutonomousCommand();

        // schedule the autonomous command (example)
        mode.set(RobotMode.AUTONOMOUS);

    // Hub is always active during autonomous.
    hubActive = true;
    robotContainer.getDriveBase().resetOdometry(robotContainer.getAutoChooser().getFieldLocation().getLocation());
  }

  /** This function is called periodically during autonomous. */
    @Override
    public void autonomousPeriodic() {

        // schedule the autonomous command
        if (this.autonomousCommand == null) {
            autonomousCommand = robotContainer.getAutonomousCommand();
            if (autonomousCommand != null) {
                CommandScheduler.getInstance().schedule(autonomousCommand);
            }
        }
    }

    @Override
    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        diagnostics.reset();
        mode.set(RobotMode.TELEOP);
        if (autonomousCommand != null) {
            autonomousCommand.cancel();
        }
    }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    // Check who won autonomous.
    if (autonomousWinner == null) {
      determineAutonomousWinner();
      hubTimer.restart();
    } else {
      timerTime = hubTimer.get();
      determineHubActive();
      determineHubCountdown();
    }

    }

    private void determineAutonomousWinner() {
        autonomousWinner = DriverStation.getGameSpecificMessage();
        if (autonomousWinner != null) {
            autoWinner = switch (autonomousWinner.toUpperCase()) {
                case "R" -> Alliance.Red;
                case "B" -> Alliance.Blue;
                default -> null;
            };
        } else {
            hubActive = true; // If game data has not been recieved,
        }
        // it is transition period and the hub is active.
    }

    private void determineHubActive() {

        // Determine whether the hub is active.
        double timeSinceTeleop = timerTime();
        if (timeSinceTeleop < 0) return; // Teleop has not started.

        if (timeSinceTeleop >= Constants.ENDGAME_START) {
            hubActive = true; // Hub is always active during endgame and transition

        } else if (timeSinceTeleop >= Constants.SHIFT_4_START) {
            hubActive = (allianceColor.get() == autoWinner);
            // Only the hub of the team that won autonomous is active during shifts 2 and 4.
        } else if (timeSinceTeleop >= Constants.SHIFT_3_START) {
            hubActive = (allianceColor.get() != autoWinner);
            // Only the hub of the team that didn't win autonomous is active during shifts 1 and 3.
        } else if (timeSinceTeleop >= Constants.SHIFT_2_START) {
            hubActive = (allianceColor.get() == autoWinner);
        } else if (timeSinceTeleop >= Constants.SHIFT_1_START) {
            hubActive = (allianceColor.get() != autoWinner);
    } else hubActive = true; // transition
  }

  private void determineHubCountdown(){
    int hubCountdown = 0;
    // Determines time until next hub shift
    int timeSinceTeleop = (int) timerTime();
    if (timeSinceTeleop < 0) 
      hubCountdown = 0; // Match has not started.
      // Calculates the time between current time and next shift for every shift of the match
    if (timeSinceTeleop >= Constants.ENDGAME_START) {
      hubCountdown = 140 - timeSinceTeleop;
    } else if (timeSinceTeleop >= Constants.SHIFT_4_START) {
      hubCountdown = Constants.ENDGAME_START - timeSinceTeleop;
    } else if (timeSinceTeleop >= Constants.SHIFT_3_START) {
      hubCountdown = Constants.SHIFT_4_START - timeSinceTeleop;
    } else if (timeSinceTeleop >= Constants.SHIFT_2_START) {
      hubCountdown = Constants.SHIFT_3_START - timeSinceTeleop;
    } else if (timeSinceTeleop >= Constants.SHIFT_1_START) {
      hubCountdown = Constants.SHIFT_2_START - timeSinceTeleop;
    } else {hubCountdown = Constants.SHIFT_1_START - timeSinceTeleop;}
    SmartDashboard.putNumber("Countdown Until Next Hub Shift", hubCountdown);
    // Puts the countdown on dashboard so driver and operator can see the time until the next shift
  }

    @Override
    public void testInit() {
        diagnostics.reset();
        mode.set(RobotMode.TEST);
        // Cancels all running commands at the start of test mode.
        CommandScheduler.getInstance().cancelAll();
    }

    /** This function is called periodically during test mode. */
    @Override
    public void testPeriodic() {
        diagnostics.refresh();
    }

  /** This function is called once when the robot is first started up. */
    @Override
    public void simulationInit() {
    }

  /** This function is called periodically whilst in simulation. */
    @Override
    public void simulationPeriodic() {
    }

    public static Diagnostics getDiagnostics() {
        return diagnostics;
    }

    // Getters
    public boolean hubActive() {
        return hubActive;
    }

    public double timerTime(){
        return timerTime;
    }

    public static Optional<Alliance> allianceColor() {
        return allianceColor;
    }

    public static String allianceColorString() {
        return String.valueOf(allianceColor.orElse(null));
    }

    public FieldLocation location() {
        return robotContainer.getAutoChooser().getFieldLocation();
    }

    public Pose2d getStartingLocation() {
        return location().getLocation();
    }
}