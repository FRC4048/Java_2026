package frc.robot.parallels;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.feeder.SpinFeeder;
import frc.robot.commands.hopper.SpinHopper;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.HopperSubsystem;

public class RunHopperAndFeeder extends ParallelCommandGroup{

    public RunHopperAndFeeder(HopperSubsystem hopperSubsystem, FeederSubsystem feederSubsystem){

        addCommands(
            new SpinHopper(hopperSubsystem),
            new SpinFeeder(feederSubsystem)

        );
    }
}
