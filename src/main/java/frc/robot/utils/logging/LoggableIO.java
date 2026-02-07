package frc.robot.utils.logging;

import org.littletonrobotics.junction.inputs.LoggableInputs;

/**
 * superclass for creating subsystem IO interfaces
 *
 * @param <T> The type of subsystem inputs that should be logged
 */
public interface LoggableIO<T extends LoggableInputs> {
  /**
   * When called while robot is running in the real world, the method should log the data stored in
   * inputs When running in simulation, data from the log should be injected into inputs
   *
   * @param inputs state container
   */
  void updateInputs(T inputs);
}
