package frc.robot.commands.auto.shoot;

import choreo.auto.AutoFactory;
import frc.robot.commands.auto.AutoReset;
import frc.robot.commands.auto.AutoShoot;
import frc.robot.commands.drive.DriveSwerve;
import frc.robot.commands.shooter.SetShootingState;
import frc.robot.commands.turret.RunTurretToRevLimit;
import frc.robot.constants.enums.DriveDirection;
import frc.robot.constants.enums.ShootingState;
import frc.robot.constants.enums.ShootingState.ShootState;
import frc.robot.subsystems.AnglerSubsystem;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.HopperSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.commands.LoggableParallelCommandGroup;
import frc.robot.utils.logging.commands.LoggableSequentialCommandGroup;

<<<<<<< HEAD
public class OutpostShoot extends LoggableSequentialCommandGroup{
    public OutpostShoot(
=======
<<<<<<<< HEAD:src/main/java/frc/robot/commands/auto/shoot/DepotShoot.java
public class DepotShoot extends LoggableSequentialCommandGroup{
    public DepotShoot(
========
public class OutpostShoot extends LoggableSequentialCommandGroup{
    public OutpostShoot(
>>>>>>>> 21a7c1e37d6efea4b8468afeb69c7e1e17245bd2:src/main/java/frc/robot/commands/auto/shoot/OutpostShoot.java
>>>>>>> 21a7c1e37d6efea4b8468afeb69c7e1e17245bd2
        SwerveSubsystem drivetrain, AutoFactory auto, ShooterSubsystem shooter, ShootingState shootstate, 
        HopperSubsystem hopper, FeederSubsystem feeder, TurretSubsystem turret, AnglerSubsystem angler) {
        super(  
                new AutoReset(shootstate, turret, angler),
<<<<<<< HEAD
                new RunTurretToRevLimit(turret),
                new DriveSwerve(drivetrain, DriveDirection.FORWARD, 0.25, 0.5), //move forward so fuel falls into robot
=======
<<<<<<<< HEAD:src/main/java/frc/robot/commands/auto/shoot/DepotShoot.java
                new RunTurretToRevLimit(turret),
                new DriveSwerve(drivetrain, DriveDirection.FORWARD, 0.25, 0.5), //move forward so fuel falls into robot
========
>>>>>>>> 21a7c1e37d6efea4b8468afeb69c7e1e17245bd2:src/main/java/frc/robot/commands/auto/shoot/OutpostShoot.java
>>>>>>> 21a7c1e37d6efea4b8468afeb69c7e1e17245bd2
                new LoggableParallelCommandGroup(
                    new SetShootingState(shootstate, ShootState.SHOOTING_HUB),
                    new DriveSwerve(drivetrain, DriveDirection.BACKWARD, 2, 0.5)
                ),
                new AutoShoot(hopper, feeder, 5),
                new SetShootingState(shootstate, ShootState.STOPPED)
        );
    }
}