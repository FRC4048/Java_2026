package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.utils.logging.commands.LoggableCommand;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.LoggedNetworkNumber;
import java.util.function.DoubleSupplier;

public class LoggedTunableNumberTuner extends LoggableCommand {
    private enum State { SET_POINT, WAIT_SETTLE, SAMPLE, CALCULATE_NEXT_PHASE }

    private final LoggedNetworkNumber tunableNumber;
    private final DoubleSupplier errorSupplier;
    private final double settlingTime;
    private final double samplingTime;

    // Search parameters
    private final int pointsPerIteration = 5; // Divide range into 5 points each time
    private final int maxIterations = 3;      // How many times to "zoom in"
    private int currentIteration = 0;
    private int currentPointIndex = 0;

    private double min, max;
    private final double[] testPoints = new double[pointsPerIteration];
    private final double[] pointErrors = new double[pointsPerIteration];

    private double sumError = 0;
    private int sampleCount = 0;
    private final Timer timer = new Timer();
    private State state = State.SET_POINT;

    public LoggedTunableNumberTuner(
            LoggedNetworkNumber tunableNumber,
            DoubleSupplier errorSupplier,
            double initialMin,
            double initialMax,
            double settlingTime,
            double samplingTime) {
        this.tunableNumber = tunableNumber;
        this.errorSupplier = errorSupplier;
        this.min = initialMin;
        this.max = initialMax;
        this.settlingTime = settlingTime;
        this.samplingTime = samplingTime;
    }

    @Override
    public void initialize() {
        currentIteration = 0;
        preparePoints();
        state = State.SET_POINT;
    }

    private void preparePoints() {
        currentPointIndex = 0;
        double step = (max - min) / (pointsPerIteration - 1);
        for (int i = 0; i < pointsPerIteration; i++) {
            testPoints[i] = min + (step * i);
        }
    }

    @Override
    public void execute() {
        switch (state) {
            case SET_POINT:
                tunableNumber.set(testPoints[currentPointIndex]);
                timer.restart();
                state = State.WAIT_SETTLE;
                break;

            case WAIT_SETTLE:
                if (timer.hasElapsed(settlingTime)) {
                    sumError = 0;
                    sampleCount = 0;
                    timer.restart();
                    state = State.SAMPLE;
                }
                break;

            case SAMPLE:
                sumError += Math.abs(errorSupplier.getAsDouble());
                sampleCount++;
                if (timer.hasElapsed(samplingTime)) {
                    pointErrors[currentPointIndex] = sumError / sampleCount;

                    // Log the "Error Map" so we can see the curve in AdvantageScope
                    Logger.recordOutput("Tuning/ErrorMap/" + testPoints[currentPointIndex], pointErrors[currentPointIndex]);

                    if (currentPointIndex < pointsPerIteration - 1) {
                        currentPointIndex++;
                        state = State.SET_POINT;
                    } else {
                        state = State.CALCULATE_NEXT_PHASE;
                    }
                }
                break;

            case CALCULATE_NEXT_PHASE:
                // Find index of lowest error
                int bestIdx = 0;
                for (int i = 1; i < pointsPerIteration; i++) {
                    if (pointErrors[i] < pointErrors[bestIdx]) {
                        bestIdx = i;
                    }
                }

                // Zoom in: New range is between the points to the left and right of the best point
                double newMin = testPoints[Math.max(0, bestIdx - 1)];
                double newMax = testPoints[Math.min(pointsPerIteration - 1, bestIdx + 1)];

                // Safety: if the best was an edge, expand slightly so we don't get stuck at a limit
                if (bestIdx == 0) newMax = testPoints[1];
                if (bestIdx == pointsPerIteration - 1) newMin = testPoints[pointsPerIteration - 2];

                min = newMin;
                max = newMax;
                currentIteration++;

                if (currentIteration < maxIterations) {
                    preparePoints();
                    state = State.SET_POINT;
                }
                break;
        }

        Logger.recordOutput("Tuning/CurrentValue", testPoints[currentPointIndex]);
        Logger.recordOutput("Tuning/Iteration", currentIteration);
    }

    @Override
    public boolean isFinished() {
        return currentIteration >= maxIterations;
    }

    @Override
    public void end(boolean interrupted) {
        timer.stop();
        // The best point is the one we calculated at the end of the last phase
        // or just keep the current best found.
    }
}