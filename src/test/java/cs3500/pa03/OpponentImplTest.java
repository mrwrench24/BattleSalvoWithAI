package cs3500.pa03;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.pa03.game.OpponentImpl;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.ShipPiece;
import cs3500.pa03.model.ShipType;
import cs3500.pa03.random.MockRandom;
import cs3500.pa03.random.Randomable;
import cs3500.pa03.view.GameViewer;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the opponent implementation
 */
public class OpponentImplTest {

  ByteArrayOutputStream consoleContents = new ByteArrayOutputStream();
  GameViewer viewer = new GameViewer(new PrintStream(consoleContents));
  OpponentImpl exampleOpponent;

  HashMap<ShipType, Integer> oneEach = new HashMap<ShipType, Integer>();

  /**
   * INitializes data before tests
   */
  @BeforeEach
  public void initData() {
    Randomable rand = new MockRandom();
    exampleOpponent = new OpponentImpl(viewer, rand);
    oneEach.put(ShipType.SUBMARINE, 1);
    oneEach.put(ShipType.DESTROYER, 1);
    oneEach.put(ShipType.BATTLESHIP, 1);
    oneEach.put(ShipType.CARRIER, 1);

    exampleOpponent.setup(10, 10, oneEach);
  }

  /**
   * Tests the name method
   */
  @Test
  public void testName() {
    assertEquals("pa04-ilovemakingtemplates", exampleOpponent.name());
  }

  @Test
  public void testSetup() {
    ArrayList<ShipPiece> result = exampleOpponent.setup(10, 10, oneEach);
    assertEquals(4, result.size());
  }

  /**
   * Tests the takeShots method - ensures it only outputs valid coordinates
   */
  @Test
  public void testTakeShots() {
    // 4 shots
    List<Coord> result = exampleOpponent.takeShots();
    assertEquals(new Coord(4, 5), result.get(0));
    assertEquals(new Coord(5, 5), result.get(1));
    assertEquals(new Coord(6, 5), result.get(2));
    assertEquals(new Coord(4, 6), result.get(3));
  }

  /**
   * Tests the report damage method
   */
  @Test
  public void testReportDamage() {

    ArrayList<Coord> allMisses =
        new ArrayList<>(Arrays.asList(new Coord(3, 2), new Coord(9, 3), new Coord(8, 3)));

    ArrayList<Coord> someHits =
        new ArrayList<>(Arrays.asList(new Coord(5, 7), new Coord(2, 3), new Coord(0, 4)));

    ArrayList<Coord> someHitsResponse =
        new ArrayList<>(Arrays.asList(new Coord(5, 7), new Coord(2, 3)));

    ArrayList<Coord> allHits =
        new ArrayList<>(Arrays.asList(new Coord(2, 3), new Coord(2, 5), new Coord(5, 7)));

    assertEquals(new ArrayList<Coord>(), exampleOpponent.reportDamage(allMisses));
    assertEquals(someHitsResponse, exampleOpponent.reportDamage(someHits));
    assertEquals(allHits, exampleOpponent.reportDamage(allHits));
  }
}
