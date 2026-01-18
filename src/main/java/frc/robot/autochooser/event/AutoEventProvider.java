package frc.robot.autochooser.event;

import frc.robot.Robot;
import frc.robot.autochooser.AutoAction;
import frc.robot.autochooser.FieldLocation;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * Superclass of {@link AutoEventProviderIO} that uses Network Tables to get {@link AutoAction
 * AutoActions} and {@link FieldLocation fieldLocations}<br>
 */
public class AutoEventProvider {
  private final AutoEventProviderIO io;
  private final AutoChooserInputs inputs;
  private final BiFunction<AutoAction, FieldLocation, Boolean> validator;
  private boolean changed = false;

  public AutoEventProvider(
      AutoEventProviderIO providerIO, BiFunction<AutoAction, FieldLocation, Boolean> validator) {
    this.io = providerIO;
    this.inputs = new AutoChooserInputs();
    this.validator = validator;
    setOnActionChangeListener((a) -> changed = true);
    setOnLocationChangeListener((l) -> changed = true);
  }

  public AutoAction getSelectedAction() {
    return inputs.action == null
        ? inputs.defaultAction
        : inputs.action;
  }

  public FieldLocation getSelectedLocation() {
    return inputs.location == null
        ? inputs.defaultLocation
        : inputs.location;
  }

  public void updateInputs() {
    FieldLocation lastsLoc = inputs.location;
    AutoAction lastsAct = inputs.action;
    if (!Robot.isReal()
        && (!lastsLoc.equals(inputs.location)
            || !lastsAct.equals(inputs.action))) {
      changed = true;
    }
    if (changed) {
      forceRefresh();
      changed = false;
    }
  }

  public void setOnActionChangeListener(Consumer<AutoAction> listener) {
    io.setOnActionChangeListener(listener);
  }

  public void setOnLocationChangeListener(Consumer<FieldLocation> listener) {
    io.setOnLocationChangeListener(listener);
  }

  public void forceRefresh() {
    if (validator.apply(getSelectedAction(), getSelectedLocation())) {
      io.setFeedbackAction(getSelectedAction());
      io.setFeedbackLocation(getSelectedLocation());
      io.runValidCommands();
    } else {
      io.setFeedbackAction(AutoAction.INVALID);
      io.setFeedbackLocation(FieldLocation.INVALID);
    }
  }

  public void addOnValidationCommand(Runnable c) {
    io.addOnValidationCommand(c);
  }
}
