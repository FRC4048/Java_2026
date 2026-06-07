package frc.robot.autochooser;

import java.io.File;
import java.util.Optional;

import javax.naming.spi.DirectoryManager;

import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;

import choreo.auto.AutoFactory;
import choreo.auto.AutoRoutine;
import choreo.auto.AutoTrajectory;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.Filesystem;
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
    private final LoggedDashboardChooser<Runnable> autoChooser;
    private final File[] deploy;

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

        this.deploy = new File(Filesystem.getDeployDirectory(), "choreo").listFiles();
        autoChooser = new LoggedDashboardChooser<>("AutoAction");
        for (File file : deploy) {
            if (file.getName() != "path.chor") {
                String name = file.getName().replaceAll("_", " ").replaceAll(".traj", "");
                autoChooser.addOption(name, () -> {
                AutoRoutine routine = factory.newRoutine(name);
                AutoTrajectory traj = routine.trajectory(file.getName());
                
                });
            }
        }
    }

    public AutoRoutine getAuto() {
        return null;
    }

    public AutoRoutine depotMid() {
        return factory.newRoutine("auto1");
    }
}
