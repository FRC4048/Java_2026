package frc.robot.autochooser;

import java.util.HashMap;
import java.util.Map;

import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;

import choreo.auto.AutoFactory;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.commands.auto.shoot.DepotShoot;
import frc.robot.commands.auto.shoot.MidShoot;
import frc.robot.commands.auto.shoot.OutpostShoot;
import frc.robot.constants.enums.ShootingState;
import frc.robot.subsystems.AnglerSubsystem;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.ControllerSubsystem;
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
    /** Structure for mapping possible choices to commands. */
    private final Map<AutoEvent, String> descriptionMap = new HashMap<>();

    private final SwerveSubsystem drivetrain; 
    private final ShootingState shootstate;
    private final ShooterSubsystem shooter;
    private final AutoFactory auto;
    private final FeederSubsystem feeder;
    private final HopperSubsystem hopper;
    private final TurretSubsystem turret;
    private final AnglerSubsystem angler;
    private final ClimberSubsystem climber;
    private final ControllerSubsystem controller;
                
                    public AutoChooser(SwerveSubsystem drivetrain, ShootingState shootstate, AutoFactory auto, 
                    ShooterSubsystem shooter, ClimberSubsystem climber, FeederSubsystem feeder, HopperSubsystem hopper, 
                    TurretSubsystem turret, AnglerSubsystem angler, ControllerSubsystem controller) {
                        this.drivetrain = drivetrain;
                        this.auto = auto;
                        this.shootstate = shootstate;
                        this.shooter = shooter;
                        this.hopper = hopper;
                        this.feeder = feeder;
                        this.turret = turret;
                        this.angler = angler;
                        this.climber = climber;
                        this.controller = controller;

        this.locationChooser = new LoggedDashboardChooser<>(
            "Location Chooser"
        );
        this.actionChooser = new LoggedDashboardChooser<>(
            "Action Chooser"
        );
        populateChoosers();
        populateCommandMap();
        populateDescriptionMap();
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

    /** Put command mappings here.
     *  @see AutoCommand */
    private void populateCommandMap() {
        //if AutoEvent is not dependent on alliance color don't put a color
        commandMap.put(new AutoEvent(AutoAction.DO_NOTHING, FieldLocation.DEPOT_SIDE),
            new DoNothingCommand());
        commandMap.put(new AutoEvent(AutoAction.DO_NOTHING, FieldLocation.MID),
            new DoNothingCommand());
        commandMap.put(new AutoEvent(AutoAction.DO_NOTHING, FieldLocation.OUTPOST_SIDE), 
            new DoNothingCommand());

        commandMap.put(new AutoEvent(AutoAction.SHOOT, FieldLocation.DEPOT_SIDE),
            new DepotShoot(drivetrain, auto, shooter, shootstate, hopper, feeder, turret, angler, controller)); 
        commandMap.put(new AutoEvent(AutoAction.SHOOT, FieldLocation.MID),
            new MidShoot(drivetrain, auto, shooter, shootstate, hopper, feeder, turret, angler, controller));
        commandMap.put(new AutoEvent(AutoAction.SHOOT, FieldLocation.OUTPOST_SIDE),
            new OutpostShoot(drivetrain, auto, shooter, shootstate, hopper, feeder, turret, angler, controller));
    }

    /** Put command mappings here.
     *  @see AutoCommand */
    private void populateDescriptionMap() {
        descriptionMap.put(new AutoEvent(AutoAction.DO_NOTHING, FieldLocation.DEPOT_SIDE),
            "do nothing");
        descriptionMap.put(new AutoEvent(AutoAction.DO_NOTHING, FieldLocation.MID),
            "do nothing");
        descriptionMap.put(new AutoEvent(AutoAction.DO_NOTHING, FieldLocation.OUTPOST_SIDE), 
            "do nothing");

        descriptionMap.put(new AutoEvent(AutoAction.SHOOT, FieldLocation.DEPOT_SIDE),
            "shoot from the depot"); 
        descriptionMap.put(new AutoEvent(AutoAction.SHOOT, FieldLocation.MID),
            "shoot from the middle"); 
        descriptionMap.put(new AutoEvent(AutoAction.SHOOT, FieldLocation.OUTPOST_SIDE),
            "shoot from the outpost"); 
    }

    public AutoEvent getSelectedEvent() {
        AutoAction chosenAction = actionChooser.get();
        FieldLocation chosenLocation = locationChooser.get();
        Alliance color = Robot.allianceColor().orElse(null);
        return new AutoEvent(chosenAction, chosenLocation, color);
    }

    public Command getSelectedCommand() {
        AutoEvent event = getSelectedEvent();
        return getCommand(event);
    }
    public AutoAction getAction(){
        return actionChooser.get();
    }
    /** @return A human-readable description of the selected command. */
    public String getCommandDescription() {
        AutoEvent event = getSelectedEvent();
        Command command = getCommand(event);
        String commandDescription;
        if (command == null) {
            return "No auto mapped for " + event.getAction() + " at " + event.getLocation();
        } else {
            commandDescription = descriptionMap.get(event.withoutColor());
        }
        return event.getAction() + " at " + event.getLocation() + " → " + commandDescription + ".";
    }

    public FieldLocation getFieldLocation() {
        return locationChooser.get();
    }


    private Command getCommand(AutoEvent event) {
        Command command = commandMap.get(event);
        if (command != null) {
            // prioritize color-specific command, if we have one
            return command;
        } else {
            // fall back to color-agnostic command if we didn't find one
            return commandMap.get(event.withoutColor());
        }
    }
}
