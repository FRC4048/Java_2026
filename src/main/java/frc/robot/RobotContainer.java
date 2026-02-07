// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.hopper.SpinHopper;
import frc.robot.commands.drive.DriveDirectionTime;
import frc.robot.commands.feeder.SpinFeeder;
import frc.robot.commands.intake.SpinIntake;
import frc.robot.commands.angler.AimAngler;
import frc.robot.commands.shooter.SetShootingState;
import frc.robot.constants.Constants;
import frc.robot.subsystems.AnglerSubsystem;
import frc.robot.subsystems.HopperSubsystem;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.constants.ShootingState;
import frc.robot.constants.ShootingState.ShootState;
import frc.robot.subsystems.GyroSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
//import frc.robot.subsystems.RollerSubsystem;
//import frc.robot.subsystems.TiltSubsystem;
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
    private final AnglerSubsystem anglerSubsystem;
    private final IntakeSubsystem intakeSubsystem;
    private final FeederSubsystem feederSubsystem;
    private RobotVisualizer robotVisualizer = null;
    private final HopperSubsystem hopperSubsystem;
    private SwerveSubsystem drivebase = null;
    private GyroSubsystem gyroSubsystem = null;
    private final CommandJoystick driveJoystick = new CommandJoystick(Constants.DRIVE_JOYSTICK_PORT);
    private final CommandJoystick steerJoystick = new CommandJoystick(Constants.STEER_JOYSTICK_PORT);
    private ShootingState shootState = new ShootingState(ShootState.SHOOTING_HUB);

    // Replace with CommandPS4Controller or CommandJoystick if needed
    //new CommandXboxController(OperatorConstants.kDriverControllerPort);private final CommandXboxController controller = new CommandXboxController(Constants.XBOX_CONTROLLER_PORT);

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        // Configure the trigger bindings
        switch (Constants.currentMode) {
            case REAL -> {
                //rollerSubsystem = new RollerSubsystem(RollerSubsystem.createRealIo());
                //tiltSubsystem = new TiltSubsystem(TiltSubsystem.createRealIo());
                anglerSubsystem = new AnglerSubsystem(AnglerSubsystem.createRealIo());
                intakeSubsystem = new IntakeSubsystem(IntakeSubsystem.createRealIo(), IntakeSubsystem.createRealDeploymentSwitch());
                hopperSubsystem = new HopperSubsystem(HopperSubsystem.createRealIo());

                feederSubsystem = new FeederSubsystem(FeederSubsystem.createRealIo());

                RealGyroIo gyroIo = (RealGyroIo) GyroSubsystem.createRealIo();
                ThreadedGyro threadedGyro = gyroIo.getThreadedGyro();
                gyroSubsystem = new GyroSubsystem(gyroIo);
                SwerveIMU swerveIMU = new ThreadedGyroSwerveIMU(threadedGyro);
                
                drivebase = !Constants.TESTBED ? new SwerveSubsystem(new File(Filesystem.getDeployDirectory(), "YAGSL"), swerveIMU) : null;
            }
            case REPLAY -> {
                //rollerSubsystem = new RollerSubsystem(RollerSubsystem.createMockIo());
                //tiltSubsystem = new TiltSubsystem(TiltSubsystem.createMockIo());
                anglerSubsystem = new AnglerSubsystem(AnglerSubsystem.createMockIo());
                intakeSubsystem = new IntakeSubsystem(IntakeSubsystem.createMockIo(), IntakeSubsystem.createMockDeploymentSwitch());
                hopperSubsystem = new HopperSubsystem(HopperSubsystem.createMockIo());
                feederSubsystem = new FeederSubsystem(FeederSubsystem.createMockIo());
                // No GyroSubsystem in REPLAY for now
                // create the drive subsystem with null gyro (use default json)
                drivebase = !Constants.TESTBED ? new SwerveSubsystem(new File(Filesystem.getDeployDirectory(), "YAGSL"), null) : null;
            }
            case SIM -> {
                robotVisualizer = new RobotVisualizer();
                //rollerSubsystem = new RollerSubsystem(RollerSubsystem.createSimIo(robotVisualizer));
                //tiltSubsystem = new TiltSubsystem(TiltSubsystem.createSimIo(robotVisualizer));
                anglerSubsystem = new AnglerSubsystem(AnglerSubsystem.createSimIo(robotVisualizer));
                intakeSubsystem = new IntakeSubsystem(IntakeSubsystem.createSimIo(robotVisualizer), IntakeSubsystem.createSimDeploymentSwitch());
                hopperSubsystem = new HopperSubsystem(HopperSubsystem.createSimIo(robotVisualizer));
                feederSubsystem = new FeederSubsystem(FeederSubsystem.createSimIo(robotVisualizer));
                // No GyroSubsystem in REPLAY for now
                // create the drive subsystem with null gyro (use default json)
                drivebase = !Constants.TESTBED ? new SwerveSubsystem(new File(Filesystem.getDeployDirectory(), "YAGSL"), null) : null;
            }

            default -> {
                throw new RuntimeException("Did not specify Robot Mode");
            }
        }

        anglerSubsystem.setDefaultCommand(new AimAngler(
                anglerSubsystem,
                () -> drivebase != null ? drivebase.getPose() : null,
                shootState));

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
        if(!Constants.TESTBED){
            SwerveInputStream driveAngularVelocity = SwerveInputStream.of(drivebase.getSwerveDrive(),
                            () -> driveJoystick.getY() * -1,
                            () -> driveJoystick.getX() * -1)
                    .withControllerRotationAxis(steerJoystick::getX)
                    .deadband(Constants.DEADBAND)
                    .scaleTranslation(0.8)
                    .allianceRelativeControl(true);
            SwerveInputStream driveRobotOriented = driveAngularVelocity.copy().robotRelative(true)
                    .allianceRelativeControl(false);
            Command driveFieldOrientedAnglularVelocity = drivebase.driveFieldOriented(driveAngularVelocity);
            drivebase.setDefaultCommand(driveFieldOrientedAnglularVelocity);
        }
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
                new TiltDown(tiltSubsystem));*/
          
            // TODO: These commands do not REQUIRE the subsystem therefore cannot be used in production
            SmartDashboard.putData(
                    "Intake/Spin Forward",
                    new InstantCommand(() -> intakeSubsystem.setSpeed(1.0)));

            SmartDashboard.putData(
                    "Intake/Spin Backward",
                    new InstantCommand(() -> intakeSubsystem.setSpeed(-1.0)));

            SmartDashboard.putData(
                    "Intake/Stop",
                    new InstantCommand(intakeSubsystem::stopMotors));

            SmartDashboard.putNumber("angler/TargetRotations", Constants.ANGLER_HOME_ROTATIONS);

            SmartDashboard.putData(
                    "angler/Set Position",
                    new InstantCommand(() -> anglerSubsystem.setPosition(
                            SmartDashboard.getNumber("angler/TargetRotations", 0.0))));

            SmartDashboard.putData(
                    "angler/Go Home",
                    new InstantCommand(() -> anglerSubsystem.setPosition(Constants.ANGLER_HOME_ROTATIONS)));

            SmartDashboard.putData(
                    "angler/Go Low",
                    new InstantCommand(() -> anglerSubsystem.setPosition(Constants.ANGLER_LOW_ROTATIONS)));

            SmartDashboard.putData(
                    "angler/Go High",
                    new InstantCommand(() -> anglerSubsystem.setPosition(Constants.ANGLER_HIGH_ROTATIONS)));

            SmartDashboard.putData(
                    "Spin Intake",
                    new SpinIntake(intakeSubsystem));
            
            SmartDashboard.putData(
                    "Start Hopper",
                    new SpinHopper(hopperSubsystem));
            
            SmartDashboard.putData(
                    "Spin Feeder",
                    new SpinFeeder(feederSubsystem));

            SmartDashboard.putData(
                    "Shooting State: Stopped",
                    new SetShootingState(shootState, ShootState.STOPPED));
            
            SmartDashboard.putData(
                    "Shooting State: Fixed",
                    new SetShootingState(shootState, ShootState.FIXED));

            SmartDashboard.putData(
                    "Shooting State: Into Hub",
                    new SetShootingState(shootState, ShootState.SHOOTING_HUB));

            SmartDashboard.putData(
                    "Shooting State: Shuttling",
                    new SetShootingState(shootState, ShootState.SHUTTLING));
            
        }
    //basic drive command
        if(!Constants.TESTBED){
            Command driveDirectionTime = new DriveDirectionTime(drivebase, 0.1,0.1, true, 1);
            SmartDashboard.putData("Drive Command", driveDirectionTime);
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

    public IntakeSubsystem getIntakeSubsystem() {
        return intakeSubsystem;
    }
    public SwerveSubsystem getDriveBase(){
      return drivebase;
    }

    public ShootingState getShootingState() {
        return shootState;
    }
}
