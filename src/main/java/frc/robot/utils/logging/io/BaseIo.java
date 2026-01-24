package frc.robot.utils.logging.io;

/**
 * An interface extended by all IO interfaces.
 * The IO interfaces are implemented in REAL, SIM and MOCK implementations.
 * IOs are responsible for connecting the LoggableInputs with the actual system (real hardware and simulation).
 * The periodic method is called from the subsystems when it is time to update the inputs from their respective source.
 */
public interface BaseIo {
    void periodic();
}
