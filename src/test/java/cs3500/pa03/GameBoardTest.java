package cs3500.pa03;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa03.model.BattleCell;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.GameBoard;
import cs3500.pa03.model.ShipDirection;
import cs3500.pa03.model.ShipPiece;
import cs3500.pa03.model.ShipType;
import cs3500.pa03.random.MockRandom;
import cs3500.pa03.random.RandomDecorator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the gameBoard class and its methods
 */
public class GameBoardTest {
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

  BattleCell[][] cellArray = new BattleCell[][] {
      {one, two, three, four, five, six},
      {seven, eight, nine, ten, eleven, twelve}
  };

  GameBoard testBoard = new GameBoard(cellArray);

  GameBoard baseBoard = new GameBoard(2, 2, new MockRandom());

  HashMap<ShipType, Integer> placementSpecs = new HashMap<>();

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

    cellArray = new BattleCell[][] {
        {one, two, three, four, five, six},
        {seven, eight, nine, ten, eleven, twelve}
    };

    testBoard = new GameBoard(cellArray);

    placementSpecs = new HashMap<>();
    placementSpecs.put(ShipType.SUBMARINE, 2);
    placementSpecs.put(ShipType.DESTROYER, 2);
    placementSpecs.put(ShipType.BATTLESHIP, 2);
    placementSpecs.put(ShipType.CARRIER, 1);
  }

  /**
   * Tests the toString method, both for visible and non-visible representations
   */
  @Test
  public void testToString() {
    assertEquals("Your Board:\n"
        + "cccccc\n"
        + "osssso\n", testBoard.toString(true));

    assertEquals("Opponent's Board:\n"
        + "oooooo\n"
        + "oooooo\n", testBoard.toString(false));

    one.hitThis();
    seven.hitThis();
    eight.hitThis();

    assertEquals("Your Board:\n"
        + "Cccccc\n"
        + "ESssso\n", testBoard.toString(true));

    assertEquals("Opponent's Board:\n"
        + "Xooooo\n"
        + "EXoooo\n", testBoard.toString(false));

    assertEquals("Your Board:\noo\noo\n", baseBoard.toString(true));
    assertEquals("Opponent's Board:\noo\noo\n", baseBoard.toString(false));
  }

  /**
   * Tests the addPiece method
   */
  @Test
  public void testAddPiece() {
    GameBoard emptyBoard = new GameBoard(6, 6, new MockRandom());

    assertEquals(0, emptyBoard.getRemainingShips().size());
    assertEquals("Your Board:\noooooo\noooooo\noooooo\noooooo\noooooo\noooooo\n",
        emptyBoard.toString(true));

    emptyBoard.addPiece(ShipType.CARRIER, ShipDirection.HORIZONTAL, new Coord(0, 0));

    assertEquals(1, emptyBoard.getRemainingShips().size());
    assertEquals("Your Board:\ncccccc\noooooo\noooooo\noooooo\noooooo\noooooo\n",
        emptyBoard.toString(true));

    emptyBoard.addPiece(ShipType.SUBMARINE, ShipDirection.VERTICAL, new Coord(2, 1));

    assertEquals(2, emptyBoard.getRemainingShips().size());
    assertEquals("Your Board:\ncccccc\noosooo\noosooo\noosooo\noooooo\noooooo\n",
        emptyBoard.toString(true));
  }

  /**
   * Tests the getWidth method
   */
  @Test
  public void testGetWidth() {
    assertEquals(2, baseBoard.getWidth());
    assertEquals(6, testBoard.getWidth());
  }

  /**
   * Tests the getHeight method
   */
  @Test
  public void testGetHeight() {
    assertEquals(2, baseBoard.getHeight());
    assertEquals(2, testBoard.getHeight());
  }

  /**
   * tests the hit and respond method
   */
  @Test
  public void testHitAndRespond() {
    final ArrayList<Coord> emptyList = new ArrayList<Coord>();
    final ArrayList<Coord> someSuccessful =
        new ArrayList<Coord>(Arrays.asList(new Coord(1, 0), new Coord(2, 0)));
    final ArrayList<Coord> allSuccessful =
        new ArrayList<Coord>(Arrays.asList(new Coord(1, 0), new Coord(2, 0), new Coord(3, 1)));

    ArrayList<Coord> nonSuccessfulArg = new ArrayList<Coord>(
        Arrays.asList(new Coord(0, 1), new Coord(5, 1)));
    ArrayList<Coord> someSuccessfulArg = new ArrayList<Coord>(
        Arrays.asList(new Coord(1, 0), new Coord(2, 0), new Coord(0, 1)));
    ArrayList<Coord> allSuccessfulArg = new ArrayList<Coord>(
        Arrays.asList(new Coord(1, 0), new Coord(2, 0), new Coord(3, 1)));

    assertEquals(emptyList, testBoard.hitAndRespond(nonSuccessfulArg));
    assertEquals(someSuccessful, testBoard.hitAndRespond(someSuccessfulArg));
    assertEquals(allSuccessful, testBoard.hitAndRespond(allSuccessfulArg));
    ArrayList<Coord> scaryCoordArg = new ArrayList<Coord>(
        Arrays.asList(new Coord(100, 100), new Coord(-80, 100),
            new Coord(454, -1349)));

    assertDoesNotThrow(() -> baseBoard.hitAndRespond(scaryCoordArg));
  }

  /**
   * tests the getRemainingShips method
   */
  @Test
  public void testGetRemainingShips() {
    assertEquals(2, testBoard.getRemainingShips().size());
    assertTrue(testBoard.getRemainingShips().contains(submarine));
    assertTrue(testBoard.getRemainingShips().contains(carrier));

    testBoard.hitAndRespond(
        new ArrayList<Coord>(
            Arrays.asList(new Coord(1, 1), new Coord(2, 1), new Coord(3, 1), new Coord(4, 1)))
    );

    assertEquals(1, testBoard.getRemainingShips().size());
    assertTrue(submarine.isSunk());

    assertEquals(0, baseBoard.getRemainingShips().size());
  }

  /**
   * tests the buildFleet method
   */
  @Test
  public void testBuildFleet() {
    HashMap<ShipType, Integer> specs = new HashMap<>();
    specs.put(ShipType.SUBMARINE, 1);
    specs.put(ShipType.CARRIER, 1);

    GameBoard buildBoard = new GameBoard(10, 10, new RandomDecorator());
    buildBoard.buildFleet(specs);
    assertEquals(2, buildBoard.getRemainingShips().size());

    HashMap<ShipType, Integer> roughSpecs = new HashMap<>();
    roughSpecs.put(ShipType.SUBMARINE, 1);
    roughSpecs.put(ShipType.DESTROYER, 2);
    roughSpecs.put(ShipType.BATTLESHIP, 2);
    roughSpecs.put(ShipType.CARRIER, 1);

    GameBoard bigBoard = new GameBoard(15, 15, new RandomDecorator());
    bigBoard.buildFleet(roughSpecs);
    assertEquals(6, bigBoard.getRemainingShips().size());
  }

  /**
   * Ensures that buildFleet placements will not (or at least, try) have ships next to
   * each other.
   */
  @Test
  public void testBuildFleetPlacement() {
    // works with a reasonable amount of ships
    for (int i = 0; i < 20; i++) {
      GameBoard placementBoard = new GameBoard(12, 12, new RandomDecorator());
      placementBoard.buildFleet(placementSpecs);
      assertFalse(placementBoard.hasAnyAdjacent());
    }
  }
}
