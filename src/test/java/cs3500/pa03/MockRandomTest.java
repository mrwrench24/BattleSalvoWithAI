package cs3500.pa03;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.pa03.random.MockRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the mockRandom
 */
public class MockRandomTest {
  MockRandom rand = new MockRandom();

  /**
   * Resets for each test
   */
  @BeforeEach
  public void initData() {
    rand = new MockRandom();
  }

  /**
   * Tests the next int method
   */
  @Test
  public void testNextInt() {
    assertEquals(0, rand.nextInt());
    assertEquals(3, rand.nextInt());
  }

  /**
   * Tests the nextInt method when given a bound
   */
  @Test
  public void testNextIntBound() {
    assertEquals(0, rand.nextInt(4));
    assertEquals(1, rand.nextInt(2));
  }
}
