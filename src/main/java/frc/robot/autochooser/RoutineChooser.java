package frc.robot.autochooser;

import java.util.Optional;

import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;

import choreo.auto.AutoFactory;
import choreo.auto.AutoRoutine;
import choreo.auto.AutoTrajectory;
import frc.robot.Robot;
import frc.robot.commands.auto.AutoShoot;
import frc.robot.commands.feeder.SpinFeeder;
import frc.robot.commands.intake.SpinIntake;
import frc.robot.commands.intake.StopIntake;
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

        factory.bind("intakeDeploy", new ForceDeployDown(deployer)); //intake deploy marker
        factory.bind("intakeStart", new SpinIntake(intake)); //intake start marker
        factory.bind("intakeStop", new StopIntake(intake)); //intake stop marker
        factory.bind("intakeUp", new ForceDeployUp(deployer)); //intake up marker

        autoChooser = new LoggedDashboardChooser<>("AutoAction");
        for (AutoPath paths : AutoPath.values()) {
            autoChooser.addOption(paths.getName(), paths);
        }
    }

    public AutoRoutine getAuto() {
        AutoPath selectedPath = autoChooser.get();
        switch (selectedPath == null ? AutoPath.DO_NOTHING : selectedPath) {
            case DO_NOTHING:
                return factory.newRoutine("do_nothing");
            case SWIPE_DEPOT:
                return swipeDepot();
            case SWIPE_OUTPOST:
                return swipeOutpost();
            case SWIPE_DEPOT_DOT:
                return swipeDepotDot();
            case SWIPE_OUTPOST_DOT:
                return swipeOutpostDot();
            case MID_DEPOT:
                return midDepot();
            case TEST:
                return test();
            case BIG_DOT_DEPOT:
                return bigDotDepot();
            case BIG_DOT_OUTPOST:
                return bigDotDepot();
            default:
                return factory.newRoutine("do_nothing");
        }

    }

    public AutoRoutine swipeDepot() {
        AutoRoutine routine = factory.newRoutine("swipe");
        AutoTrajectory swipe1 = routine.trajectory("swipe_1");
        AutoTrajectory swipe2 = routine.trajectory("swipe_2");
        AutoTrajectory swipe3 = routine.trajectory("swipe_2");
        AutoTrajectory shoot1 = routine.trajectory("shoot_depot_1");
        AutoTrajectory shoot2 = routine.trajectory("shoot_depot_2");

        routine.active().onTrue(new LoggableSequentialCommandGroup(
                new LoggableCommandWrapper(swipe1.resetOdometry()),
                new LoggableCommandWrapper(swipe1.cmd())
        ));
        
        swipe1.done().onTrue(shoot1.cmd());

        shoot1.done().onTrue(new LoggableParallelCommandGroup(
                    new AutoShoot(hopper, feeder, shootState, 5),
                    new LoggableWaitCommand(3)
        ));
        shoot2.cmd();

        shoot2.done().onTrue(shoot1.cmd());

        shoot1.done().onTrue(new LoggableParallelCommandGroup(
                    new AutoShoot(hopper, feeder, shootState, 5),
                    new LoggableWaitCommand(3)
        ));
        shoot2.cmd();

        shoot2.done().onTrue(swipe3.cmd());

        return routine;
    }


   public AutoRoutine swipeOutpost() {
        AutoRoutine routine = factory.newRoutine("swipe");
        AutoTrajectory firstSwipeTraj = routine.trajectory("swipe_one").mirrorY();
        AutoTrajectory secondSwipeTraj = routine.trajectory("swipe_two").mirrorY();
        AutoTrajectory thirdSwipeTraj = routine.trajectory("swipe_three").mirrorY();
        firstSwipeTraj.done().onTrue(new LoggableWaitCommand(3).andThen(secondSwipeTraj.cmd()));
        secondSwipeTraj.done().onTrue(new LoggableWaitCommand(3).andThen(thirdSwipeTraj.cmd()));
        routine.active().onTrue(new LoggableSequentialCommandGroup(
                new LoggableCommandWrapper(firstSwipeTraj.resetOdometry()),
                new LoggableCommandWrapper(firstSwipeTraj.cmd())));
        return routine;
    }
    
    public AutoRoutine midDepot() {
        AutoRoutine routine = factory.newRoutine("depot");
        AutoTrajectory traj = routine.trajectory("depot_hook");

        routine.active().onTrue(new LoggableSequentialCommandGroup(
                new LoggableCommandWrapper(traj.resetOdometry()),
                new LoggableCommandWrapper(traj.cmd())));
        return routine;
    }

    public AutoRoutine swipeDepotDot() {
        AutoRoutine routine = factory.newRoutine("swipe");
        AutoTrajectory firstSwipeTraj = routine.trajectory("swipe_one");
        AutoTrajectory secondSwipeTraj = routine.trajectory("swipe_two_dot");
        firstSwipeTraj.done().onTrue(new LoggableWaitCommand(3).andThen(secondSwipeTraj.cmd()));
        routine.active().onTrue(new LoggableSequentialCommandGroup(
                new LoggableCommandWrapper(firstSwipeTraj.resetOdometry()),
                new LoggableCommandWrapper(firstSwipeTraj.cmd())));
        return routine;
    }

    public AutoRoutine swipeOutpostDot() {
        AutoRoutine routine = factory.newRoutine("swipe");
        AutoTrajectory firstSwipeTraj = routine.trajectory("swipe_one").mirrorY();
        AutoTrajectory secondSwipeTraj = routine.trajectory("swipe_two_dot").mirrorY();
        firstSwipeTraj.done().onTrue(new LoggableWaitCommand(3).andThen(secondSwipeTraj.cmd()));
        routine.active().onTrue(new LoggableSequentialCommandGroup(
                new LoggableCommandWrapper(firstSwipeTraj.resetOdometry()),
                new LoggableCommandWrapper(firstSwipeTraj.cmd())));
        return routine;
    }

    public AutoRoutine test() {
        AutoRoutine routine = factory.newRoutine("test");
        AutoTrajectory traj = routine.trajectory("test");

        routine.active().onTrue(new LoggableSequentialCommandGroup(
                new LoggableCommandWrapper(traj.resetOdometry()),
                new LoggableCommandWrapper(traj.cmd())));
        return routine;
    }

    public AutoRoutine bigDotDepot() {
        AutoRoutine routine = factory.newRoutine("dot");
        AutoTrajectory traj = routine.trajectory("bigDot");

        routine.active().onTrue(new LoggableSequentialCommandGroup(
                new LoggableCommandWrapper(traj.resetOdometry()),
                new LoggableCommandWrapper(traj.cmd())));
        return routine;
    }

    public AutoRoutine bigDotOutpost() {
        AutoRoutine routine = factory.newRoutine("dot");
        AutoTrajectory traj = routine.trajectory("bigDot").mirrorY();

        routine.active().onTrue(new LoggableSequentialCommandGroup(
                new LoggableCommandWrapper(traj.resetOdometry()),
                new LoggableCommandWrapper(traj.cmd())));

        return routine;
    }

        /*
        
        Swipe Auto
            swipe_one
                2 - intakeDeploy, intakeStart
                9 - intakeStop
                12 - shooting

            swipe_two
                1 - shootingStop
                2 - intakeStart
                11 - intakeStop
                12 - shootingStart
            
            swipe_three
                1 - shootingStop
                //go to neutral zone for the beginning of teleop

        Swipe-dot Auto
            swipe_one
                2 - intakeDeploy, intakeStart
                9 - intakeStop
                12 - shooting
            swipe_two_dot
                1 - shootingStop, intakeUp
         */
    
}
