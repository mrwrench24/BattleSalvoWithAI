package cs3500.pa03;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.pa03.controller.UserInputHandler;
import cs3500.pa03.game.PlayerImpl;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.ShipType;
import cs3500.pa03.random.MockRandom;
import cs3500.pa03.random.Randomable;
import cs3500.pa03.view.GameViewer;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the player implementation
 */
public class PlayerImplTest {

  UserInputHandler exampleHandler =
      new UserInputHandler(new StringReader("0 0 3 4 100 -10 5 6 8 8"));

  ByteArrayOutputStream consoleContents = new ByteArrayOutputStream();
  GameViewer viewer = new GameViewer(new PrintStream(consoleContents));
  PlayerImpl examplePlayer;

  HashMap<ShipType, Integer> oneEach = new HashMap<ShipType, Integer>();

  /**
   * Initializes data before tests
   */
  @BeforeEach
  public void initData() {
    Randomable rand = new MockRandom();
    examplePlayer = new PlayerImpl(exampleHandler, viewer, rand);
    oneEach.put(ShipType.SUBMARINE, 1);
    oneEach.put(ShipType.DESTROYER, 1);
    oneEach.put(ShipType.BATTLESHIP, 1);
    oneEach.put(ShipType.CARRIER, 1);

    examplePlayer.setup(10, 10, oneEach);
  }

  /**
   * Tests the name method
   */
  @Test
  public void testName() {
    assertEquals("Human Being", examplePlayer.name());
  }

  /**
   * Ensures the board built is accurate/expected
   */
  @Test
  public void testBoard() {
    // when setup is called on the player, the board is displayed to them.
    assertEquals("Your Board:\n"
        + "oooooooooo\n"
        + "oooooooooo\n"
        + "oooooooooo\n"
        + "oosoocoooo\n"
        + "oosdbcoooo\n"
        + "oosdbcoooo\n"
        + "ooodbcoooo\n"
        + "ooodbcoooo\n"
        + "oooobcoooo\n"
        + "oooooooooo\n"
        + "\n", consoleContents.toString());
  }


  /**
   * Tests the report damage method
   */
  @Test
  public void testReportDamage() {
    ArrayList<Coord> allMisses = new ArrayList<Coord>(
        Arrays.asList(new Coord(0, 0), new Coord(2, 0), new Coord(5, 2)));

    ArrayList<Coord> someHits = new ArrayList<Coord>(
        Arrays.asList(new Coord(9, 0), new Coord(0, 3), new Coord(2, 3)));

    ArrayList<Coord> someHitsResponse = new ArrayList<Coord>(
        Arrays.asList(new Coord(2, 3)));

    ArrayList<Coord> allHits = new ArrayList<Coord>(
        Arrays.asList(new Coord(2, 3), new Coord(4, 6)));

    assertEquals(new ArrayList<Coord>(), examplePlayer.reportDamage(allMisses));
    assertEquals(someHitsResponse, examplePlayer.reportDamage(someHits));
    assertEquals(allHits, examplePlayer.reportDamage(allHits));
  }

  /**
   * Tests that successfulHits sends out message to user when successful
   */
  @Test
  public void testSuccessfulHits() {
    ArrayList<Coord> exampleCoords = new ArrayList<Coord>(
        Arrays.asList(new Coord(0, 0), new Coord(3, 7), new Coord(5, 8)));
    examplePlayer.successfulHits(exampleCoords);

    /*assertEquals("Your Board:\n"
        + "oooooooooo\n"
        + "oooooooooo\n"
        + "oooooooooo\n"
        + "coboosoooo\n"
        + "coboosoooo\n"
        + "cobodsoooo\n"
        + "cobodooooo\n"
        + "cobodooooo\n"
        + "cooodooooo\n"
        + "oooooooooo\n"
        + "\n"
        + "Successful Hit: (0, 0)\n"
        + "Successful Hit: (3, 7)\n"
        + "Successful Hit: (5, 8)\n", consoleContents.toString());*/
  }

  /**
   * Tests the takeShots method - reads from string above instead of System.in
   */
  @Test
  public void testTakeShots() {
    ArrayList<Coord> exampleList = new ArrayList<Coord>(
        Arrays.asList(new Coord(0, 0), new Coord(3, 4), new Coord(5, 6), new Coord(8, 8)));

    assertEquals(exampleList, examplePlayer.takeShots());
  }
}
