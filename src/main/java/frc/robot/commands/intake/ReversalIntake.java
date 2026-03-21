package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.constants.Constants;
import frc.robot.constants.enums.DeploymentState;
import frc.robot.subsystems.IntakeDeployerSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class ReversalIntake extends LoggableCommand {
    private final IntakeSubsystem subsystem;
    private final Timer timer;

    public ReversalIntake(IntakeSubsystem subsystem) {
        timer = new Timer();
        this.subsystem = subsystem;
        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
        timer.restart();
    }

    @Override
    public void execute() {
            subsystem.setSpeed(Constants.REVERSAL_INTAKE_SPEED);
    }

    @Override
    public boolean isFinished() {
        return timer.hasElapsed(Constants.REVERSAL_INTAKE_TIMEOUT);
    }

    @Override
    public void end(boolean interrupted) {
        subsystem.stopMotors();
    }
}
