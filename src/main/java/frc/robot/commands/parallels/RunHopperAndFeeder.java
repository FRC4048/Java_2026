package frc.robot.commands.parallels;

import frc.robot.commands.feeder.SpinFeeder;
import frc.robot.commands.hopper.SpinHopper;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.HopperSubsystem;
import frc.robot.utils.logging.commands.LoggableParallelCommandGroup;

public class RunHopperAndFeeder extends LoggableParallelCommandGroup{

    public RunHopperAndFeeder(HopperSubsystem hopperSubsystem, FeederSubsystem feederSubsystem){

        super(
            new SpinHopper(hopperSubsystem),
            new SpinFeeder(feederSubsystem)
        );
    }
}
