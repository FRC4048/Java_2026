// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.swerve.SwerveDrivetrain;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.roller.SpinRoller;
import frc.robot.commands.tilt.TiltDown;
import frc.robot.commands.tilt.TiltUp;
import frc.robot.subsystems.RollerSubsystem;
import frc.robot.subsystems.TiltSubsystem;
import frc.robot.utils.simulation.RobotVisualizer;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private SwerveDrivetrain drivetrain;
  private final RollerSubsystem rollerSubsystem;
  private final TiltSubsystem tiltSubsystem;
  private RobotVisualizer robotVisualizer = null;
  private final CommandXboxController controller =
      new CommandXboxController(Constants.XBOX_CONTROLLER_ID);
  private final Joystick joyleft = new Joystick(Constants.LEFT_JOYSTICK_ID);
  private final Joystick joyright = new Joystick(Constants.RIGHT_JOYSTICK_ID);


  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    switch (Constants.currentMode) {
            case REAL -> {
                rollerSubsystem = new RollerSubsystem(RollerSubsystem.createRealIo());
                tiltSubsystem = new TiltSubsystem(TiltSubsystem.createRealIo());
            }
            case REPLAY -> {
                rollerSubsystem = new RollerSubsystem(RollerSubsystem.createMockIo());
                tiltSubsystem = new TiltSubsystem(TiltSubsystem.createMockIo());
            }
            case SIM -> {
                robotVisualizer = new RobotVisualizer();
                rollerSubsystem = new RollerSubsystem(RollerSubsystem.createSimIo(robotVisualizer));
                tiltSubsystem = new TiltSubsystem(TiltSubsystem.createSimIo(robotVisualizer));
            }
            default -> {
                throw new RuntimeException("Did not specify Robot Mode");
            }
        }
    configureBindings();
    putShuffleboardCommands();
  }

  private void configureBindings() {
    JoystickButton joyLeft2 = new JoystickButton(joyleft, 2);
    JoystickButton joyRight1 = new JoystickButton(joyright, 1);
  }

  public void putShuffleboardCommands() {
        if (Constants.DEBUG) {
            SmartDashboard.putData(
                    "Spin Roller",
                    new SpinRoller(rollerSubsystem));

            SmartDashboard.putData(
                    "Tilt Up",
                    new TiltUp(tiltSubsystem));

            SmartDashboard.putData(
                    "Tilt Down",
                    new TiltDown(tiltSubsystem));
        }
    }
  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return null;
  }
  public RobotVisualizer getRobotVisualizer() {
    return robotVisualizer;
  }
}


