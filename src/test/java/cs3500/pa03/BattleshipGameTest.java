package cs3500.pa03;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cs3500.pa03.controller.UserInputHandler;
import cs3500.pa03.game.BattleshipGame;
import cs3500.pa03.game.OpponentImpl;
import cs3500.pa03.game.Player;
import cs3500.pa03.game.PlayerImpl;
import cs3500.pa03.random.MockRandom;
import cs3500.pa03.random.Randomable;
import cs3500.pa03.view.GameViewer;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * Tests a BattleshipGame
 */
public class BattleshipGameTest {
  // user will literally input every coordinate on a 7 by 7 board
  UserInputHandler handlerToUse = new UserInputHandler(new StringReader(
      "7 7 1 1 1 1 0 0 0 1 0 2 0 3 0 4 0 5 0 6 1 0 1 1 1 2 1 3 1 4 1 5 2 0 2 1 2 2 2 3 2 4 2 5 "
          + "3 0 3 1 3 2 3 3 3 4 3 5 4 0 4 1 4 2 4 3 4 4 4 5 5 0 5 1 5 2 5 3 5 4 5 5 1 6 2 6 3 6 4 "
          + "6 5 6 6 0 6 1 6 2 6 3 6 4 6 5 6 6"));

  ByteArrayOutputStream consoleContents = new ByteArrayOutputStream();
  GameViewer viewerToUse = new GameViewer(new PrintStream(consoleContents));

  Randomable mock = new MockRandom();

  Player testPlayer = new PlayerImpl(handlerToUse, viewerToUse, mock);
  Player testOpponent = new OpponentImpl(viewerToUse, mock);

  BattleshipGame testGame = new BattleshipGame(testPlayer, testOpponent, handlerToUse, viewerToUse);

  /**
   * Tests a full game of battleship, the anticipated console output is listed below.
   * The player will shoot at every single coordinate possible, as evidenced by that
   * lovely looking string above.
   */
  @Test
  public void testGame() {
    assertDoesNotThrow(() -> testGame.runGame());
    System.out.println(consoleContents.toString());
  }

  /*
  Welcome to BattleSalvo. Let's get setup.
  Enter a number between 6 and 15 for Board Length: Enter a number between 6 and 15
   for Board Width: Your fleet may have up to 7 ships.
  Enter a number between 1 and 7 for SUBMARINE: Enter a number between 1 and 7 for
  DESTROYER: Enter a number between 1 and 7 for BATTLESHIP: Enter a number between 1 and 7
   for CARRIER: Your Board:
  cobbbbb
  coooooo
  coooooo
  codoooo
  codsooo
  codsooo
  oodsooo

  Opponent's Board:
  ooooooo
  ooooooo
  ooooooo
  ooooooo
  ooooooo
  ooooooo
  ooooooo

  Enter 4 shots.
  Enter a number between 0 and 6 for Shot 1 x-coordinate: Enter a number between 0 and 6 for Shot 1
   y-coordinate: Enter a number between 0 and 6 for Shot 2 x-coordinate: Enter a number between 0
   and 6 for Shot 2 y-coordinate: Enter a number between 0 and 6 for Shot 3 x-coordinate: Enter a
   number between 0 and 6 for Shot 3 y-coordinate: Enter a number between 0 and 6 for Shot 4
   x-coordinate: Enter a number between 0 and 6 for Shot 4 y-coordinate: Opponent's Board:
  Xoooooo
  Xoooooo
  Xoooooo
  Xoooooo
  ooooooo
  ooooooo
  ooooooo

  Your Board:
  cobbbbb
  coooooE
  coooooo
  coDoooo
  codsooo
  codsEoo
  oodsooo

  Successful Hit: (0, 0)
  Successful Hit: (0, 1)
  Successful Hit: (0, 2)
  Successful Hit: (0, 3)
  Enter 4 shots.
  Enter a number between 0 and 6 for Shot 1 x-coordinate: Enter a number between 0 and 6
   for Shot 1 y-coordinate: Enter a number between 0 and 6 for Shot 2 x-coordinate: Enter
   a number between 0 and 6 for Shot 2 y-coordinate: Enter a number between 0 and 6 for Shot
    3 x-coordinate: Enter a number between 0 and 6 for Shot 3 y-coordinate: Enter a number
    between 0 and 6 for Shot 4 x-coordinate: Enter a number between 0 and 6 for Shot 4
     y-coordinate: Opponent's Board:
  XEooooo
  Xoooooo
  Xoooooo
  Xoooooo
  Xoooooo
  Xoooooo
  Eoooooo

  Your Board:
  CobbbbB
  coooooE
  cEooooo
  coDoooo
  codsooo
  codsEoo
  oodsooo

  Successful Hit: (0, 4)
  Successful Hit: (0, 5)
  Enter 4 shots.
  Enter a number between 0 and 6 for Shot 1 x-coordinate: Enter a number between
   0 and 6 for Shot 1 y-coordinate: Enter a number between 0 and 6 for Shot 2 x-coordinate:
   Enter a number between 0 and 6 for Shot 2 y-coordinate: Enter a number between 0 and 6 for
    Shot 3 x-coordinate: Enter a number between 0 and 6 for Shot 3 y-coordinate: Enter a number
    between 0 and 6 for Shot 4 x-coordinate: Enter a number between 0 and 6 for Shot 4
    y-coordinate: Opponent's Board:
  XEooooo
  XEooooo
  XEooooo
  XEooooo
  XEooooo
  Xoooooo
  Eoooooo

  Your Board:
  CobbbbB
  coooooE
  cEooEoo
  coDoooo
  codsooo
  codsEoo
  oodsooo

  Enter 4 shots.
  Enter a number between 0 and 6 for Shot 1 x-coordinate: Enter a number between 0 and 6
  for Shot 1 y-coordinate: Enter a number between 0 and 6 for Shot 2 x-coordinate: Enter a number
  between 0 and 6 for Shot 2 y-coordinate: Enter a number between 0 and 6 for Shot 3 x-coordinate:
   Enter a number between 0 and 6 for Shot 3 y-coordinate: Enter a number between 0 and 6 for Shot
    4 x-coordinate: Enter a number between 0 and 6 for Shot 4 y-coordinate: Opponent's Board:
  XEXoooo
  XEEoooo
  XEEoooo
  XEooooo
  XEooooo
  XEooooo
  Eoooooo

  Your Board:
  CobbbbB
  coooooE
  cEooEoo
  coDooEo
  codSooo
  codsEoo
  oodsooo

  Successful Hit: (2, 0)
  Enter 4 shots.
  Enter a number between 0 and 6 for Shot 1 x-coordinate: Enter a number between 0 and 6 for Shot
  1 y-coordinate: Enter a number between 0 and 6 for Shot 2 x-coordinate: Enter a number between
   0 and 6 for Shot 2 y-coordinate: Enter a number between 0 and 6 for Shot 3 x-coordinate:
    Enter a number between 0 and 6 for Shot 3 y-coordinate: Enter a number between 0 and 6 for
    Shot 4 x-coordinate: Enter a number between 0 and 6 for Shot 4 y-coordinate: Opponent's Board:
  XEXXooo
  XEEoooo
  XEEoooo
  XEXoooo
  XEXoooo
  XEXoooo
  Eoooooo

  Your Board:
  CoBbbbB
  coEoooE
  cEooEoo
  coDooEo
  codSooo
  codsEoo
  oodsooo

  Successful Hit: (2, 3)
  Successful Hit: (2, 4)
  Successful Hit: (2, 5)
  Successful Hit: (3, 0)
  Enter 4 shots.
  Enter a number between 0 and 6 for Shot 1 x-coordinate: Enter a number between 0 and 6 for Shot
  1 y-coordinate: Enter a number between 0 and 6 for Shot 2 x-coordinate: Enter a number between
  0 and 6 for Shot 2 y-coordinate: Enter a number between 0 and 6 for Shot 3 x-coordinate: Enter a
  number between 0 and 6 for Shot 3 y-coordinate: Enter a number between 0 and 6 for Shot 4
  x-coordinate: Enter a number between 0 and 6 for Shot 4 y-coordinate: Opponent's Board:
  XEXXooo
  XEEEooo
  XEEEooo
  XEXEooo
  XEXXooo
  XEXoooo
  Eoooooo

  Your Board:
  CoBbbbB
  coEEooE
  cEooEoo
  CoDoEEo
  codSooo
  codsEoo
  oodsooo

  Successful Hit: (3, 4)
  Enter 4 shots.
  Enter a number between 0 and 6 for Shot 1 x-coordinate: Enter a number between 0 and 6
  for Shot 1 y-coordinate: Enter a number between 0 and 6 for Shot 2 x-coordinate:
  Enter a number between 0 and 6 for Shot 2 y-coordinate: Enter a number between 0 and 6
   for Shot 3 x-coordinate: Enter a number between 0 and 6 for Shot 3 y-coordinate:
   Enter a number between 0 and 6 for Shot 4 x-coordinate: Enter a number between 0 and 6
   for Shot 4 y-coordinate: Opponent's Board:
  XEXXXoo
  XEEEEoo
  XEEEEoo
  XEXEooo
  XEXXooo
  XEXXooo
  Eoooooo

  Your Board:
  CoBbbbB
  coEEooE
  cEooEoo
  CoDoEEo
  coDSooo
  codsEoo
  oodsooo

  Successful Hit: (3, 5)
  Successful Hit: (4, 0)
  Enter 4 shots.
  Enter a number between 0 and 6 for Shot 1 x-coordinate: Enter a number between 0 and 6
  for Shot 1 y-coordinate: Enter a number between 0 and 6 for Shot 2 x-coordinate: Enter a
   number between 0 and 6 for Shot 2 y-coordinate: Enter a number between 0 and 6 for Shot 3
   x-coordinate: Enter a number between 0 and 6 for Shot 3 y-coordinate: Enter a number between
    0 and 6 for Shot 4 x-coordinate: Enter a number between 0 and 6 for Shot 4 y-coordinate:
     Opponent's Board:
  XEXXXXo
  XEEEEoo
  XEEEEoo
  XEXEEoo
  XEXXEoo
  XEXXEoo
  Eoooooo

  Your Board:
  CoBbbbB
  coEEooE
  cEooEoE
  CoDoEEo
  coDSooo
  codsEoo
  oodsooo

  Successful Hit: (5, 0)
  Enter 4 shots.
  Enter a number between 0 and 6 for Shot 1 x-coordinate: Enter a number between 0 and 6 for Shot 1
   y-coordinate: Enter a number between 0 and 6 for Shot 2 x-coordinate: Enter a number between
   0 and 6 for Shot 2 y-coordinate: Enter a number between 0 and 6 for Shot 3 x-coordinate:
    Enter a number between 0 and 6 for Shot 3 y-coordinate: Enter a number between 0 and 6 for
    Shot 4 x-coordinate: Enter a number between 0 and 6 for Shot 4 y-coordinate: Opponent's Board:
  XEXXXXo
  XEEEEEo
  XEEEEEo
  XEXEEEo
  XEXXEEo
  XEXXEoo
  Eoooooo

  Your Board:
  CoBbbbB
  coEEooE
  cEooEoE
  CoDoEEo
  cEDSooo
  codsEEo
  oodsooo

  Enter 4 shots.
  Enter a number between 0 and 6 for Shot 1 x-coordinate: Enter a number between 0 and 6 for Shot 1
   y-coordinate: Enter a number between 0 and 6 for Shot 2 x-coordinate: Enter a number between 0
   and 6 for Shot 2 y-coordinate: Enter a number between 0 and 6 for Shot 3 x-coordinate: Enter a
    number between 0 and 6 for Shot 3 y-coordinate: Enter a number between 0 and 6 for Shot 4
    x-coordinate: Enter a number between 0 and 6 for Shot 4 y-coordinate: Opponent's Board:
  XEXXXXo
  XEEEEEo
  XEEEEEo
  XEXEEEo
  XEXXEEo
  XEXXEEo
  EEXXooo

  Your Board:
  CoBbbbB
  coEEooE
  cEoEEoE
  CoDoEEo
  cEDSooo
  codsEEo
  oodsooo

  Successful Hit: (2, 6)
  Successful Hit: (3, 6)
  Enter 4 shots.
  Enter a number between 0 and 6 for Shot 1 x-coordinate: Enter a number between 0 and 6 for Shot
   1 y-coordinate: Enter a number between 0 and 6 for Shot 2 x-coordinate: Enter a number between
    0 and 6 for Shot 2 y-coordinate: Enter a number between 0 and 6 for Shot 3 x-coordinate: Enter
     a number between 0 and 6 for Shot 3 y-coordinate: Enter a number between 0 and 6 for Shot 4
     x-coordinate: Enter a number between 0 and 6 for Shot 4 y-coordinate: Opponent's Board:
  XEXXXXX
  XEEEEEE
  XEEEEEo
  XEXEEEo
  XEXXEEo
  XEXXEEo
  EEXXEEo

  Your Board:
  CoBbbbB
  coEEooE
  cEoEEoE
  CoDoEEo
  cEDSooo
  codsEEo
  oodsooo

  Successful Hit: (6, 0)
  Enter 4 shots.
  Enter a number between 0 and 6 for Shot 1 x-coordinate: Enter a number between 0 and 6 for Shot
   1 y-coordinate: Enter a number between 0 and 6 for Shot 2 x-coordinate: Enter a number between
    0 and 6 for Shot 2 y-coordinate: Enter a number between 0 and 6 for Shot 3 x-coordinate: Enter
    a number between 0 and 6 for Shot 3 y-coordinate: Enter a number between 0 and 6 for Shot 4
     x-coordinate: Enter a number between 0 and 6 for Shot 4 y-coordinate:
  */
}
