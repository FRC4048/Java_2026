package frc.robot.autochooser;

import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;

import choreo.auto.AutoFactory;
import choreo.auto.AutoRoutine;
import choreo.auto.AutoTrajectory;
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
import frc.robot.subsystems.IntakeDeployerSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.utils.logging.commands.LoggableCommandWrapper;
import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;
import frc.robot.utils.logging.commands.LoggableWaitCommand;

public class RoutineChooser {
    private final AutoFactory factory;
    private final FeederSubsystem feeder;
    private final IntakeSubsystem intake;
    private final IntakeDeployerSubsystem deployer;
    private final ShootingState shootState;
    private final LoggedDashboardChooser<AutoPath> autoChooser;

    public RoutineChooser(AutoFactory factory,
            FeederSubsystem feeder,
            IntakeSubsystem intake,
            IntakeDeployerSubsystem deployer,
            ShootingState shootState) {
        this.factory = factory;
        this.feeder = feeder;
        this.intake = intake;
        this.deployer = deployer;
        this.shootState = shootState;

        factory.bind("intakeDeploy", new ForceDeployDown(deployer)); //intake deploy marker
        factory.bind("intakeStart", new SpinIntake(intake)); //intake start marker
        factory.bind("intakeStop", new StopIntake(intake)); //intake stop marker
        factory.bind("intakeUp", new ForceDeployUp(deployer)); //intake up marker
        factory.bind("shooting", new SetShootingState(shootState, ShootState.SHOOTING_HUB)); //shoot state to shooting marker
        factory.bind("stopShooting", new SetShootingState(shootState, ShootState.STOPPED)); //shoot state to stop marker

        autoChooser = new LoggedDashboardChooser<>("AutoAction");
        for (AutoPath paths : AutoPath.values()) {
            autoChooser.addOption(paths.getName(), paths);
        }
    }

    public AutoRoutine getAuto() {
        switch (autoChooser.get()) {
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
            default:
                return factory.newRoutine("do_nothing");
        }

    }

    public AutoRoutine swipeDepot() {
        AutoRoutine routine = factory.newRoutine("swipe");
        AutoTrajectory firstSwipeTraj = routine.trajectory("swipe_one");
        AutoTrajectory secondSwipeTraj = routine.trajectory("swipe_two");
        AutoTrajectory thirdSwipeTraj = routine.trajectory("swipe_three");
        firstSwipeTraj.done().onTrue(new LoggableWaitCommand(3).andThen(secondSwipeTraj.cmd()));
        secondSwipeTraj.done().onTrue(new LoggableWaitCommand(3).andThen(thirdSwipeTraj.cmd()));
        routine.active().onTrue(new LoggableSequentialCommandGroup(
                new LoggableCommandWrapper(firstSwipeTraj.resetOdometry()),
                new LoggableCommandWrapper(firstSwipeTraj.cmd())));
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
        AutoTrajectory firstSwipeTraj = routine.trajectory("swipe_one");
        AutoTrajectory secondSwipeTraj = routine.trajectory("swipe_two_dot");
        firstSwipeTraj.done().onTrue(new LoggableWaitCommand(3).andThen(secondSwipeTraj.cmd()));
        routine.active().onTrue(new LoggableSequentialCommandGroup(
                new LoggableCommandWrapper(firstSwipeTraj.resetOdometry()),
                new LoggableCommandWrapper(firstSwipeTraj.cmd())));
        return routine;

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
         */
    }
}
