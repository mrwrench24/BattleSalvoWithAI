package cs3500.pa03;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.pa03.view.GameViewer;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the salvoViewer class.
 */
public class GameViewerTest {

  ByteArrayOutputStream consoleContents = new ByteArrayOutputStream();
  GameViewer viewer = new GameViewer(new PrintStream(consoleContents));

  ByteArrayOutputStream errContents = new ByteArrayOutputStream();

  @BeforeEach
  public void setErrStream() {
    System.setErr(new PrintStream(errContents));
  }

  @AfterAll
  public static void resetErrStream() {
    System.setErr(System.err);
  }

  /**
   * Tests the display method
   */
  @Test
  public void testDisplay() {
    viewer.display("hello");
    assertEquals("hello\n", consoleContents.toString());

    viewer.display("goodbye");
    assertEquals("hello\ngoodbye\n", consoleContents.toString());
  }

  /**
   * Tests the promptInputFor method
   */
  @Test
  public void testPromptInputFor() {
    viewer.promptInputFor("Shot 1 X-Coordinate", 0, 7);

    assertEquals(
        "Enter a number between 0 and 7 for Shot 1 X-Coordinate: ",
        consoleContents.toString()
    );
  }

  /**
   * Tests the scold method
   */
  @Test
  public void testScold() {
    viewer.scold(3, 6);

    assertEquals(
        "Invalid input. Must be between 3 to 6, inclusive.\n", consoleContents.toString());
  }

  @Test
  public void testExceptions() throws IOException {

  }

}
