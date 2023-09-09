package cs3500.pa03;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa03.json.ShipAdapter;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.ShipDirection;
import cs3500.pa03.model.ShipPiece;
import cs3500.pa03.model.ShipType;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the ShipPiece and its methods
 */
public class ShipPieceTest {

  ShipPiece submarine = new ShipPiece(ShipType.SUBMARINE, ShipDirection.HORIZONTAL);
  ShipPiece destroyer = new ShipPiece(ShipType.DESTROYER, ShipDirection.HORIZONTAL);
  ShipPiece battleship = new ShipPiece(ShipType.BATTLESHIP, ShipDirection.HORIZONTAL);
  ShipPiece carrier = new ShipPiece(ShipType.CARRIER, ShipDirection.HORIZONTAL);

  ShipPiece otherDestroyer = new ShipPiece(ShipType.DESTROYER, ShipDirection.HORIZONTAL);
  ShipPiece otherBattleship = new ShipPiece(ShipType.BATTLESHIP, ShipDirection.HORIZONTAL);

  ArrayList<ShipPiece> originalsOnly;

  /**
   * Resets this arrayList because it is mutated in later tests
   */
  @BeforeEach
  public void initData() {
    submarine = new ShipPiece(ShipType.SUBMARINE, ShipDirection.HORIZONTAL);
    originalsOnly = new ArrayList<>(
        Arrays.asList(submarine, destroyer, battleship, carrier));
  }

  /**
   * Tests the ShipPiece's abbreviate method.
   */
  @Test
  public void testAbbreviate() {

    assertEquals("s", submarine.abbreviate(false));
    assertEquals("S", submarine.abbreviate(true));

    assertEquals("d", destroyer.abbreviate(false));
    assertEquals("D", destroyer.abbreviate(true));

    assertEquals("b", battleship.abbreviate(false));
    assertEquals("B", battleship.abbreviate(true));

    assertEquals("c", carrier.abbreviate(false));
    assertEquals("C", carrier.abbreviate(true));
  }

  /**
   * Tests the HandleCounting method
   */
  @Test
  public void testHandleCounting() {
    submarine.addIfNotPresent(ShipType.SUBMARINE, originalsOnly);
    assertEquals(4, originalsOnly.size());

    submarine.addIfNotPresent(ShipType.BATTLESHIP, originalsOnly);
    assertEquals(4, originalsOnly.size());

    otherDestroyer.addIfNotPresent(ShipType.SUBMARINE, originalsOnly);
    assertEquals(4, originalsOnly.size());

    // should be added here
    otherDestroyer.addIfNotPresent(ShipType.DESTROYER, originalsOnly);
    assertEquals(5, originalsOnly.size());

    otherDestroyer.addIfNotPresent(ShipType.DESTROYER, originalsOnly);
    assertEquals(5, originalsOnly.size());

    otherBattleship.addIfNotPresent(ShipType.BATTLESHIP, originalsOnly);
    assertEquals(6, originalsOnly.size());
  }

  /**
   * Tests the isSunk method
   */
  @Test
  public void testIsSunk() {
    submarine.addPresenceAt(new Coord(0, 0));
    submarine.addPresenceAt(new Coord(1, 1));
    submarine.addPresenceAt(new Coord(2, 2));
    assertFalse(submarine.isSunk());

    submarine.addHitAt(new Coord(0, 0));
    assertFalse(submarine.isSunk());

    submarine.addHitAt(new Coord(1, 1));
    assertFalse(submarine.isSunk());

    submarine.addHitAt(new Coord(2, 2));
    assertTrue(submarine.isSunk());

    submarine.addHitAt(new Coord(100, 100));
    assertTrue(submarine.isSunk());
  }

  /**
   * Tests the toJson method, particularly as it relates to determining the
   * "starting point" of the ships.
   */
  @Test
  public void testToJson() {
    ShipPiece examplePiece = new ShipPiece(ShipType.SUBMARINE, ShipDirection.HORIZONTAL);
    examplePiece.addPresenceAt(new Coord(3, 0));
    examplePiece.addPresenceAt(new Coord(1, 0));
    examplePiece.addPresenceAt(new Coord(2, 0));

    ShipAdapter examplePieceAsAdapter = examplePiece.toJson();
    assertEquals(new Coord(1, 0), examplePieceAsAdapter.coord);
    assertEquals(3, examplePieceAsAdapter.length);
    assertEquals("HORIZONTAL", examplePieceAsAdapter.direction);

    ShipPiece exampleVertical = new ShipPiece(ShipType.DESTROYER, ShipDirection.VERTICAL);
    exampleVertical.addPresenceAt(new Coord(2, 5));
    exampleVertical.addPresenceAt(new Coord(2, 3));
    exampleVertical.addPresenceAt(new Coord(2, 2));
    exampleVertical.addPresenceAt(new Coord(2, 4));

    ShipAdapter exampleVerticalAsAdapter = exampleVertical.toJson();
    assertEquals(new Coord(2, 2), exampleVerticalAsAdapter.coord);
    assertEquals(4, exampleVerticalAsAdapter.length);
    assertEquals("VERTICAL", exampleVerticalAsAdapter.direction);
  }
}
