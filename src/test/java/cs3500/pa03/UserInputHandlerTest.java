package cs3500.pa03;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cs3500.pa03.controller.UserInputHandler;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * Tests the userInputHandler
 */
public class UserInputHandlerTest {

  UserInputHandler handler = new UserInputHandler(new StringReader("1 2 3 50 -3 50"));

  /**
   * Tests the next input method. The file being read from is "1 2 3 4 5 6".
   */
  @Test
  public void testNextInput() {
    handler = new UserInputHandler(new StringReader("1 2 3 50 -3 50"));
    assertEquals(1, handler.nextInput(1, 1));
    assertEquals(2, handler.nextInput(0, 2));
    assertEquals(3, handler.nextInput(3, 84));

    //4
    assertThrows(
        IllegalArgumentException.class,
        () -> handler.nextInput(0, 3)
    );

    //5
    assertThrows(
        IllegalArgumentException.class,
        () -> handler.nextInput(5, -1)
    );

    //6
    assertThrows(
        IllegalArgumentException.class,
        () -> handler.nextInput(100, 200)
    );
  }
}
