package utils.generators;

public class LockIdGenerator {
  private static int counter = 0;

  public static int getId() {
    return counter++;
  }
}
