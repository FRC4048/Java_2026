package frc.robot.commands.pneumatics;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.utils.logging.commands.LoggableCommand;

public class PneumaticDelay extends LoggableCommand {
  private final Timer timer;
  private double time;

  public PneumaticDelay(double time) {
    timer = new Timer();
    this.time = time;
    addRequirements();
  }

  @Override
  public void initialize() {
    timer.start();
  }

  @Override
  public void execute() {

  }

  @Override
  public void end(boolean interrupted) {
    timer.stop();
  }

  @Override
  public boolean isFinished() {
    if (timer.hasElapsed(time)) {
        return true;
    }
    return false;
  }
}
