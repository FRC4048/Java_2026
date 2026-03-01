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
import frc.robot.commands.auto.BlueDepotShootReloadClimb;
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
        //do nothing
        commandMap.put(new AutoEvent(AutoAction.DO_NOTHING, FieldLocation.DEPOT_SIDE, null),
            new DoNothingCommand());
        commandMap.put(new AutoEvent(AutoAction.DO_NOTHING, FieldLocation.MID, null),
            new DoNothingCommand());
        commandMap.put(new AutoEvent(AutoAction.DO_NOTHING, FieldLocation.OUTPOST_SIDE, null), 
            new DoNothingCommand());

        commandMap.put(new AutoEvent(AutoAction.SHOOT, FieldLocation.DEPOT_SIDE, Alliance.Blue),
            new BlueShoot(drivetrain, auto, shooter, shootstate, climber, hopper, feeder)); //shoot depot blue
        commandMap.put(new AutoEvent(AutoAction.SHOOT, FieldLocation.MID, Alliance.Blue),
            new BlueShoot(drivetrain, auto, shooter, shootstate, climber, hopper, feeder)); //shoot mid blue
        commandMap.put(new AutoEvent(AutoAction.SHOOT, FieldLocation.OUTPOST_SIDE, Alliance.Blue),
            new BlueShoot(drivetrain, auto, shooter, shootstate, climber, hopper, feeder)); //shoot outpost blue

        commandMap.put(new AutoEvent(AutoAction.SHOOT, FieldLocation.DEPOT_SIDE, Alliance.Red),
            new RedShoot(drivetrain, auto, shooter, shootstate, climber, hopper, feeder)); //shoot depot red
        commandMap.put(new AutoEvent(AutoAction.SHOOT, FieldLocation.MID, Alliance.Red),
            new RedShoot(drivetrain, auto, shooter, shootstate, climber, hopper, feeder)); //shoot mid red
        commandMap.put(new AutoEvent(AutoAction.SHOOT, FieldLocation.OUTPOST_SIDE, Alliance.Red),
            new RedShoot(drivetrain, auto, shooter, shootstate, climber, hopper, feeder)); // shoot outpost red

        commandMap.put(new AutoEvent(AutoAction.SHOOT_RELOAD_CLIMB, FieldLocation.DEPOT_SIDE, Alliance.Blue), //shoot, reload & climb
            new RedDepotShootReloadClimb(drivetrain, auto, shooter, shootstate, climber, hopper, feeder));
        commandMap.put(new AutoEvent(AutoAction.SHOOT_RELOAD_CLIMB, FieldLocation.MID, Alliance.Blue),
            new RedDepotShootReloadClimb(drivetrain, auto, shooter, shootstate, climber, hopper, feeder)); //shoot & climb
        commandMap.put(new AutoEvent(AutoAction.SHOOT_RELOAD_CLIMB, FieldLocation.OUTPOST_SIDE, Alliance.Blue),
            new RedDepotShootReloadClimb(drivetrain, auto, shooter, shootstate, climber, hopper, feeder)); //shoot, reload & climb

        commandMap.put(new AutoEvent(AutoAction.SHOOT_RELOAD_CLIMB, FieldLocation.DEPOT_SIDE, Alliance.Red),
            new RedMidShootClimb(drivetrain, auto, shooter, shootstate, climber, hopper, feeder)); //shoot, reload & climb
        commandMap.put(new AutoEvent(AutoAction.SHOOT_RELOAD_CLIMB, FieldLocation.MID, Alliance.Red),
            new RedMidShootClimb(drivetrain, auto, shooter, shootstate, climber, hopper, feeder)); //shoot & climb
        commandMap.put(new AutoEvent(AutoAction.SHOOT_RELOAD_CLIMB, FieldLocation.OUTPOST_SIDE, Alliance.Red),
            new RedMidShootClimb(drivetrain, auto, shooter, shootstate, climber, hopper, feeder)); //shoot, reload & climb
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
