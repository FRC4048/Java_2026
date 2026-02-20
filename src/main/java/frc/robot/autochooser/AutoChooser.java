package frc.robot.autochooser;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;

import choreo.auto.AutoFactory;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.auto.BlueLeftShootClimb;
import frc.robot.commands.auto.BlueMidShootClimb;
import frc.robot.commands.auto.BlueRightShootClimb;
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

    private final SwerveSubsystem subsystem; 
    private final ShootingState shootstate;
    private final ShooterSubsystem shooter;
    private final AutoFactory auto;
    private final ClimberSubsystem climber;
    private final FeederSubsystem feeder;
    private final HopperSubsystem hopper;

    public AutoChooser(SwerveSubsystem subsystem, ShootingState shootstate, AutoFactory auto, 
    ShooterSubsystem shooter, ClimberSubsystem climber, FeederSubsystem feeder, HopperSubsystem hopper) {
        this.subsystem = subsystem;
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
        // Currently, we have some example mappings.
        commandMap.put(new AutoEvent(AutoAction.DO_NOTHING, FieldLocation.LEFT),
            new DoNothingCommand());
        commandMap.put(new AutoEvent(AutoAction.DO_NOTHING, FieldLocation.RIGHT), 
            new DoNothingCommand());
        commandMap.put(new AutoEvent(AutoAction.DO_NOTHING, FieldLocation.MIDDLE),
            new DoNothingCommand());
        commandMap.put(new AutoEvent(AutoAction.SHOOT, FieldLocation.LEFT), 
            new LeftShoot(subsystem, auto, shooter, shootstate, hopper, feeder));
        commandMap.put(new AutoEvent(AutoAction.SHOOT, FieldLocation.RIGHT),
            new RightShoot(subsystem, auto, shooter, shootstate, hopper, feeder));
        commandMap.put(new AutoEvent(AutoAction.SHOOT, FieldLocation.MIDDLE), 
            new MidShoot(subsystem, auto, shooter, shootstate, hopper, feeder));

        Optional<DriverStation.Alliance> alliance = DriverStation.getAlliance();
        if (alliance.isPresent() && alliance.get() == DriverStation.Alliance.Blue) {
            //Blue ShootClimbAutos
            commandMap.put(new AutoEvent(AutoAction.SHOOT_AND_CLIMB, FieldLocation.LEFT),
                new BlueLeftShootClimb(subsystem, auto, shooter, shootstate, climber, hopper, feeder));
            commandMap.put(new AutoEvent(AutoAction.SHOOT_AND_CLIMB, FieldLocation.RIGHT), 
                new BlueRightShootClimb(subsystem, auto, shooter, shootstate, climber, hopper, feeder));
            commandMap.put(new AutoEvent(AutoAction.SHOOT_AND_CLIMB, FieldLocation.MIDDLE),
                new BlueMidShootClimb(subsystem, auto, shooter, shootstate, climber, hopper, feeder));
        }  
        
        if (alliance.isPresent() && alliance.get() == DriverStation.Alliance.Red) {
            //Red ShootClimb Autos
            commandMap.put(new AutoEvent(AutoAction.SHOOT_AND_CLIMB, FieldLocation.LEFT),
                new RedLeftShootClimb(subsystem, auto, shooter, shootstate, climber, hopper, feeder));
            commandMap.put(new AutoEvent(AutoAction.SHOOT_AND_CLIMB, FieldLocation.RIGHT), 
                new RedRightShootClimb(subsystem, auto, shooter, shootstate, climber, hopper, feeder));
            commandMap.put(new AutoEvent(AutoAction.SHOOT_AND_CLIMB, FieldLocation.MIDDLE),
                new RedMidShootClimb(subsystem, auto, shooter, shootstate, climber, hopper, feeder));
        }
    }

    public AutoEvent getSelectedEvent() {
        AutoAction chosenAction = actionChooser.get();
        FieldLocation chosenLocation = locationChooser.get();
        return new AutoEvent(chosenAction, chosenLocation);
    }

    public Command getSelectedCommand() {
        AutoEvent event = getSelectedEvent();
        return commandMap.getOrDefault(event, new DoNothingCommand());
    }

    public Command getCommand() {
        return getSelectedCommand();
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
