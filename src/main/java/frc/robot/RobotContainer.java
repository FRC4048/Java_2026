// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.intake.SpinIntake;
import frc.robot.subsystems.GyroSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.io.gyro.RealGyroIo;
import frc.robot.utils.logging.io.gyro.ThreadedGyro;
import frc.robot.utils.logging.io.gyro.ThreadedGyroSwerveIMU;
import frc.robot.utils.simulation.RobotVisualizer;
import swervelib.SwerveInputStream;
import swervelib.imu.SwerveIMU;

import java.io.File;

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
    private SwerveSubsystem drivebase = null;
    private final CommandJoystick driveJoystick = new CommandJoystick(Constants.DRIVE_JOYSTICK_PORT);
    private final CommandJoystick steerJoystick = new CommandJoystick(Constants.STEER_JOYSTICK_PORT);

    // Replace with CommandPS4Controller or CommandJoystick if needed
    //new CommandXboxController(OperatorConstants.kDriverControllerPort);

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        // Configure the trigger bindings
        switch (Constants.currentMode) {
            case REAL -> {
                //rollerSubsystem = new RollerSubsystem(RollerSubsystem.createRealIo());
                //tiltSubsystem = new TiltSubsystem(TiltSubsystem.createRealIo());
                intakeSubsystem = new IntakeSubsystem(IntakeSubsystem.createRealIo(), new DigitalInput(Constants.INTAKE_DIGITAL_INPUT_CHANNEL));
                // For now, we are not creating an instance of the gyro subsystem - just the threadedIO...
                RealGyroIo gyroIo = (RealGyroIo) GyroSubsystem.createRealIo();
                ThreadedGyro threadedGyro = gyroIo.getThreadedGyro();
                SwerveIMU swerveIMU = new ThreadedGyroSwerveIMU(threadedGyro);
                drivebase = new SwerveSubsystem(new File(Filesystem.getDeployDirectory(), "YAGSL"), swerveIMU);
            }
            case REPLAY -> {
                //rollerSubsystem = new RollerSubsystem(RollerSubsystem.createMockIo());
                //tiltSubsystem = new TiltSubsystem(TiltSubsystem.createMockIo());
                intakeSubsystem = new IntakeSubsystem(IntakeSubsystem.createMockIo(), new DigitalInput(Constants.INTAKE_DIGITAL_INPUT_CHANNEL));
                // create the drive subsystem with null gyro (use default json)
                drivebase = new SwerveSubsystem(new File(Filesystem.getDeployDirectory(), "YAGSL"), null);
            }
            case SIM -> {
                robotVisualizer = new RobotVisualizer();
                //rollerSubsystem = new RollerSubsystem(RollerSubsystem.createSimIo(robotVisualizer));
                //tiltSubsystem = new TiltSubsystem(TiltSubsystem.createSimIo(robotVisualizer));
                intakeSubsystem = new IntakeSubsystem(IntakeSubsystem.createSimIo(robotVisualizer), new DigitalInput(Constants.INTAKE_DIGITAL_INPUT_CHANNEL));
                // create the drive subsystem with null gyro (use default json)
                drivebase = new SwerveSubsystem(new File(Filesystem.getDeployDirectory(), "YAGSL"), null);
            }

            default -> {
                throw new RuntimeException("Did not specify Robot Mode");
            }
        }
        configureBindings();
        putShuffleboardCommands();
    }

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
        // TODO: Clean this up a little - create command in method and only create the one actually needed
        SwerveInputStream driveAngularVelocity = SwerveInputStream.of(drivebase.getSwerveDrive(),
                        () -> driveJoystick.getY() * -1,
                        () -> driveJoystick.getX() * -1)
                .withControllerRotationAxis(steerJoystick::getX)
                .deadband(Constants.DEADBAND)
                .scaleTranslation(0.8)
                .allianceRelativeControl(true);
        SwerveInputStream driveRobotOriented = driveAngularVelocity.copy().robotRelative(true)
                .allianceRelativeControl(false);
        Command driveRobotOrientedAngularVelocity = drivebase.driveFieldOriented(driveRobotOriented);
        drivebase.setDefaultCommand(driveRobotOrientedAngularVelocity);
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
}
