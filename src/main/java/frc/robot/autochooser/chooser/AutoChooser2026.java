package frc.robot.autochooser.chooser;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.autochooser.AutoAction;
import frc.robot.autochooser.FieldLocation;
import frc.robot.autochooser.event.AutoEvent;
import frc.robot.autochooser.event.AutoEventProvider;
import frc.robot.autochooser.event.AutoEventProviderIO;
import frc.robot.utils.logging.commands.DoNothingCommand;
import java.util.Map;

public class AutoChooser2026 extends SubsystemBase implements AutoChooser {
  private final Map<AutoEvent, Command> commandMap;
  private final AutoEventProvider provider;
  // private final SwerveDrivetrain drivetrain;

  public AutoChooser2026(
      AutoEventProviderIO providerIO
      // SwerveDrivetrain drivetrain,
      ) {
    provider = new AutoEventProvider(providerIO, this::isValid);
    // this.drivetrain = drivetrain;
    commandMap =
        Map.ofEntries(
            Map.entry(
                new AutoEvent(AutoAction.DO_NOTHING, FieldLocation.LEFT), new DoNothingCommand()),
            Map.entry(
                new AutoEvent(AutoAction.DO_NOTHING, FieldLocation.MIDDLE), new DoNothingCommand()),
            Map.entry(
                new AutoEvent(AutoAction.DO_NOTHING, FieldLocation.RIGHT), new DoNothingCommand())
            );
  }

  @Override
  public Command getAutoCommand() {
    return commandMap.get(
        new AutoEvent(provider.getSelectedAction(), provider.getSelectedLocation()));
  }

  @Override
  public Pose2d getStartingPosition() {
    return provider.getSelectedLocation().getLocation();
  }

  protected boolean isValid(AutoAction action, FieldLocation location) {
    return commandMap.containsKey(new AutoEvent(action, location));
  }

  @Override
  public void periodic() {
    provider.updateInputs();
  }

  public AutoEventProvider getProvider() {
    return provider;
  }
}
