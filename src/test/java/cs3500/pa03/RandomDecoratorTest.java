package cs3500.pa03;

import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa03.random.RandomDecorator;
import org.junit.jupiter.api.Test;

/**
 * Tests the RandomDecorator
 */
public class RandomDecoratorTest {
  RandomDecorator rand = new RandomDecorator();

  /**
   * Tests the nextInt method
   */
  @Test
  public void testNextInt() {
    for (int i = 0; i < 100; i++) {
      int randInt = rand.nextInt();
      assertTrue(randInt >= Integer.MIN_VALUE);
      assertTrue(randInt <= Integer.MAX_VALUE);
    }
  }

  /**
   * Tests the nextInt method when given a bound
   */
  @Test
  public void testNextIntBound() {
    for (int i = 10; i < 100; i++) {
      assertTrue(rand.nextInt(i) <= i);
    }
  }
}
