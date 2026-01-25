package frc.robot.commands.pneumatics;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Constants;
import frc.robot.subsystems.PneumaticsSubsystem;
import frc.robot.utils.logging.commands.LoggableCommand;

public class PneumaticsOn extends LoggableCommand {
  private final PneumaticsSubsystem subsystem;
  private final Timer timer;

  public PneumaticsOn(PneumaticsSubsystem subsystem) {
    timer = new Timer();
    this.subsystem = subsystem;
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {
    subsystem.solenoidOn();
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
