package cs3500.pa03;


import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.pa03.random.MockRandomFour;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the MockRandomFour used for testing the A.I. features
 */
public class MockRandomFourTest {
  MockRandomFour rand = new MockRandomFour();

  /**
   * Resets the mock for each test
   */
  @BeforeEach
  public void initData() {
    rand = new MockRandomFour();
  }

  /**
   * Tests the nextInt methdo
   */
  @Test
  public void testNextInt() {
    assertEquals(16, rand.nextInt());
    assertEquals(78, rand.nextInt());
    assertEquals(48, rand.nextInt());
  }

  /**
   * Tests the nextInt method when given an upper bound limit
   */
  @Test
  public void testNextIntBound() {
    assertEquals(7, rand.nextInt(8));
    assertEquals(3, rand.nextInt(5));
    assertEquals(67, rand.nextInt(100));
  }
}
