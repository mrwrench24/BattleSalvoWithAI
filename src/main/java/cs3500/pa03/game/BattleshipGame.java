package cs3500.pa03.game;

import cs3500.pa03.controller.SetupController;
import cs3500.pa03.controller.UserInputHandler;
import cs3500.pa03.model.Coord;
import cs3500.pa03.view.GameViewer;
import java.util.List;

/**
 * Represents a full game of Battleship. Holds two players, each of which holds their own
 * respective models.
 */
public class BattleshipGame {
  private Player user;
  private Player opponent;
  private UserInputHandler handler;
  private GameViewer viewer;

  /**
   * Creates a new battleship game played by the two players.
   *
   * @param forUser     - the player who is the human user
   * @param forOpponent - the player who is the opponent
   * @param handlerToUse - the user input handler to use
   * @param toWriteTo   - the game viewer to write to
   */
  public BattleshipGame(Player forUser, Player forOpponent, UserInputHandler handlerToUse,
                        GameViewer toWriteTo) {
    this.user = forUser;
    this.opponent = forOpponent;
    this.handler = handlerToUse;
    this.viewer = toWriteTo;
  }

  /**
   * Runs a game of Battleship!
   */
  public void runGame() {
    this.runSetup();
    while (this.runRounds()) {

    }
  }

  /**
   * Runs the setup for a game of Battleship
   */
  private void runSetup() {
    SetupController controller = new SetupController(user, opponent, handler, viewer);
    controller.runSession();
  }

  /**
   * Runs rounds of battleship repeatedly until the game is over.
   */
  private boolean runRounds() {
    List<Coord> userShots = user.takeShots();
    List<Coord> oppShots = opponent.takeShots();

    if (userShots.isEmpty() || oppShots.isEmpty()) {
      return false;
    }

    List<Coord> userThatHit = opponent.reportDamage(userShots);
    List<Coord> oppThatHit = user.reportDamage(oppShots);

    user.successfulHits(userThatHit);
    opponent.successfulHits(oppThatHit);

    return true;
  }
}
