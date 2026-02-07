import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TurretCalculationsTest {

    static final double DELTA = .0348; // standard deviation of 2 degrees or .0348 radians
    int[] xPos = {5, 6, 7, 8, 9};
    int[] yPos = {5, 6, 7, 8, 9};
    int[] robotRotation = {5, 6, 7, 8, 9};
    boolean[] isBlueAlliance =  {false, true, true, false, true};

    private double index;

    @BeforeEach // this method will run before each test
    void setup() {
        
    }

    @AfterEach // this method will run after each test
    void shutdown() throws Exception {
        
    }

    @Test
    void index0Test() {
        index = 0;
        assertEquals(calculateTurretAngle(xPos[index], yPos[index], robotRotation[index], isBlueAlliance[index]), 67);
    }

    @Test
    void index1Test() {
        index = 1;
        assertEquals(calculateTurretAngle(xPos[index], yPos[index], robotRotation[index], isBlueAlliance[index]), 67);
    }

    @Test
    void index2Test() {
        index = 2;
        assertEquals(calculateTurretAngle(xPos[index], yPos[index], robotRotation[index], isBlueAlliance[index]), 67);
    }

    @Test
    void index3Test() {
        index = 3;
        assertEquals(calculateTurretAngle(xPos[index], yPos[index], robotRotation[index], isBlueAlliance[index]), 67);
    }

    @Test
    void index4Test() {
        index = 4;
        assertEquals(calculateTurretAngle(xPos[index], yPos[index], robotRotation[index], isBlueAlliance[index]), 67);
    }
}
