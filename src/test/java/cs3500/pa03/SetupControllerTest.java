package cs3500.pa03;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.pa03.controller.SetupController;
import cs3500.pa03.controller.UserInputHandler;
import cs3500.pa03.game.OpponentImpl;
import cs3500.pa03.game.PlayerImpl;
import cs3500.pa03.model.Coord;
import cs3500.pa03.random.MockRandom;
import cs3500.pa03.view.GameViewer;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the setup controller
 */
public class SetupControllerTest {
  ByteArrayOutputStream consoleContents = new ByteArrayOutputStream();

  PlayerImpl exampleUser;
  OpponentImpl exampleOpponent;

  SetupController sizeController;
  SetupController fleetController;

  /**
   * Resets the controllers before each test
   */
  @BeforeEach
  public void initData() {
    UserInputHandler exampleHandler =
        new UserInputHandler(new StringReader("10 10 8 8 999 8 8 1 1 1 1 10 "
            + "10 1 1 1 1 0 0 1 1 2 2 3 3"));
    GameViewer forBoth = new GameViewer(new PrintStream(consoleContents));

    exampleUser = new PlayerImpl(exampleHandler, forBoth, new MockRandom());
    exampleOpponent = new OpponentImpl(forBoth, new MockRandom());

    sizeController = new SetupController(exampleUser, exampleOpponent, exampleHandler, forBoth);
    fleetController = new SetupController(exampleUser, exampleOpponent, exampleHandler, forBoth);
  }

  /**
   * Focused on handling/taking in valid and invalid input when prompting for fleet size.
   */
  @Test
  public void testSizeInputs() {
    sizeController.runSession();

    // should be 4 shots b/c there are 4 ships
    assertEquals(4, exampleOpponent.takeShots().size());
  }

  /**
   * Ensures the inputs are as expected
   */
  @Test
  public void testFleetInputs() {
    fleetController.runSession();

    // should be 4 shots b/c there are 4 ships, randomly generated
    assertEquals(4, exampleOpponent.takeShots().size());

    // should also have 4 shots
    ArrayList<Coord> expectedList = new ArrayList<Coord>(
        Arrays.asList(new Coord(1, 1), new Coord(1, 1), new Coord(0, 0),
            new Coord(1, 1)));
    assertEquals(expectedList, exampleUser.takeShots());
  }
}
