package frc.robot.autochooser;

import java.util.HashMap;
import java.util.Map;

import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;

import frc.robot.utils.logging.commands.LoggableCommand;

public class AutoChooser {

    /** Drop-down chooser for the location. */
    private LoggedDashboardChooser<FieldLocation> locationChooser;
    /** Drop-down chooser for the action. */
    private LoggedDashboardChooser<AutoAction> actionChooser;
    /** Structure for mapping possible choices to commands. */
    private final Map<AutoEvent, AutoCommand> commandMap = new HashMap<>();

    private final AutoCommand DEFAULT_COMMAND = AutoCommand.Invalid;

    public AutoChooser() {
        this.locationChooser = new LoggedDashboardChooser<>(
            "Location Chooser"
        );
        this.actionChooser = new LoggedDashboardChooser<>(
            "Action Chooser"
        );
        populateChoosers();
        populateMap();
    }

    /** Populates the drop-down choosers with enum constants. */
    private void populateChoosers() {
        for (FieldLocation location : FieldLocation.values()) {
            switch (location) {
                case INVALID -> {} // Skip the invalid case.
                case ZERO -> { // Default
                    locationChooser.addDefaultOption(location.toString(), location);
                }
                default -> {locationChooser.addOption(location.toString(), location);}
            };
        }
        for (AutoAction action : AutoAction.values()) {
            switch (action) {
                case INVALID -> {} // Skip the invalid case.
                case DO_NOTHING -> { // Default
                    actionChooser.addDefaultOption(action.toString(), action);
                }
                default -> {actionChooser.addOption(action.toString(), action);}
            };
        }
    }

    /** Put mappings here.
     *  @see AutoCommand */
    private void populateMap() {
        // Currently, we have some example mappings.
        commandMap.put(new AutoEvent(AutoAction.DO_NOTHING, FieldLocation.LEFT),
            AutoCommand.DoNothing);
        commandMap.put(new AutoEvent(AutoAction.DO_NOTHING, FieldLocation.RIGHT), 
            AutoCommand.DoSomething);
    }

    private AutoCommand get() {
        AutoAction chosenAction = actionChooser.get();
        FieldLocation chosenLocation = locationChooser.get();
        AutoEvent event = new AutoEvent(chosenAction, chosenLocation);

        return commandMap.getOrDefault(event, DEFAULT_COMMAND);
    }

    public LoggableCommand getCommand() {
        return get().getCommand();
    }

    /** @return A human-readable description of the selected command. */
    public String getCommandDescription() {
        return get().getDescription();
    }

    public FieldLocation getLocation() {
        return locationChooser.get();
    }

}
