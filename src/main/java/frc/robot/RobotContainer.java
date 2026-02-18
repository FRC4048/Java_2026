// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.AddTunableApriltagReading;
import frc.robot.commands.AddApriltagReading;
import frc.robot.commands.climber.ClimberDown;
import frc.robot.commands.climber.ClimberUp;
import frc.robot.commands.hopper.SpinHopper;
import frc.robot.commands.drive.DriveDirectionTime;
import frc.robot.commands.feeder.SpinFeeder;
import frc.robot.commands.drive.FakeVision;
import frc.robot.commands.intake.SpinIntake;
import frc.robot.commands.intakeDeployment.InitalRunDeployment;
import frc.robot.commands.intakeDeployment.SetDeploymentState;
import frc.robot.autochooser.AutoChooser;
import frc.robot.commands.angler.AimAngler;
import frc.robot.commands.angler.RunAnglerToReverseLimit;
import frc.robot.commands.auto.ExampleAuto;
import frc.robot.commands.shooter.SetShootingState;
import frc.robot.commands.turret.RunTurretToFwdLimit;
import frc.robot.commands.turret.RunTurretToRevLimit;
import frc.robot.commands.turret.SetTurretAngle;
import frc.robot.commands.shooter.SpinShooter;
import frc.robot.constants.Constants;
import frc.robot.constants.enums.DeploymentState;
import frc.robot.constants.enums.ShootingState;
import frc.robot.constants.enums.ShootingState.ShootState;
import frc.robot.subsystems.AnglerSubsystem;
import frc.robot.subsystems.ApriltagSubsystem;
import frc.robot.subsystems.HopperSubsystem;
import frc.robot.subsystems.IntakeDeployerSubsystem;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.GyroSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.ClimberSubsystem;
//import frc.robot.subsystems.RollerSubsystem;
//import frc.robot.subsystems.TiltSubsystem;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.io.gyro.RealGyroIo;
import frc.robot.utils.logging.io.gyro.ThreadedGyro;
import frc.robot.utils.logging.io.gyro.ThreadedGyroSwerveIMU;
import frc.robot.utils.simulation.RobotVisualizer;
import swervelib.SwerveInputStream;
import swervelib.imu.SwerveIMU;
import frc.robot.utils.logging.io.BaseIoImpl;
import frc.robot.apriltags.ApriltagInputs;
import frc.robot.apriltags.ApriltagReading;
import frc.robot.apriltags.MockApriltagIo;
import frc.robot.apriltags.TCPApriltagIo;

import java.io.File;

import choreo.auto.AutoFactory;
import choreo.auto.AutoRoutine;
import choreo.auto.AutoTrajectory;

import choreo.auto.AutoFactory;
import choreo.auto.AutoRoutine;
import choreo.auto.AutoTrajectory;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
        // Instantiate the autochooser.
        private final AutoChooser autoChooser = new AutoChooser();
        // The robot's subsystems and commands are defined here...
        // private final TiltSubsystem tiltSubsystem;
        private final AnglerSubsystem anglerSubsystem;
        private final IntakeSubsystem intakeSubsystem;
        private final FeederSubsystem feederSubsystem;
        private final ApriltagSubsystem apriltagSubsystem;
        private final ShooterSubsystem shooterSubsystem;
        private RobotVisualizer robotVisualizer = null;
        private final HopperSubsystem hopperSubsystem;
        private final ClimberSubsystem climberSubsystem;
    private final TurretSubsystem turretSubsystem;
        private final IntakeDeployerSubsystem intakeDeployer;
        private SwerveSubsystem drivebase = null;
        private GyroSubsystem gyroSubsystem = null;
        private final CommandJoystick driveJoystick = new CommandJoystick(Constants.DRIVE_JOYSTICK_PORT);
        private final CommandJoystick steerJoystick = new CommandJoystick(Constants.STEER_JOYSTICK_PORT);
        private ShootingState shootState = new ShootingState(ShootState.STOPPED);
        private Drive drive;
        private AutoFactory autoFactory;
        private static AutoRoutine straightRoutine;
        private static AutoTrajectory straightTrajectory;

        // Replace with CommandPS4Controller or CommandJoystick if needed
        // new CommandXboxController(OperatorConstants.kDriverControllerPort);private
        // final CommandXboxController controller = new
        // CommandXboxController(Constants.XBOX_CONTROLLER_PORT);

        /**
         * The container for the robot. Contains subsystems, OI devices, and commands.
         */
        public RobotContainer() {
                // Configure the trigger bindings
                switch (Constants.currentMode) {
                        case REAL -> {
                                // rollerSubsystem = new RollerSubsystem(RollerSubsystem.createRealIo());
                                // tiltSubsystem = new TiltSubsystem(TiltSubsystem.createRealIo());
                                anglerSubsystem = new AnglerSubsystem(AnglerSubsystem.createRealIo());
                                intakeSubsystem = new IntakeSubsystem(IntakeSubsystem.createRealIo(),
                                                IntakeSubsystem.createRealDeploymentSwitch());
                                hopperSubsystem = new HopperSubsystem(HopperSubsystem.createRealIo());
                                intakeDeployer = new IntakeDeployerSubsystem(IntakeDeployerSubsystem.createRealIo());
                                turretSubsystem = new TurretSubsystem(TurretSubsystem.createRealIo());



                                climberSubsystem = new ClimberSubsystem(ClimberSubsystem.createRealIo());
                                feederSubsystem = new FeederSubsystem(FeederSubsystem.createRealIo());
                                shooterSubsystem = new ShooterSubsystem(ShooterSubsystem.createRealIo());
                                apriltagSubsystem = new ApriltagSubsystem(ApriltagSubsystem.createRealIo());
                                RealGyroIo gyroIo = (RealGyroIo) GyroSubsystem.createRealIo();
                                ThreadedGyro threadedGyro = gyroIo.getThreadedGyro();
                                gyroSubsystem = new GyroSubsystem(gyroIo);
                                SwerveIMU swerveIMU = new ThreadedGyroSwerveIMU(threadedGyro);

                                drivebase = !Constants.TESTBED ? new SwerveSubsystem(
                                                new File(Filesystem.getDeployDirectory(), "YAGSL"), swerveIMU) : null;
                        }
                        case REPLAY -> {
                                // rollerSubsystem = new RollerSubsystem(RollerSubsystem.createMockIo());
                                // tiltSubsystem = new TiltSubsystem(TiltSubsystem.createMockIo());
                                anglerSubsystem = new AnglerSubsystem(AnglerSubsystem.createMockIo());
                                intakeSubsystem = new IntakeSubsystem(IntakeSubsystem.createMockIo(),
                                                IntakeSubsystem.createMockDeploymentSwitch());
                                hopperSubsystem = new HopperSubsystem(HopperSubsystem.createMockIo());
                                climberSubsystem = new ClimberSubsystem(ClimberSubsystem.createMockIo());
                                feederSubsystem = new FeederSubsystem(FeederSubsystem.createMockIo());
                                turretSubsystem = new TurretSubsystem(TurretSubsystem.createMockIo());
                apriltagSubsystem = new ApriltagSubsystem(ApriltagSubsystem.createMockIo());
                                shooterSubsystem = new ShooterSubsystem(ShooterSubsystem.createMockIo());
                                intakeDeployer = new IntakeDeployerSubsystem(IntakeDeployerSubsystem.createMockIo());
                                // No GyroSubsystem in REPLAY for now
                                // create the drive subsystem with null gyro (use default json)
                                drivebase = !Constants.TESTBED ? new SwerveSubsystem(
                                                new File(Filesystem.getDeployDirectory(), "YAGSL"), null) : null;
                        }
                        case SIM -> {
                                robotVisualizer = new RobotVisualizer();
                                // rollerSubsystem = new
                                // RollerSubsystem(RollerSubsystem.createSimIo(robotVisualizer));
                                // tiltSubsystem = new
                                // TiltSubsystem(TiltSubsystem.createSimIo(robotVisualizer));
                                anglerSubsystem = new AnglerSubsystem(AnglerSubsystem.createSimIo(robotVisualizer));
                                intakeSubsystem = new IntakeSubsystem(IntakeSubsystem.createSimIo(robotVisualizer),
                                                IntakeSubsystem.createSimDeploymentSwitch());
                                hopperSubsystem = new HopperSubsystem(HopperSubsystem.createSimIo(robotVisualizer));
                                climberSubsystem = new ClimberSubsystem(ClimberSubsystem.createSimIo(robotVisualizer));
                                feederSubsystem = new FeederSubsystem(FeederSubsystem.createSimIo(robotVisualizer));
                                turretSubsystem = new TurretSubsystem(TurretSubsystem.createSimIo(robotVisualizer));
                apriltagSubsystem = new ApriltagSubsystem(ApriltagSubsystem.createSimIo());
                                shooterSubsystem = new ShooterSubsystem(ShooterSubsystem.createSimIo(robotVisualizer));
                                intakeDeployer = new IntakeDeployerSubsystem(
                                                IntakeDeployerSubsystem.createSimIo(robotVisualizer));

                                // No GyroSubsystem in REPLAY for now
                                // create the drive subsystem with null gyro (use default json)
                                drivebase = !Constants.TESTBED ? new SwerveSubsystem(
                                                new File(Filesystem.getDeployDirectory(), "YAGSL"), null) : null;
                        }

                        default -> {
                                throw new RuntimeException("Did not specify Robot Mode");
                        }
                }

                configureBindings();
                putShuffleboardCommands();
                setUpAutoFactory();
        }

        /**
         * Use this method to define your trigger->command mappings. Triggers can be
         * created via the
         * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with
         * an arbitrary
         * predicate, or via the named factories in {@link
         * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for
         * {@link
         * CommandXboxController
         * Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
         * PS4} controllers or
         * {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
         * joysticks}.
         */
        private void setUpAutoFactory() {

                drive = new Drive(drivebase);

                // Sets up Choreo with pose, odometry, drivebase, and a follow trajectory
                // command
                autoFactory = new AutoFactory(drivebase::getPose,
                                drivebase::resetOdometry,
                                drive::followTrajectory,
                                true,
                                drivebase);

                // example implementation of autoRoutine
                if (false) {
                        // Uses autofactory to create a new routine
                        straightRoutine = autoFactory.newRoutine("StraightRoutine");

                        /*
                         * Loads a trajectory created in Choreo given the name
                         * Can load multiple trajectories from the same routine
                         * 
                         * i.e.
                         * AutoRoutine routine = autoFactory.newRoutine("grabAndScore");
                         * AutoTrajectory grabTraj = routine.trajectory("grabPiece");
                         * AutoTrajectory scoreTraj = routine.trajectory("scorePiece");
                         */
                        straightTrajectory = straightRoutine.trajectory("StraightPath");

                        /*
                         * .active() is a trigger that becomes true when the routine is running
                         * .onTrue() starts a command when the trigger becomes true (i.e. when the
                         * routine starts)
                         * 
                         * Use commands.sequence() to sequence multiple commands (i.e. reset odometry,
                         * then follow trajectory)
                         */
                        straightRoutine.active().onTrue(
                                        straightTrajectory.resetOdometry()
                                                        .andThen(straightTrajectory.cmd()));

                        /*
                         * -----------------------------------------------------------------------------
                         * -------------------
                         * Trajectory Triggers (read more on docs page
                         * https://choreo.autos/choreolib/auto-factory/):
                         * -----------------------------------------------------------------------------
                         * -------------------
                         * 
                         * trajectory.atTime(String)
                         * trajectory.atTime(double time)
                         * trajectory.done()
                         * trajectory.active()
                         * trajectory.inactive()
                         * trajectory.atPose(String, double, double)
                         * trajectory.atPose(Pose2d, double, double)
                         * trajectory.doneDelayed(int)
                         * trajectory.doneFor(int)
                         * trajectory.recentlyDone()
                         */
                }
        }

        private void configureBindings() {
                // Schedule `ExampleCommand` when `exampleCondition` changes to `true`
                // new Trigger(m_exampleSubsystem::exampleCondition)
                // .onTrue(new ExampleCommand(m_exampleSubsystem));

                // Schedule `exampleMethodCommand` when the Xbox controller's B button is
                // pressed,
                // cancelling on release.
                // m_driverController.b().whileTrue(m_exampleSubsystem.exampleMethodCommand());
                // TODO: Clean this up a little - create command in method and only create the
                // one actually needed

                // example default command for angler- disabled for now
                if (false) {
                        new AimAngler(
                                        anglerSubsystem,
                                        () -> drivebase != null ? drivebase.getPose() : null,
                                        shootState);
                }

                if (!Constants.TESTBED) {
                        SwerveInputStream driveAngularVelocity = SwerveInputStream.of(drivebase.getSwerveDrive(),
                                        () -> driveJoystick.getY() * -1,
                                        () -> driveJoystick.getX() * -1)
                                        .withControllerRotationAxis(steerJoystick::getX)
                                        .deadband(Constants.DEADBAND)
                                        .scaleTranslation(0.8)
                                        .allianceRelativeControl(true);
                        Command driveFieldOrientedAnglularVelocity = drivebase.driveFieldOriented(driveAngularVelocity);
                        drivebase.setDefaultCommand(driveFieldOrientedAnglularVelocity);
                }
        }

        public void putShuffleboardCommands() {
                if (Constants.DEBUG) {

                        /*
                         * SmartDashboard.putData(
                         * "Spin Roller",
                         * new SpinRoller(rollerSubsystem));
                         * 
                         * SmartDashboard.putData(
                         * "Tilt Up",
                         * new TiltUp(tiltSubsystem));
                         * 
                         * SmartDashboard.putData(
                         * "Tilt Down",
                         * new TiltDown(tiltSubsystem));
                         */

                        // TODO: These commands do not REQUIRE the subsystem therefore cannot be used in
                        // production
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

                        SmartDashboard.putNumber("angler/TargetAngle", 0);

                        SmartDashboard.putData(
                                        "angler/Set Position",
                                        new InstantCommand(() -> anglerSubsystem.setPosition(
                                                        SmartDashboard.getNumber("angler/TargetRotations", 0.0))));

                        SmartDashboard.putData(
                                        "angler/Set Angle",
                                        new InstantCommand(() -> anglerSubsystem.setAngle(
                                                        SmartDashboard.getNumber("angler/TargetAngle", 0.0))));

                        SmartDashboard.putData(
                                        "angler/Go Home",
                                        new InstantCommand(() -> anglerSubsystem
                                                        .setPosition(Constants.ANGLER_HOME_ROTATIONS)));

                        SmartDashboard.putData(
                                        "angler/Go Low",
                                        new InstantCommand(() -> anglerSubsystem
                                                        .setPosition(Constants.ANGLER_ENCODER_LOW)));

                        SmartDashboard.putData(
                                        "angler/Go High",
                                        new InstantCommand(() -> anglerSubsystem
                                                        .setPosition(Constants.ANGLER_ENCODER_HIGH)));

                        SmartDashboard.putData(
                                        "angler/Run To Fwd Limit",
                                        new RunCommand(anglerSubsystem::runForward, anglerSubsystem)
                                                        .until(anglerSubsystem::isAtForwardLimit));

                        SmartDashboard.putData(
                                        "angler/Run To Rev Limit",
                                        new RunCommand(anglerSubsystem::runReverse, anglerSubsystem)
                                                        .until(anglerSubsystem::isAtReverseLimit));

                        SmartDashboard.putData(
                                        "angler/Reset Encoder",
                                        new InstantCommand(anglerSubsystem::resetEncoderToZero));

                        SmartDashboard.putData(
                                        "angler/Home Rev (Reset)",
                                        new RunAnglerToReverseLimit(anglerSubsystem));

            SmartDashboard.putData(
                    "turret/Turret Go 45",
                    new SetTurretAngle(turretSubsystem, 45));

            SmartDashboard.putData(
                    "turret/Turret Go 0",
                    new SetTurretAngle(turretSubsystem, 0));

            SmartDashboard.putData(
                    "turret/Turret Go 75",
                    new SetTurretAngle(turretSubsystem, 75));

            SmartDashboard.putData(
                    "turret/Run Turret to Rev Limit",
                    new RunTurretToRevLimit(turretSubsystem));

            SmartDashboard.putData(
                    "turret/Run Turret to Fwd Limit",
                    new RunTurretToFwdLimit(turretSubsystem));

                        SmartDashboard.putData(
                                        "intakedeployer/InitlizeDeployer",
                                        new InitalRunDeployment(intakeDeployer));
            SmartDashboard.putData(
                    "Spin Intake",
                    new SpinIntake(intakeSubsystem));
            
            SmartDashboard.putData(
                    "Start Hopper",
                    new SpinHopper(hopperSubsystem));
            
            SmartDashboard.putData(
                    "Climber Up",
                    new ClimberUp(climberSubsystem));

            SmartDashboard.putData(
                    "Climber Down",
                    new ClimberDown(climberSubsystem));

          SmartDashboard.putData(
                    "Spin Feeder",
                    new SpinFeeder(feederSubsystem));

                        SmartDashboard.putData(
                                        "Spin Shooter",
                                        new SpinShooter(shooterSubsystem, Constants.SHOOTER_SPEED));

                        SmartDashboard.putData(
                                        "Shooting State: Stopped",
                                        new SetShootingState(shootState, ShootState.STOPPED));

                        SmartDashboard.putData(
                                        "Shooting State: Fixed",
                                        new SetShootingState(shootState, ShootState.FIXED));

                        SmartDashboard.putData(
                                        "Shooting State: Fixed 2",
                                        new SetShootingState(shootState, ShootState.FIXED_2));

                        SmartDashboard.putData(
                                        "Shooting State: Into Hub",
                                        new SetShootingState(shootState, ShootState.SHOOTING_HUB));

                        SmartDashboard.putData(
                                        "Shooting State: Shuttling",
                                        new SetShootingState(shootState, ShootState.SHUTTLING));
                        SmartDashboard.putData(
                                        "intakedeployer/Deployment State: UP",
                                        new SetDeploymentState(intakeDeployer, DeploymentState.UP));
                        SmartDashboard.putData(
                                        "intakedeployer/Deployment State: DOWN",
                                        new SetDeploymentState(intakeDeployer, DeploymentState.DOWN));
                        SmartDashboard.putData(
                                        "intakedeployer/Deployment State: STOPPED",
                                        new SetDeploymentState(intakeDeployer, DeploymentState.STOPPED));
                        SmartDashboard.putData("AddTunedApriltagReading",
                                        new AddTunableApriltagReading(apriltagSubsystem));
                        SmartDashboard.putData("AddApriltagReading", new AddApriltagReading(apriltagSubsystem,
                                        new ApriltagReading(0, 0, 0, 0, 0, 0, 0)));

                }

                // basic drive command
                if (!Constants.TESTBED) {
                        Command driveDirectionTime = new DriveDirectionTime(drivebase, 0.1, 0.1, true, 1);
                        SmartDashboard.putData("Drive Command", driveDirectionTime);
                        SmartDashboard.putData("Fake vision", new FakeVision(drivebase));
                }

        }

        /**
         * Use this to pass the autonomous command to the main {@link Robot} class.
         *
         * @return the command to run in autonomous
         */
        public Command getAutonomousCommand() {
                // return autoChooser.getCommand();
                // return straightRoutine.cmd(straightTrajectory.done());
                return new ExampleAuto(drivebase, autoFactory);
        }

        public ClimberSubsystem getClimberSubsystem() {
                return climberSubsystem;
        }

        public RobotVisualizer getRobotVisualizer() {
                return robotVisualizer;
        }

        public AutoChooser getAutoChooser() {
                return autoChooser;
        }

        public IntakeSubsystem getIntakeSubsystem() {
                return intakeSubsystem;
        }

        public SwerveSubsystem getDriveBase() {
                return drivebase;
        }

        public ShootingState getShootingState() {
                return shootState;
        }
}
