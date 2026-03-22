package frc.robot.commands.auto;

import frc.robot.commands.feeder.SpinFeeder;
import frc.robot.commands.hopper.SpinHopper;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.HopperSubsystem;
import frc.robot.utils.logging.commands.LoggableRaceCommandGroup;
import frc.robot.utils.logging.commands.LoggableWaitCommand;

public class AutoShoot extends LoggableRaceCommandGroup{

    public AutoShoot(HopperSubsystem hopperSubsystem, FeederSubsystem feederSubsystem, double time){

        super(
            new SpinHopper(hopperSubsystem),
            new SpinFeeder(feederSubsystem),
            new LoggableWaitCommand(time)
        );
    }
}