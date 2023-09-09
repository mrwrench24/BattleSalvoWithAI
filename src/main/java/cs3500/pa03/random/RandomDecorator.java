package cs3500.pa03.random;

import java.util.Random;

/**
 * Represents an object that can generate random information
 */
public class RandomDecorator implements Randomable {
  private final Random rand;

  /**
   * Creates the decorator using a random seed
   */
  public RandomDecorator() {
    this.rand = new Random();
  }

  /**
   * Creates a seeded decorator using given seed
   *
   * @param seed - the seed for our decorator
   */
  public RandomDecorator(int seed) {
    this.rand = new Random(seed);
  }

  // Implement the method from the interface; use the Random object
  @Override
  public int nextInt() {
    return this.rand.nextInt();
  }

  // Implement the method from the interface; use the Random object
  @Override
  public int nextInt(int bound) {
    return this.rand.nextInt(bound);
  }
}
