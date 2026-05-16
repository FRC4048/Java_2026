package frc.robot.commands.intakeDeployment;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.IntakeDeployerSubsystem;

public class Agitate extends Command {
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
    if(timer.hasElapsed(1)){
      timer.restart();
    }
    subsystem.setSpeed(timer.hasElapsed(0.5) ? Constants.AGITATIOR_UP: Constants.AGITATOR_DOWN);
  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return false;
  }
}
