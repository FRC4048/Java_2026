package frc.robot.commands.drive;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.subsystems.GyroSubsystem;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class TurnToAngle extends LoggableCommand {
    
    private final SwerveSubsystem drivebase;
    private GyroSubsystem gyro;
    private PIDController pidController;
    private double targetAngle;
    private double robotAngle;

    private Timer timer;

    public TurnToAngle(SwerveSubsystem drivebase, GyroSubsystem gyro, double targetAngle) {

        this.targetAngle = targetAngle; // Target angle is in degrees
        this.drivebase = drivebase;
        this.gyro = gyro;
        addRequirements(drivebase);

        timer = new Timer();
    }

    @Override
    public void initialize() {
        pidController = new PIDController(0.4, 0, 0);
        
        timer.restart();

        if (targetAngle < 0) {
            targetAngle = (targetAngle % 360) + 360;
        } else {
            targetAngle = (targetAngle % 360);
        }

        
    }

    @Override
    public void execute() {

        if (gyro.getGyroValues().getAnglesInDeg() < 0) {
            robotAngle = (gyro.getGyroValues().getAnglesInDeg() % 360) + 360;
        } else {
            robotAngle = (gyro.getGyroValues().getAnglesInDeg() % 360);
        }

        drivebase.drive(new Translation2d(0, 0),
        pidController.calculate(robotAngle, targetAngle) * Math.PI / 180,
        true);
    }

    @Override
    public boolean isFinished() {
        return timer.hasElapsed(3);
    }

    @Override
    public void end(boolean interrupted) {
        drivebase.drive(new Translation2d(), 0, false);
    }

}
