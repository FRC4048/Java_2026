package frc.robot.commands.pneumatics;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Constants;
import frc.robot.subsystems.PneumaticsSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class PneumaticsRunForSetTime extends LoggableCommand {
  private final PneumaticsSubsystem subsystem;
  private final Timer timer;
  private double solenoidTime;

  public PneumaticsRunForSetTime(PneumaticsSubsystem subsystem, double solenoidTime) {
    timer = new Timer();
    this.subsystem = subsystem;
    this.solenoidTime = solenoidTime;
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {
    subsystem.solenoidOn();
    timer.start();
  }

  @Override
  public void execute() {

  }

  @Override
  public void end(boolean interrupted) {
        subsystem.solenoidOff();
        timer.stop();
  }

  @Override
  public boolean isFinished() {
      if (timer.hasElapsed(solenoidTime)) {
        return true;
      }
    return false;
  }
}
