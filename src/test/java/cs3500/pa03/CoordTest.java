package cs3500.pa03;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa03.model.Coord;
import org.junit.jupiter.api.Test;

/**
 * Tests the Coord class and its methods
 */
public class CoordTest {
  Coord fourFive = new Coord(4, 5);

  Coord threeEight = new Coord(3, 8);

  /**
   * Tests accuracy of Coord "x" field
   */
  @Test
  public void testPosnX() {
    assertEquals(4, fourFive.x);
    assertEquals(3, threeEight.x);
  }

  /**
   * Tests accuracy of Coord "y" field
   */
  @Test
  public void testPosnY() {
    assertEquals(5, fourFive.y);
    assertEquals(8, threeEight.y);
  }

  /**
   * Tests Coord toString method
   */
  @Test
  public void testToString() {
    assertEquals("(4, 5)", fourFive.toString());
    assertEquals("(3, 8)", threeEight.toString());
  }

  /**
   * Tests overriding of .equals method
   */
  @Test
  public void testEquals() {
    assertFalse(fourFive.equals(threeEight));
    assertFalse(threeEight.equals(fourFive));

    assertTrue(fourFive.equals(fourFive));
    Coord altFourFive = new Coord(4, 5);
    assertTrue(fourFive.equals(altFourFive));

    Coord fourEight = new Coord(4, 8);
    assertFalse(fourFive.equals(fourEight));

    assertFalse(fourFive.equals(4));
    assertFalse(fourFive.equals("fourFive"));
  }
}
