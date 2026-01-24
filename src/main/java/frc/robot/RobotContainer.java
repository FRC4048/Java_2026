// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.io.File;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.drive.Drive;
import frc.robot.commands.intake.SpinIntake;
import frc.robot.commands.roller.SpinRoller;
import frc.robot.commands.tilt.TiltDown;
import frc.robot.commands.tilt.TiltUp;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.RollerSubsystem;
import frc.robot.subsystems.TiltSubsystem;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.simulation.RobotVisualizer;
import swervelib.SwerveInputStream;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  //private final RollerSubsystem rollerSubsystem;
  //private final TiltSubsystem tiltSubsystem;
  private final IntakeSubsystem intakeSubsystem;
  private RobotVisualizer robotVisualizer = null;
  private final SwerveSubsystem drivebase = new SwerveSubsystem(new File(Filesystem.getDeployDirectory(),"YAGSL"));
  private final CommandJoystick driveJoystick = new CommandJoystick(Constants.DRIVE_JOYSTICK_PORT);
  private final CommandJoystick steerJoystick = new CommandJoystick(Constants.STEER_JOYSTICK_PORT);

  // Replace with CommandPS4Controller or CommandJoystick if needed
      //new CommandXboxController(OperatorConstants.kDriverControllerPort);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    switch (Constants.currentMode) {
            case REAL -> {
                //rollerSubsystem = new RollerSubsystem(RollerSubsystem.createRealIo());
                //tiltSubsystem = new TiltSubsystem(TiltSubsystem.createRealIo());
                intakeSubsystem = new IntakeSubsystem(IntakeSubsystem.createRealIo(), new DigitalInput(Constants.INTAKE_DIGITAL_INPUT_CHANNEL));
            }
            case REPLAY -> {
                //rollerSubsystem = new RollerSubsystem(RollerSubsystem.createMockIo());
                //tiltSubsystem = new TiltSubsystem(TiltSubsystem.createMockIo());
                intakeSubsystem = new IntakeSubsystem(IntakeSubsystem.createMockIo(), new DigitalInput(Constants.INTAKE_DIGITAL_INPUT_CHANNEL));
            }
            case SIM -> {
                robotVisualizer = new RobotVisualizer();
               //rollerSubsystem = new RollerSubsystem(RollerSubsystem.createSimIo(robotVisualizer));
               //tiltSubsystem = new TiltSubsystem(TiltSubsystem.createSimIo(robotVisualizer));
                intakeSubsystem = new IntakeSubsystem(IntakeSubsystem.createSimIo(robotVisualizer), new DigitalInput(Constants.INTAKE_DIGITAL_INPUT_CHANNEL));
            }
            
            default -> {
                throw new RuntimeException("Did not specify Robot Mode");
            }
       }
    configureBindings();
    putShuffleboardCommands();
  }
  SwerveInputStream driveAngularVelocity = SwerveInputStream.of(drivebase.getSwerveDrive(),
                                                                () -> driveJoystick.getY(),
                                                                () -> driveJoystick.getX())
                                                            .withControllerRotationAxis(steerJoystick::getX)
                                                            .deadband(Constants.DEADBAND)
                                                            .scaleTranslation(0.8)
                                                            .allianceRelativeControl(true);

  
  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    // Schedule `ExampleCommand` when `exampleCondition` changes to `true`
    //new Trigger(m_exampleSubsystem::exampleCondition)
      //  .onTrue(new ExampleCommand(m_exampleSubsystem));

    // Schedule `exampleMethodCommand` when the Xbox controller's B button is pressed,
    // cancelling on release.
   // m_driverController.b().whileTrue(m_exampleSubsystem.exampleMethodCommand());
    Command driveFieldOrientedAnglularVelocity = drivebase.driveFieldOriented(driveAngularVelocity);
    drivebase.setDefaultCommand(driveFieldOrientedAnglularVelocity);
    
    //basic command
    Pose2d pose = new Pose2d(new Translation2d(1,1),new Rotation2d(5));
    Command drive = new Drive(drivebase, pose, true);
    driveJoystick.button(1).whileTrue(drive);
  }
  public void putShuffleboardCommands() {
        if (Constants.DEBUG) {
            /*SmartDashboard.putData(
                    "Spin Roller",
                    new SpinRoller(rollerSubsystem));

            SmartDashboard.putData(
                    "Tilt Up",
                    new TiltUp(tiltSubsystem));

            SmartDashboard.putData(
                    "Tilt Down",
                    new TiltDown(tiltSubsystem));
          */
            SmartDashboard.putData(
                    "Spin Intake",
                    new SpinIntake(intakeSubsystem));
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
  public SwerveSubsystem getdrivebase(){
    return drivebase;
  }
}

