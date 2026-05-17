package frc.robot.commands.intakeDeployment;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.IntakeDeployerSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class Agitate extends LoggableCommand {
  public final IntakeDeployerSubsystem subsystem;
  public final Timer timer;
  public Agitate(IntakeDeployerSubsystem subsystem) {
    this.subsystem = subsystem;
    timer = new Timer();
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {
    timer.restart();
  }

  @Override
  public void execute() {
    if(timer.hasElapsed(Constants.AGITATOR_CYCLE_TIME)){
      timer.reset();
      timer.start();
    }
    subsystem.setSpeed(timer.hasElapsed(Constants.AGITATOR_CYCLE_TIME/2.0) ? Constants.AGITATOR_DOWN: Constants.AGITATIOR_UP);
    Logger.recordOutput("agitatortimer", timer.hasElapsed(Constants.AGITATOR_CYCLE_TIME/2.0));
  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return false;
  }
}
