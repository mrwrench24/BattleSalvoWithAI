package cs3500.pa03;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.pa03.controller.SalvoController;
import cs3500.pa03.controller.UserInputHandler;
import cs3500.pa03.model.Coord;
import cs3500.pa03.view.GameViewer;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Tests the salvo controller
 */
public class SalvoControllerTest {
  ByteArrayOutputStream consoleContents = new ByteArrayOutputStream();
  GameViewer viewer = new GameViewer(new PrintStream(consoleContents));

  UserInputHandler handler = new UserInputHandler(new StringReader("0 0 1 1 2 2 3 3 8 8 4 4"));

  SalvoController fiveShots = new SalvoController(5, 5, handler, viewer);

  /**
   * Tests the runSession method
   */
  @Test
  public void testRunSession() {
    List<Coord> result = fiveShots.runSession(5);
    assertEquals(5, result.size());
    assertEquals("(0, 0)", result.get(0).toString());
    assertEquals("(1, 1)", result.get(1).toString());
    assertEquals("(2, 2)", result.get(2).toString());
    assertEquals("(3, 3)", result.get(3).toString());
    assertEquals("(4, 4)", result.get(4).toString());
  }
}
