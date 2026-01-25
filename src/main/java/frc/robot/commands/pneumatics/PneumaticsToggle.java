package frc.robot.commands.pneumatics;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Constants;
import frc.robot.subsystems.PneumaticsSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class PneumaticsToggle extends LoggableCommand {
  private final PneumaticsSubsystem subsystem;
  private final Timer timer;

  public PneumaticsToggle(PneumaticsSubsystem subsystem) {
    timer = new Timer();
    this.subsystem = subsystem;
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {
    subsystem.toggleSolenoid();
  }

  @Override
  public void execute() {

  }

  @Override
  public void end(boolean interrupted) {

  }

  @Override
  public boolean isFinished() {
      return true;
  }
}
