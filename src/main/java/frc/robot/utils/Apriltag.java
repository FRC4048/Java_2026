package frc.robot.utils;

import edu.wpi.first.math.geometry.Translation3d;

public enum Apriltag { //Andymark field:
  ONE(467.08,291.79, 35.00), //Trench, Red z rotation:180
  TWO(468.56,182.08, 44.25), //Hub, Red z rotation:90
  THREE( 444.80,172.32, 44.25), //Hub, Red z rotation:180
  FOUR(444.80,158.32, 44.25), //Hub, Red z rotation:180
  FIVE(468.56,134.56, 44.25), //Hub, Red z rotation:270
  SIX(467.08,24.85, 35.00), //Trench, Red z rotation:180
  SEVEN(470.03,24.85, 35.00), //Trench, Red z rotation:0
  EIGHT(482.56,134.56, 44.25), //Hub, Red z rotation:270
  NINE(492.33,144.32, 44.25), //Hub, Red z rotation:0
  TEN(492.33,158.32, 44.25), //Hub, Red z rotation:0
  ELEVEN(482.56,182.08, 44.25), //Hub, Red z rotation:90
  TWELVE(470.03,291.79, 35.00), //Trench, Red z rotation:0
  THIRTEEN(649.58,291.02, 21.75), //Outpost, Red z rotation:180
  FOURTEEN(649.58,274.02, 21.75), //Outpost, Red z rotation:180
  FIFTEEN(649.57,169.78, 21.75), //Tower, Red z rotation:180
  SIXTEEN(649.57,152.78, 21.75), //Tower, Red z rotation:180
  SEVENTEEN(183.03,24.85, 35.00), //Trench, Blue z rotation:0
  EIGHTEEN(181.56,134.56, 44.25), //Hub, Blue z rotation:270
  NINETEEN(205.32,144.32, 44.25), //Hub, Blue z rotation:0
  TWENTY(205.32,158.32, 44.52), //Hub, Blue z rotation:0
  TWENTY_ONE(181.56,182.08, 44.25), //Hub, Blue z rotation:90
  TWENTY_TWO(183.03, 291.79, 35.00), //Trench, Blue z rotation:0
  TWENTY_THREE(180.08, 291.79, 35.00),//Trench, Blue z rotation:180
  TWENTY_FOUR(167.56, 182.08, 44.25),//Hub, Blue z rotation:90
  TWENTY_FIVE(157.79, 172.32, 44.25),//Hub, Blue z rotation:180
  TWENTY_SIX(157.79, 158.32, 44.25),//Hub, Blue z rotation:180
  TWENTY_SEVEN(167.58, 134.56, 44.25),//Hub, Blue z rotation:270
  TWENTY_EIGHT(180.08, 24.85, 35.00),//Trench, Blue z rotation:180
  TWENTY_NINE(0.54, 25.62, 21.75),//Outpost, Blue z rotation:0
  THIRTY(0.54, 42.62, 21.75),//Outpost, Blue z rotation:0
  THIRTY_ONE(0.55, 146.86, 21.75),//Tower, Blue z rotation:0
  THIRTY_TWO(0.55, 163.86, 21.75);//Tower, Blue z rotation:0
  /*

  Welded Field:

  public enum Apriltag {
  ONE(467.64, 292.31, 35.00), //Trench, Red z rotation:180
  TWO(469.11, 182.60, 44.25), //Hub, Red z rotation:90
  THREE( 445.35, 172.84, 44.25), //Hub, Red z rotation:180
  FOUR(445.35, 158.84, 44.25), //Hub, Red z rotation:180
  FIVE(469.11, 135.09, 44.25), //Hub, Red z rotation:270
  SIX(467.64, 25.37, 35.00), //Trench, Red z rotation:180
  SEVEN(470.59, 25.37, 35.00), //Trench, Red z rotation:0
  EIGHT(483.11, 135.09, 44.25), //Hub, Red z rotation:270
  NINE(492.88, 144.84, 44.25), //Hub, Red z rotation:0
  TEN(492.88, 158.84, 44.25), //Hub, Red z rotation:0
  ELEVEN(483.11, 182.60, 44.25), //Hub, Red z rotation:90
  TWELVE(470.59, 292.31, 44.25), //Trench, Red z rotation:0
  THIRTEEN(650.92, 291.47, 21.75), //Outpost, Red z rotation:180
  FOURTEEN(650.92, 274.47, 21.75), //Outpost, Red z rotation:180
  FIFTEEN(650.90, 170.22, 21.75), //Tower, Red z rotation:180
  SIXTEEN(650.90, 153.22, 21.75), //Tower, Red z rotation:180
  SEVENTEEN(183.59, 25.37, 35.00), //Trench, Blue z rotation:0
  EIGHTEEN(182.11, 135.09, 44.25), //Hub, Blue z rotation:270
  NINETEEN(205.87, 144.84, 44.25), //Hub, Blue z rotation:0
  TWENTY(205.87, 158.84, 44.25), //Hub, Blue z rotation:0
  TWENTY_ONE(182.11, 182.60, 44.25), //Hub, Blue z rotation:90
  TWENTY_TWO(183.59, 292.31, 35.00), //Trench, Blue z rotation:0
  TWENTY_THREE(180.64, 292.31, 35.00),//Trench, Blue z rotation:180
  TWENTY_FOUR(168.11, 182.60, 44.25),//Hub, Blue z rotation:90
  TWENTY_FIVE(158.34, 172.84, 44.25),//Hub, Blue z rotation:180
  TWENTY_SIX(158.34, 158.84, 44.25),//Hub, Blue z rotation:180
  TWENTY_SEVEN(168.11, 135.09, 44.25),//Hub, Blue z rotation:270
  TWENTY_EIGHT(180.64, 25.37, 35.00),//Trench, Blue z rotation:180
  TWENTY_NINE(0.30, 26.22, 21.75),//Outpost, Blue z rotation:0
  THIRTY(0.30, 43.22, 21.75),//Outpost, Blue z rotation:0
  THIRTY_ONE(0.32, 147.47, 21.75),//Tower, Blue z rotation:0
  THIRTY_TWO(0.32, 164.47, 21.75);//Tower, Blue z rotation:0
  */

  private final double x;
  private final double y;
  private final double z;

  Apriltag(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public static Apriltag of(int number) {
    if (number > values().length || number <= 0) {
      return null;
    }
    return Apriltag.values()[number - 1];
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public double getZ() {
    return z;
  }

  public Translation3d getTranslation() {
    return new Translation3d(x, y, z);
  }

  public int number() {
    return ordinal();
  }
}
