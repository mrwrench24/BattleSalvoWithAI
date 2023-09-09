package cs3500.pa03.game;

/**
 * Represents the possible outcomes for a game of Battleship. Relative to the "user" field
 * in the Battleship game. (Meaning, a "WIN" is a win for the "user" and a loss for the "opponent");
 */
public enum GameResult {
  /**
   * The user won the game.
   */
  WIN,
  /**
   * The user lost the game.
   */
  LOSE,
  /**
   * The game ended in a tie.
   */
  TIE;

  /**
   * Return
   *
   * @return - this GameResult as a String.
   */
  @Override
  public String toString() {
    if (this == WIN) {
      return "\"WIN\"";
    } else if (this == LOSE) {
      return "\"LOSE\"";
    } else {
      return "\"TIE\"";
    }
  }


}
