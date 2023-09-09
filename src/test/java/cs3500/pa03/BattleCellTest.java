package cs3500.pa03;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa03.model.BattleCell;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.ShipDirection;
import cs3500.pa03.model.ShipPiece;
import cs3500.pa03.model.ShipType;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the BattleCell and its methods
 */
public class BattleCellTest {
  ShipPiece carrier = new ShipPiece(ShipType.CARRIER, ShipDirection.HORIZONTAL);

  BattleCell one = new BattleCell(0, 0);
  BattleCell two = new BattleCell(0, 1);
  BattleCell three = new BattleCell(0, 2);
  BattleCell four = new BattleCell(0, 3);
  BattleCell five = new BattleCell(0, 4);
  BattleCell six = new BattleCell(0, 5);

  ShipPiece submarine = new ShipPiece(ShipType.SUBMARINE, ShipDirection.HORIZONTAL);

  BattleCell seven = new BattleCell(1, 0);
  BattleCell eight = new BattleCell(1, 1);
  BattleCell nine = new BattleCell(1, 2);
  BattleCell ten = new BattleCell(1, 3);
  BattleCell eleven = new BattleCell(1, 4);
  BattleCell twelve = new BattleCell(1, 5);

  /**
   * Sets the contents of each cell as appropriate.
   */
  @BeforeEach
  public void initData() {
    one = new BattleCell(0, 0);
    two = new BattleCell(0, 1);
    three = new BattleCell(0, 2);
    four = new BattleCell(0, 3);
    five = new BattleCell(0, 4);
    six = new BattleCell(0, 5);
    seven = new BattleCell(1, 0);
    eight = new BattleCell(1, 1);
    nine = new BattleCell(1, 2);
    ten = new BattleCell(1, 3);
    eleven = new BattleCell(1, 4);
    twelve = new BattleCell(1, 5);

    carrier = new ShipPiece(ShipType.CARRIER, ShipDirection.HORIZONTAL);
    submarine = new ShipPiece(ShipType.SUBMARINE, ShipDirection.HORIZONTAL);
    // C C C C C C
    // o S S S S o

    one.setContents(carrier);
    two.setContents(carrier);
    three.setContents(carrier);
    four.setContents(carrier);
    five.setContents(carrier);
    six.setContents(carrier);

    eight.setContents(submarine);
    nine.setContents(submarine);
    ten.setContents(submarine);
    eleven.setContents(submarine);
  }

  /**
   * Tests the opponentToString method
   */
  @Test
  public void testOpponentToString() {
    assertEquals("o", one.opponentToString());
    assertEquals("o", seven.opponentToString());
    assertEquals("o", eight.opponentToString());

    one.hitThis();
    seven.hitThis();
    eight.hitThis();

    assertEquals("X", one.opponentToString());
    assertEquals("E", seven.opponentToString());
    assertEquals("X", eight.opponentToString());
  }

  /**
   * Tests the playerToString method
   */
  @Test
  public void testPlayerToString() {
    assertEquals("c", one.playerToString());
    assertEquals("o", seven.playerToString());
    assertEquals("s", eight.playerToString());

    one.hitThis();
    seven.hitThis();
    eight.hitThis();

    assertEquals("C", one.playerToString());
    assertEquals("E", seven.playerToString());
    assertEquals("S", eight.playerToString());
  }

  /**
   * Tests the handleCounting method
   */
  @Test
  public void testHandleCounting() {
    ArrayList<ShipPiece> piecesSeen = new ArrayList<>();

    one.handleCounting(ShipType.SUBMARINE, piecesSeen);
    assertEquals(0, piecesSeen.size());

    one.handleCounting(ShipType.CARRIER, piecesSeen);
    assertEquals(1, piecesSeen.size());

    seven.handleCounting(ShipType.CARRIER, piecesSeen);
    assertEquals(1, piecesSeen.size());

    seven.handleCounting(ShipType.DESTROYER, piecesSeen);
    assertEquals(1, piecesSeen.size());

    seven.handleCounting(ShipType.SUBMARINE, piecesSeen);
    assertEquals(1, piecesSeen.size());

    eight.handleCounting(ShipType.SUBMARINE, piecesSeen);
    assertEquals(2, piecesSeen.size());
  }

  /**
   * Tests the "hitThis" method
   */
  @Test
  public void testHit() {
    assertEquals("c", one.playerToString());
    assertTrue(one.hitThis());
    assertEquals("C", one.playerToString());

    assertEquals("o", seven.playerToString());
    assertFalse(seven.hitThis());
    assertEquals("E", seven.playerToString());
  }

  @Test
  public void testAddIfAlive() {
    ArrayList<ShipPiece> testResult = new ArrayList<>();

    one.addIfAlive(testResult);
    assertEquals(carrier, testResult.get(0));

    two.addIfAlive(testResult);
    assertEquals(1, testResult.size());
    assertEquals(carrier, testResult.get(0));

    three.addIfAlive(testResult);
    assertEquals(1, testResult.size());
    assertEquals(carrier, testResult.get(0));

    six.addIfAlive(testResult);
    assertEquals(1, testResult.size());
    assertEquals(carrier, testResult.get(0));

    seven.addIfAlive(testResult);
    assertEquals(1, testResult.size());
    assertEquals(carrier, testResult.get(0));

    eight.addIfAlive(testResult);
    assertEquals(2, testResult.size());
    assertEquals(submarine, testResult.get(1));

    nine.addIfAlive(testResult);
    assertEquals(2, testResult.size());
    assertEquals(submarine, testResult.get(1));
  }

  /**
   * Ensures the addIfAlive method will add ships that have been hit, but not ships that
   * have been sunk.
   */
  @Test
  public void testDontAddSunk() {
    submarine.addHitAt(new Coord(1, 1));
    submarine.addHitAt(new Coord(1, 2));
    submarine.addHitAt(new Coord(1, 3));
    submarine.addHitAt(new Coord(1, 4));

    carrier.addHitAt(new Coord(0, 1));
    carrier.addHitAt(new Coord(0, 5));
    carrier.addHitAt(new Coord(0, 6));

    ArrayList<ShipPiece> testResult = new ArrayList<>();

    one.addIfAlive(testResult);
    assertEquals(1, testResult.size());
    assertEquals(carrier, testResult.get(0));

    seven.addIfAlive(testResult);
    assertEquals(1, testResult.size());
    assertFalse(testResult.contains(submarine));
  }

  /**
   * Tests the getShip method
   */
  @Test
  public void testGetShip() {
    assertEquals(carrier, one.getShip());
    assertEquals(carrier, two.getShip());

    assertEquals(submarine, eight.getShip());
    assertEquals(submarine, nine.getShip());

    assertEquals(null, seven.getShip());
    assertEquals(null, twelve.getShip());
  }
}

