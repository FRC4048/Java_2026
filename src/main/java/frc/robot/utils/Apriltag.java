package frc.robot.utils;

import edu.wpi.first.math.geometry.Translation3d;

public enum Apriltag {
  ONE(16.687292, 0.628142, 1.4859),
  TWO(16.687292, 7.414259999999999, 1.4859),
  THREE(11.49096, 8.031733999999998, 1.30175),
  FOUR(9.276079999999999, 6.132575999999999, 1.8679160000000001),
  FIVE(9.276079999999999, 1.909825999999999, 1.8679160000000001),
  SIX(13.474446, 3.3012379999999997, 0.308102),
  SEVEN(13.890498, 4.0208200000000005, 0.308102),
  EIGHT(13.474446, 4.740402, 0.308102),
  NINE(12.643358, 4.740402, 0.308102),
  TEN(12.227305999999999, 4.0208200000000005, 0.308102),
  ELEVEN(12.643358, 3.3012379999999997, 0.308102),
  TWELVE(0.8613139999999999, 0.628142, 1.4859),
  THIRTEEN(0.8613139999999999, 7.414259999999999, 1.4859),
  FOURTEEN(8.272272, 6.132575999999999, 1.8679160000000001),
  FIFTEEN(8.272272, 1.9098259999999998, 1.8679160000000001),
  SIXTEEN(6.057646, 0.010667999999999999, 1.30175),
  SEVENTEEN(4.073905999999999, 3.3012379999999997, 0.308102),
  EIGHTEEN(3.6576, 4.0208200000000005, 0.308102),
  NINETEEN(4.073905999999999, 4.740402, 0.308102),
  TWENTY(4.904739999999999, 4.740402, 0.308102),
  TWENTY_ONE(5.321046, 4.0208200000000005, 0.17),
  TWENTY_TWO(4.904739999999999, 3.3012379999999997, 0.308102);

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