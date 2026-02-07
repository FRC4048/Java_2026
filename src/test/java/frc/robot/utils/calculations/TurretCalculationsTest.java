import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import frc.robot.utils.calculations.TurretCalculations;

public class TurretCalculationsTest {

    static final double DELTA = .0348; // standard deviation of 2 degrees or .0348 radians
    double[] xPos = {5.827, 13.850, 9.211, 1.211, 3.811};
    double[] yPos = {2.359, 7.921, 5.386, 0.857, 4.035};
    double[] robotRotation = {toRadians(30), toRadians(-53), toRadians(180), toRadians(45), toRadians(-45)};
    double[] panAngles = {toRadians(-18), toRadians(-102), toRadians(-217), toRadians(-2), toRadians(-1)};
    boolean[] isBlueAlliance =  {false, true, false, true, true};

    private int index;

    private double toRadians(double degree) {
        return degree * Math.PI / 180;
    }

    @Test
    void index0Test() {
        index = 0;
        assertEquals(TurretCalculations.calculateTurretAngle(xPos[index], yPos[index], robotRotation[index], isBlueAlliance[index]), panAngles[index], DELTA);
    }

    @Test
    void index1Test() {
        index = 1;
        assertEquals(TurretCalculations.calculateTurretAngle(xPos[index], yPos[index], robotRotation[index], isBlueAlliance[index]), panAngles[index], DELTA);
    }

    @Test
    void index2Test() {
        index = 2;
        assertEquals(TurretCalculations.calculateTurretAngle(xPos[index], yPos[index], robotRotation[index], isBlueAlliance[index]), panAngles[index], DELTA);
    }

    @Test
    void index3Test() {
        index = 3;
        assertEquals(TurretCalculations.calculateTurretAngle(xPos[index], yPos[index], robotRotation[index], isBlueAlliance[index]), panAngles[index], DELTA);
    }

    @Test
    void index4Test() {
        index = 4;
        assertEquals(TurretCalculations.calculateTurretAngle(xPos[index], yPos[index], robotRotation[index], isBlueAlliance[index]), panAngles[index], DELTA);
    }
}
