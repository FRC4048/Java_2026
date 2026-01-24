package frc.robot.utils.logging;

import frc.robot.utils.logging.subsystem.inputs.PidMotorInputs;
import frc.robot.utils.motor.NeoPidConfig;

public interface PIDLoggableIO extends LoggableIO<PidMotorInputs> {
  void configurePID(NeoPidConfig pidConfig);
}
