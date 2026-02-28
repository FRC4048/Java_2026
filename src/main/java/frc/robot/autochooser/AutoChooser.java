package frc.robot.autochooser;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;

import choreo.auto.AutoFactory;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.commands.auto.BlueDepot;
import frc.robot.commands.auto.BlueLeftShootClimb;
import frc.robot.commands.auto.BlueMidShootClimb;
import frc.robot.commands.auto.BlueRightShootClimb;
import frc.robot.commands.auto.CommandDescription;
import frc.robot.commands.auto.LeftShoot;
import frc.robot.commands.auto.RedLeftShootClimb;
import frc.robot.commands.auto.MidShoot;
import frc.robot.commands.auto.RedMidShootClimb;
import frc.robot.commands.auto.RightShoot;
import frc.robot.commands.auto.RedRightShootClimb;
import frc.robot.constants.enums.ShootingState;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.HopperSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.utils.logging.commands.DoNothingCommand;

public class AutoChooser {

    /** Drop-down chooser for the location. */
    private LoggedDashboardChooser<FieldLocation> locationChooser;
    /** Drop-down chooser for the action. */
    private LoggedDashboardChooser<AutoAction> actionChooser;
    /** Structure for mapping possible choices to commands. */
    private final Map<AutoEvent, Command> commandMap = new HashMap<>();

    private final SwerveSubsystem drivetrain; 
    private final ShootingState shootstate;
    private final ShooterSubsystem shooter;
    private final AutoFactory auto;
    private final ClimberSubsystem climber;
    private final FeederSubsystem feeder;
    private final HopperSubsystem hopper;

    public AutoChooser(SwerveSubsystem drivetrain, ShootingState shootstate, AutoFactory auto, 
    ShooterSubsystem shooter, ClimberSubsystem climber, FeederSubsystem feeder, HopperSubsystem hopper) {
        this.drivetrain = drivetrain;
        this.auto = auto;
        this.shootstate = shootstate;
        this.shooter = shooter;
        this.climber = climber;
        this.hopper = hopper;
        this.feeder = feeder;

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
        // Commands where alliance color is irrelevant.
        commandMap.put(new AutoEvent(AutoAction.DO_NOTHING, FieldLocation.LEFT),
            new DoNothingCommand());
        commandMap.put(new AutoEvent(AutoAction.DO_NOTHING, FieldLocation.RIGHT), 
            new DoNothingCommand());
        commandMap.put(new AutoEvent(AutoAction.DO_NOTHING, FieldLocation.MIDDLE),
            new DoNothingCommand());
        // Commands where alliance color is relevant.
        commandMap.put(new AutoEvent(AutoAction.COMPLETE, FieldLocation.LEFT, null), 
        new CommandDescription("depot")); /*
        For each pair of alliance-specific commands (one for red,
        one for blue), There should also be a key with a null
        color passed that is associated with a CommandDescription.
        This allows a more vague description of what the command might
        do, regardless of the alliance color, to be put on the dashboard
        while the chooser is in use by the drive team, rather than it
        appearing as though the selection is invalid. */
        commandMap.put(new AutoEvent(AutoAction.COMPLETE, FieldLocation.LEFT,
            Alliance.Blue),
            new BlueDepot(drivetrain, auto, shooter, shootstate, climber, hopper, feeder));
    }

    public AutoEvent getSelectedEvent() {
        AutoAction chosenAction = actionChooser.get();
        FieldLocation chosenLocation = locationChooser.get();
        Alliance color = Robot.allianceColor().orElse(null);
        if (color == null) { /*
            Passing null explicitly will internally
            mark the instance as irrelevant, which
            is not wanted in this case. */
            return new AutoEvent(chosenAction, chosenLocation);
        } else {
            return new AutoEvent(chosenAction, chosenLocation, color);
        }
    }

    public Command getSelectedCommand() {
        AutoEvent event = getSelectedEvent();
        return commandMap.get(event);
    }

    public Command getCommand() {
        Command command = getSelectedCommand();
        if (command instanceof CommandDescription) {
            return null; /*
            CommandDescription is not meant to be
            an actual command. */
        } else {
            return command;
        }
    }

    public String getCommandDescription() {
        AutoEvent event = getSelectedEvent();
        Command command = commandMap.get(event);
        if (command == null) {
            return "No auto mapped for " + event.getAction() + " at " + event.getLocation();
        }
        return event.getAction() + " at " + event.getLocation() + " -> " + command.getName();
    }

    public FieldLocation getLocation() {
        return locationChooser.get();
    }

}
