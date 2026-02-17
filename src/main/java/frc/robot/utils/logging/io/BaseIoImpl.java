package frc.robot.utils.logging.io;

import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.inputs.LoggableInputs;

/**
 * Superclass for all IO implementations.
 */
public abstract class BaseIoImpl<I extends LoggableInputs> implements BaseIo {
    // The name of the "folder" where the logs from this IO will be logged
    private final String prefix;
    private final I inputs;

    public BaseIoImpl(String folder, I inputs) {
        this.inputs = inputs;
        this.prefix = "LoggableInputs/" + folder;
    }

    /**
     * Abstract method implemented by the subclasses and ensures that the inputs are updated from their proper source.
     *
     * @param inputs the inputs (stored at this class) that are to be updated.
     */
    protected abstract void updateInputs(I inputs);

    /**
     * When called while robot is running in the real world, the method logs the data stored in inputs
     * When running in simulation, data from the log is injected into inputs
     */
    public void periodic() {
        updateInputs(inputs);
        Logger.processInputs(prefix, inputs);
    }

    public I getInputs() {
        return inputs;
    }
}
