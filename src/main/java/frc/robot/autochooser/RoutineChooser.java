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

public class RoutineChooser {
    private final AutoFactory factory;
    private final FeederSubsystem feeder;
    private final IntakeSubsystem intake;
    private final IntakeDeployerSubsystem deployer;
    private final LoggedDashboardChooser<AutoPath> autoChooser;

    public RoutineChooser(AutoFactory factory, FeederSubsystem feeder, IntakeSubsystem intake, IntakeDeployerSubsystem deployer){
        this.factory = factory;
        this.feeder = feeder;
        this.intake = intake;
        this.deployer = deployer;
        autoChooser = new LoggedDashboardChooser<>("AutoAction");
        for(AutoPath paths : AutoPath.values()){
            autoChooser.addOption(paths.getName(), paths);
        }
    }

    public AutoRoutine routine(String key){
        AutoRoutine routine = factory.newRoutine(key);
        AutoTrajectory traj = routine.trajectory(key);
        routine.active().onTrue(new LoggableSequentialCommandGroup(
            new LoggableCommandWrapper(traj.resetOdometry()),
            new LoggableCommandWrapper(traj.cmd())));
        traj.atTime("feed").onTrue(new SpinFeeder(feeder));
        traj.atTime("intake").onTrue(new SpinIntake(intake, deployer));
        return routine;
    }

    public AutoRoutine getAuto(){
        switch (autoChooser.get()){
            case DO_NOTHING:
            Logger.recordOutput("Auto", "Do nothing");
            return factory.newRoutine("do_nothing");
            case SWIPE: 
            Logger.recordOutput("Auto", "swipe");
            return routine("swipe");
            default: 
            Logger.recordOutput("Auto", "INVALID AUTO THIS IS BAD");
            return factory.newRoutine("do_nothing");
        }
        
    }

}
