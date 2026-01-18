package frc.robot.autochooser.event;

import edu.wpi.first.networktables.NetworkTableValue;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.autochooser.AutoAction;
import frc.robot.autochooser.FieldLocation;
import frc.robot.utils.shuffleboard.SmartShuffleboard;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class RealAutoEventProvider implements AutoEventProviderIO {

  public static final String AUTO_TAB_NAME = "Auto";
  public static final String ACTION_FIELD_NAME = "Auto Action";
  public static final String LOCATION_FIELD_NAME = "Location Chooser";
  public static final String AUTO_LOCATION_FEEDBACK_NAME = "FieldLocationFeedback";
  public static final String AUTO_ACTION_FEEDBACK_NAME = "AutoActionFeedback";

  private final SendableChooser<AutoAction> actionChooser;
  private final SendableChooser<FieldLocation> locationChooser;
  private final AutoAction defaultAutoAction;
  private final FieldLocation defaultFieldLocation;
  public final List<Runnable> onValidEvents = new ArrayList<>();

  public RealAutoEventProvider(AutoAction defaultAutoAction, FieldLocation defaultFieldLocation) {
    this.defaultAutoAction = defaultAutoAction;
    this.defaultFieldLocation = defaultFieldLocation;
    this.actionChooser = new SendableChooser<>();
    this.locationChooser = new SendableChooser<>();
    Arrays.stream(AutoAction.values()).forEach(a -> actionChooser.addOption(a.getName(), a));
    Arrays.stream(FieldLocation.values())
        .forEach(l -> locationChooser.addOption(l.getShuffleboardName(), l));
    actionChooser.setDefaultOption(defaultAutoAction.getName(), defaultAutoAction);
    locationChooser.setDefaultOption(
        defaultFieldLocation.getShuffleboardName(), defaultFieldLocation);
    ShuffleboardTab autoTab = Shuffleboard.getTab(AUTO_TAB_NAME);
    autoTab
        .add(ACTION_FIELD_NAME, actionChooser)
        .withWidget(BuiltInWidgets.kComboBoxChooser)
        .withPosition(0, 0)
        .withSize(4, 1);
    autoTab.add(LOCATION_FIELD_NAME, locationChooser).withPosition(0, 1).withSize(4, 1);
    SmartShuffleboard.put(AUTO_TAB_NAME, AUTO_ACTION_FEEDBACK_NAME, defaultAutoAction.toString())
        .withPosition(2, 2)
        .withSize(2, 1);
    SmartShuffleboard.put(
            AUTO_TAB_NAME, AUTO_LOCATION_FEEDBACK_NAME, defaultFieldLocation.getShuffleboardName())
        .withPosition(0, 2)
        .withSize(2, 1);
  }

  /**
   * @param listener function to be called when the value in {@link #actionChooser} changes
   */
  @Override
  public void setOnActionChangeListener(Consumer<AutoAction> listener) {
    actionChooser.onChange(listener);
  }

  /**
   * @param listener function to be called when the value in {@link #locationChooser} changes
   */
  @Override
  public void setOnLocationChangeListener(Consumer<FieldLocation> listener) {
    locationChooser.onChange(listener);
  }

  @Override
  public void addOnValidationCommand(Runnable consumer) {
    onValidEvents.add(consumer);
  }

  @Override
  public void runValidCommands() {
    onValidEvents.forEach(
        c -> {
          try {
            c.run();
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        });
  }

  @Override
  public void setFeedbackAction(AutoAction action) {
    SmartShuffleboard.put(AUTO_TAB_NAME, AUTO_ACTION_FEEDBACK_NAME, action.toString());
  }

  @Override
  public void setFeedbackLocation(FieldLocation location) {
    SmartShuffleboard.put(
        AUTO_TAB_NAME, AUTO_LOCATION_FEEDBACK_NAME, location.getShuffleboardName());
  }

  @Override
  public void updateInputs(AutoChooserInputs inputs) {
    inputs.action = actionChooser.getSelected();
    inputs.location = locationChooser.getSelected();
    inputs.defaultAction = defaultAutoAction;
    inputs.defaultLocation = defaultFieldLocation;
    NetworkTableValue feedbackLocation =
        SmartShuffleboard.getValue(AUTO_TAB_NAME, AUTO_LOCATION_FEEDBACK_NAME);
    if (feedbackLocation == null || feedbackLocation.getString().isBlank()) {
      inputs.feedbackLocation = FieldLocation.INVALID;
    } else {
      inputs.feedbackLocation = FieldLocation.fromName(feedbackLocation.getString());
    }
    NetworkTableValue action = SmartShuffleboard.getValue(AUTO_TAB_NAME, AUTO_ACTION_FEEDBACK_NAME);
    if (action == null || action.getString().isBlank()) {
      inputs.feedbackAction = AutoAction.INVALID;
    } else {
      inputs.feedbackAction = AutoAction.fromName(action.toString());
    }
  }
}
