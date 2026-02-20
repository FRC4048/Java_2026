package frc.robot.utils.math;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class ArrayUtils {
  public static <T extends Number> boolean contains(T[] array, T value) {
    for (T t : array) {
      if (t.equals(value)) {
        return true;
      }
    }
    return false;
  }

  public static boolean contains(double[] array, double value) {
    for (double d : array) {
      if (d == value) {
        return true;
      }
    }
    return false;
  }

  public static boolean contains(int[] array, int value) {
    for (int d : array) {
      if (d == value) {
        return true;
      }
    }
    return false;
  }

  public static boolean allMatch(double[] array, double value) {
    for (double d : array) {
      if (d != value) {
        return false;
      }
    }
    return true;
  }

  public static boolean allMatch(int[] array, int value) {
    for (int d : array) {
      if (d != value) {
        return false;
      }
    }
    return true;
  }

  public static <T extends Number> boolean allMatch(T[] array, T value) {
    for (T t : array) {
      if (!t.equals(value)) {
        return false;
      }
    }
    return true;
  }

  @SuppressWarnings("unchecked")
  public static <T> T[] toArray(List<T> list, Class<T> tClass) {
    T[] t = (T[]) Array.newInstance(tClass, list.size());
    return list.toArray(t);
  }

  public static double[] unwrap(Double[] a) {
    return Arrays.stream(a).mapToDouble(Double::doubleValue).toArray();
  }

  public static int[] unwrap(Integer[] a) {
    return Arrays.stream(a).mapToInt(Integer::intValue).toArray();
  }

  public static long[] unwrap(Long[] a) {
    return Arrays.stream(a).mapToLong(Long::longValue).toArray();
  }
}
