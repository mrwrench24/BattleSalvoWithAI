package cs3500.pa03;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.pa03.model.ShipType;
import org.junit.jupiter.api.Test;

/**
 * Tests the ShipType enumeration.
 */
public class ShipTypeTest {
  ShipType submarine = ShipType.SUBMARINE;
  ShipType destroyer = ShipType.DESTROYER;
  ShipType battleship = ShipType.BATTLESHIP;
  ShipType carrier = ShipType.CARRIER;

  /**
   * Ensures the length of each shipPiece is correct.
   */
  @Test
  public void testLength() {
    assertEquals(3, submarine.length);
    assertEquals(4, destroyer.length);
    assertEquals(5, battleship.length);
    assertEquals(6, carrier.length);
  }

  /**
   * Tests the abbreviate method for each ShipPiece.
   */
  @Test
  public void testAbbreviate() {
    assertEquals("S", submarine.abbreviate(true));
    assertEquals("D", destroyer.abbreviate(true));
    assertEquals("B", battleship.abbreviate(true));
    assertEquals("C", carrier.abbreviate(true));

    assertEquals("s", submarine.abbreviate(false));
    assertEquals("d", destroyer.abbreviate(false));
    assertEquals("b", battleship.abbreviate(false));
    assertEquals("c", carrier.abbreviate(false));
  }
}
