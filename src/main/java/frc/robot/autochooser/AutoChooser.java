package frc.robot.autochooser;

import java.util.HashMap;
import java.util.Map;

import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;

import choreo.auto.AutoFactory;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.commands.auto.CommandDescription;
import frc.robot.commands.auto.shoot.BlueDepotShoot;
import frc.robot.commands.auto.shoot.BlueMidShoot;
import frc.robot.commands.auto.shoot.BlueOutpostShoot;
import frc.robot.commands.auto.shoot.RedDepotShoot;
import frc.robot.commands.auto.shoot.RedMidShoot;
import frc.robot.commands.auto.shoot.RedOutpostShoot;
import frc.robot.constants.enums.ShootingState;
import frc.robot.subsystems.AnglerSubsystem;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.HopperSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.TurretSubsystem;
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
    private final FeederSubsystem feeder;
    private final HopperSubsystem hopper;
    private final TurretSubsystem turret;
    private final AnglerSubsystem angler;

    public AutoChooser(SwerveSubsystem drivetrain, ShootingState shootstate, AutoFactory auto, 
    ShooterSubsystem shooter, ClimberSubsystem climber, FeederSubsystem feeder, HopperSubsystem hopper, 
    TurretSubsystem turret, AnglerSubsystem angler) {
        this.drivetrain = drivetrain;
        this.auto = auto;
        this.shootstate = shootstate;
        this.shooter = shooter;
        this.hopper = hopper;
        this.feeder = feeder;
        this.turret = turret;
        this.angler = angler;

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
            new BlueDepotShoot(drivetrain, auto, shooter, shootstate, hopper, feeder, turret, angler)); //shoot depot blue
        commandMap.put(new AutoEvent(AutoAction.SHOOT, FieldLocation.MID, Alliance.Blue),
            new BlueMidShoot(drivetrain, auto, shooter, shootstate, hopper, feeder, turret, angler)); //shoot mid blue
        commandMap.put(new AutoEvent(AutoAction.SHOOT, FieldLocation.OUTPOST_SIDE, Alliance.Blue),
            new BlueOutpostShoot(drivetrain, auto, shooter, shootstate, hopper, feeder, turret, angler)); //shoot outpost blue

        commandMap.put(new AutoEvent(AutoAction.SHOOT, FieldLocation.DEPOT_SIDE, Alliance.Red),
            new RedDepotShoot(drivetrain, auto, shooter, shootstate, hopper, feeder, turret, angler)); //shoot depot red
        commandMap.put(new AutoEvent(AutoAction.SHOOT, FieldLocation.MID, Alliance.Red),
            new RedMidShoot(drivetrain, auto, shooter, shootstate, hopper, feeder, turret, angler)); //shoot mid red
        commandMap.put(new AutoEvent(AutoAction.SHOOT, FieldLocation.OUTPOST_SIDE, Alliance.Red),
            new RedOutpostShoot(drivetrain, auto, shooter, shootstate, hopper, feeder, turret, angler)); // shoot outpost red
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