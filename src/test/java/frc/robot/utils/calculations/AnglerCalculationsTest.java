package frc.robot.utils.calculations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import frc.robot.subsystems.AnglerSubsystem;

public class AnglerCalculationsTest {

    static final double DELTA = 0.1;

    private static final double ENCODER_HIGH = 100.0;
    private static final double ENCODER_LOW = 0.0;
    private static final double ANGLE_HIGH = 45.0;
    private static final double ANGLE_LOW = 0.0;

    double[] angles = {0, 45, 20, -20, 60, 30};
    double[] expectedRotations = {
            0.0,
            100.0,
            44.46,
            0.0,
            100.0,
            66.67
    };

    private int index;

    private double calculate(double angle) {
        return AnglerSubsystem.calculateRotationsForAngle(
                angle,
                ENCODER_HIGH,
                ENCODER_LOW,
                ANGLE_HIGH,
                ANGLE_LOW);
    }

    @Test
    void angle0Test() {
        index = 0;
        assertEquals(expectedRotations[index], calculate(angles[index]), DELTA);
    }

    @Test
    void angle45Test() {
        index = 1;
        assertEquals(expectedRotations[index], calculate(angles[index]), DELTA);
    }

    @Test
    void angle20Test() {
        index = 2;
        assertEquals(expectedRotations[index], calculate(angles[index]), DELTA);
    }

    @Test
    void angleNeg20Test() {
        index = 3;
        assertEquals(expectedRotations[index], calculate(angles[index]), DELTA);
    }

    @Test
    void angle60Test() {
        index = 4;
        assertEquals(expectedRotations[index], calculate(angles[index]), DELTA);
    }

    @Test
    void angle30Test() {
        index = 5;
        assertEquals(expectedRotations[index], calculate(angles[index]), DELTA);
    }
}
