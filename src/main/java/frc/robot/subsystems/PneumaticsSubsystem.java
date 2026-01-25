// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

// The Roller subsystem spins the wheel that releases the algae.

public class PneumaticsSubsystem extends SubsystemBase {
    private final Solenoid solenoid;

    public PneumaticsSubsystem(Solenoid solenoid) {
        this.solenoid = solenoid;
    }

    public void solenoidOn() {
        solenoid.set(true);
    }

    public void solenoidOff() {
        solenoid.set(false);
    }

    public void toggleSolenoid() {
        solenoid.toggle();
    }
}