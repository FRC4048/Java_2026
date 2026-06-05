package frc.robot.autochooser;

import java.util.Optional;

import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;

import choreo.auto.AutoFactory;
import choreo.auto.AutoRoutine;
import choreo.auto.AutoTrajectory;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Robot;
import frc.robot.commands.auto.AutoShoot;
import frc.robot.commands.feeder.SpinFeeder;
import frc.robot.commands.intake.SpinIntake;
import frc.robot.commands.intake.StopIntake;
import frc.robot.commands.intakeDeployment.Agitate;
import frc.robot.commands.intakeDeployment.ForceDeployDown;
import frc.robot.commands.intakeDeployment.ForceDeployUp;
import frc.robot.commands.intakeDeployment.ToggleDeployment;
import frc.robot.commands.shooter.SetShootingState;
import frc.robot.constants.enums.ShootingState;
import frc.robot.constants.enums.ShootingState.ShootState;
import frc.robot.subsystems.ControllerSubsystem;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.HopperSubsystem;
import frc.robot.subsystems.IntakeDeployerSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.utils.logging.commands.Loggable;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.commands.LoggableCommandWrapper;
import frc.robot.utils.logging.commands.LoggableParallelCommandGroup;
import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;
import frc.robot.utils.logging.commands.LoggableWaitCommand;

public class RoutineChooser {
    private final AutoFactory factory;
    private final FeederSubsystem feeder;
    private final IntakeSubsystem intake;
    private final IntakeDeployerSubsystem deployer;
    private final ShootingState shootState;
    private final HopperSubsystem hopper;
    private final LoggedDashboardChooser<AutoPath> autoChooser;

    public RoutineChooser(AutoFactory factory,
            FeederSubsystem feeder,
            IntakeSubsystem intake,
            IntakeDeployerSubsystem deployer,
            ShootingState shootState,
            HopperSubsystem hopper) {
        this.factory = factory;
        this.feeder = feeder;
        this.intake = intake;
        this.deployer = deployer;
        this.shootState = shootState;
        this.hopper = hopper;

        factory.bind("intakeDeploy", new ForceDeployDown(deployer)); // intake deploy marker
        factory.bind("intakeStart", new SpinIntake(intake)); // intake start marker
        factory.bind("intakeStop", new StopIntake(intake)); // intake stop marker
        factory.bind("intakeUp", new ForceDeployUp(deployer)); // intake up marker

        autoChooser = new LoggedDashboardChooser<>("AutoAction");
        for (AutoPath paths : AutoPath.values()) {
            autoChooser.addOption(paths.getName(), paths);
        }
    }

    public AutoRoutine getAuto() {
        AutoPath selectedPath = autoChooser.get();
        switch (selectedPath == null ? AutoPath.DO_NOTHING : selectedPath) {
            case DO_NOTHING:
                return doNothing();
            case MID_DEPOT:
                return depotMid();
            case TEST:
                return test();
            /* 
            case SINGLE_SWIPE_DEPOT:
                return depotSingleSwipe();
            case SINGLE_SWIPE_OUTPOST:
                return outpostSingleSwipe();
            case DIP_AND_DOT_DEPOT:
                return depotDipAndDot();
            case DIP_AND_DOT_OUTPOST:
                return outpostDipAndDot();
            */
            default:
                return factory.newRoutine("do_nothing");
        }

    }

    /*
    public AutoRoutine depotSingleSwipe() {
        AutoRoutine routine = factory.newRoutine("swipe");

        AutoTrajectory swipe1 = routine.trajectory("swipe_1");
        AutoTrajectory shoot1 = routine.trajectory("shoot_depot_1");

        routine.active().onTrue(new LoggableSequentialCommandGroup(
                new LoggableCommandWrapper(swipe1.resetOdometry()),
                new LoggableCommandWrapper(swipe1.cmd())));

        swipe1.done().onTrue(shoot1.cmd());

        shoot1.done().onTrue(new LoggableParallelCommandGroup(
                new AutoShoot(hopper, feeder, shootState, 5),
                new LoggableSequentialCommandGroup(
                        new LoggableWaitCommand(4),
                        new Agitate(deployer))));

        return routine;
    }
    
    public AutoRoutine outpostSingleSwipe() {
        AutoRoutine routine = factory.newRoutine("swipe");

        AutoTrajectory swipe1 = routine.trajectory("swipe_1").mirrorY();
        AutoTrajectory shoot1 = routine.trajectory("shoot_outpost_1");

        routine.active().onTrue(new LoggableSequentialCommandGroup(
                new LoggableCommandWrapper(swipe1.resetOdometry()),
                new LoggableCommandWrapper(swipe1.cmd())));

        swipe1.done().onTrue(shoot1.cmd());

        shoot1.done().onTrue(new LoggableParallelCommandGroup(
                new AutoShoot(hopper, feeder, shootState, 5),
                new LoggableSequentialCommandGroup(
                        new LoggableWaitCommand(4),
                        new Agitate(deployer))));

        return routine;
    }
    */

    public AutoRoutine depotMid() {
        AutoRoutine routine = factory.newRoutine("depot");
        AutoTrajectory traj = routine.trajectory("midHook");
        AutoTrajectory traj1 = routine.trajectory("midHook_2");

        routine.active().onTrue(new LoggableSequentialCommandGroup(
                new LoggableCommandWrapper(traj.resetOdometry()),
                new LoggableCommandWrapper(traj.cmd())));

        traj.done().onTrue(new LoggableSequentialCommandGroup(
            new AutoShoot(hopper, feeder, shootState, 5),
            new LoggableCommandWrapper(traj1.cmd())));

        return routine;
    }

    public AutoRoutine doNothing() {
        AutoRoutine routine = factory.newRoutine("do nothing");
        AutoTrajectory donothing = routine.trajectory("doNothing");
        routine.active().onTrue(new LoggableSequentialCommandGroup(
                new LoggableCommandWrapper(donothing.resetOdometry()),
                new LoggableCommandWrapper(donothing.cmd())));
        return routine;
    }

    public AutoRoutine test() {
        AutoRoutine routine = factory.newRoutine("test");
        AutoTrajectory traj = routine.trajectory("test_spin");

        routine.active().onTrue(new LoggableSequentialCommandGroup(
                new LoggableCommandWrapper(traj.resetOdometry()),
                new LoggableCommandWrapper(traj.cmd())));
        return routine;
    }

    /*
    
    public AutoRoutine depotDipAndDot() {
        AutoRoutine routine = factory.newRoutine("dot");
        AutoTrajectory traj = routine.trajectory("bigDot_depot_1");
        AutoTrajectory traj1 = routine.trajectory("bigDot_depot_2");

        routine.active().onTrue(new LoggableSequentialCommandGroup(
                new LoggableCommandWrapper(traj.resetOdometry()),
                new LoggableCommandWrapper(traj.cmd())));

        traj.done().onTrue(new LoggableParallelCommandGroup(
                new AutoShoot(hopper, feeder, shootState, 5),
                new LoggableSequentialCommandGroup(
                        new LoggableWaitCommand(4),
                        new Agitate(deployer))));

        traj1.cmd(); // dot

        return routine;
    }

    public AutoRoutine outpostDipAndDot() {
        AutoRoutine routine = factory.newRoutine("dot");
        AutoTrajectory traj = routine.trajectory("bigDot_depot_1");
        AutoTrajectory traj1 = routine.trajectory("bigDot_depot_2");

        routine.active().onTrue(new LoggableSequentialCommandGroup(
                new LoggableCommandWrapper(traj.resetOdometry()),
                new LoggableCommandWrapper(traj.cmd())));

        traj.done().onTrue(new LoggableParallelCommandGroup(
                new AutoShoot(hopper, feeder, shootState, 5),
                new LoggableSequentialCommandGroup(
                        new LoggableWaitCommand(4),
                        new Agitate(deployer))));

        traj1.cmd(); // dot

        return routine;
    }
    */
}
