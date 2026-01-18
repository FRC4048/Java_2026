package frc.robot.utils.logging;

import frc.robot.utils.logging.input.PidMotorInputs;
import frc.robot.utils.motor.NeoPidConfig;
import frc.robot.utils.logging.io.BaseIoImpl;

public interface PIDIo {
  void configurePID(NeoPidConfig pidConfig);
}