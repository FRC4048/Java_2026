package frc.robot.commands.roller;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Constants;
import frc.robot.subsystems.RollerSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class SpinRoller extends LoggableCommand {
  private final RollerSubsystem subsystem;
  private final Timer timer;

  public SpinRoller(RollerSubsystem subsystem) {
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
    subsystem.setSpeed(Constants.ROLLER_SPEED);
  }

  @Override
  public void end(boolean interrupted) {
    subsystem.stopMotors();
  }

  @Override
  public boolean isFinished() {
    if (timer.hasElapsed(Constants.SPIN_TIMEOUT)) {
      return true;
    } else{
      return false;
    }
  }
}
