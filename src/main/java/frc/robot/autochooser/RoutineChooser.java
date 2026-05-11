package frc.robot.autochooser;

import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;

import choreo.auto.AutoFactory;
import choreo.auto.AutoRoutine;
import choreo.auto.AutoTrajectory;
import frc.robot.commands.feeder.SpinFeeder;
import frc.robot.commands.intake.SpinIntake;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.IntakeDeployerSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.utils.logging.commands.LoggableCommandWrapper;
import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;
import frc.robot.utils.logging.commands.LoggableWaitCommand;

public class RoutineChooser {
    private final AutoFactory factory;
    private final FeederSubsystem feeder;
    private final IntakeSubsystem intake;
    private final IntakeDeployerSubsystem deployer;
    private final LoggedDashboardChooser<AutoPath> autoChooser;

    public RoutineChooser(AutoFactory factory,
            FeederSubsystem feeder,
            IntakeSubsystem intake,
            IntakeDeployerSubsystem deployer) {
        this.factory = factory;
        this.feeder = feeder;
        this.intake = intake;
        this.deployer = deployer;
        factory.bind("feed", new SpinFeeder(feeder));
        factory.bind("intake", new SpinIntake(intake, deployer));
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
                return swipeDepot();
            default:
                return factory.newRoutine("do_nothing");
        }

    }

    public AutoRoutine swipeDepot() {
        AutoRoutine routine = factory.newRoutine("swipe");
        AutoTrajectory firstSwipeTraj = routine.trajectory("swipe");
        AutoTrajectory SecondSwipeTraj = routine.trajectory("swipe");
        firstSwipeTraj.done().onTrue(new LoggableWaitCommand(10).andThen(SecondSwipeTraj.cmd()));
        routine.active().onTrue(new LoggableSequentialCommandGroup(
                new LoggableCommandWrapper(firstSwipeTraj.resetOdometry()),
                new LoggableCommandWrapper(firstSwipeTraj.cmd())));
        return routine;
    }
}
