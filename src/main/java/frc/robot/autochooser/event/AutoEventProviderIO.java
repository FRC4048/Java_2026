package frc.robot.autochooser.event;

import frc.robot.autochooser.AutoAction;
import frc.robot.autochooser.FieldLocation;
import java.util.function.Consumer;

/**
 * interface that outlines the necessary methods to provide {@link FieldLocation FieldLocations} and
 * {@link AutoAction AutoActions}
 */
public interface AutoEventProviderIO {
  void setOnActionChangeListener(Consumer<AutoAction> listener);

  void setOnLocationChangeListener(Consumer<FieldLocation> listener);

  void addOnValidationCommand(Runnable consumer);

  void runValidCommands();

  void setFeedbackAction(AutoAction action);

  void setFeedbackLocation(FieldLocation location);

  void updateInputs(AutoChooserInputs inputs);
}
