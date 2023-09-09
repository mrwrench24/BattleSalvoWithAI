package cs3500.pa03;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cs3500.pa03.game.Driver;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

/**
 * Tests the Driver class.
 */
public class DriverTest {

  /**
   * Tests input of invalid number of arguments
   */
  @Test
  public void testInvalidArgs() {
    ByteArrayOutputStream consoleContents = new ByteArrayOutputStream();
    System.setErr(new PrintStream(consoleContents));

    String[] oneArg = new String[] {"hello"};
    Driver.main(oneArg);
    assertEquals("Why would you enter 1 arguments??\n", consoleContents.toString());

    String[] threeArgs = new String[] {"hello", "world", "there"};
    Driver.main(threeArgs);
    assertEquals("Why would you enter 1 arguments??\nWhy would you enter 3 arguments??\n",
        consoleContents.toString());

    System.setErr(System.err);
  }

  /**
   * Tests running a classic game, which should print out a little to the outputStream
   * but will fail b/c nothing to read from accurately
   */
  @Test
  public void testClassicGame() {
    String[] emptyArray = new String[0];
    ByteArrayOutputStream consoleContents = new ByteArrayOutputStream();
    System.setOut(new PrintStream(consoleContents));

    assertThrows(
        NoSuchElementException.class,
        () -> Driver.main(emptyArray));

    assertEquals(
        "Welcome to BattleSalvo. Let's get setup.\n"
        + "Enter a number between 6 and 15 for Board Length: ", consoleContents.toString());

    System.setOut(System.out);
  }

  /**
   * Tests running a Networked game of battleship.
   */
  @Test
  public void testNetworkedGame() {
    String[] selfHostArray = new String[] {"0.0.0.0", "35001"};
    ByteArrayOutputStream consoleContents = new ByteArrayOutputStream();
    System.setErr(new PrintStream(consoleContents));

    Driver.main(selfHostArray);

    assertEquals("We got an unexpected error! java.net.Connect"
        + "Exception: Connection refused\n", consoleContents.toString());

    System.setErr(System.err);
  }
}
