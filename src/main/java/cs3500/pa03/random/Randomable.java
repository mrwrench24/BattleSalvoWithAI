package cs3500.pa03.random;

/**
 * Represents something that can generate random information.
 */
public interface Randomable {
  /**
   * Returns a random integer.
   *
   * @return - a random integer.
   */
  public int nextInt();

  /**
   * Returns a random integer less than the given bound.
   *
   * @param bound - the highest (exclusive) our result can be.
   * @return - a random integer less than the given bound.
   */
  public int nextInt(int bound);
}
