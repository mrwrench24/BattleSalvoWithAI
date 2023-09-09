package cs3500.pa03.game;

/**
 * Represents the status of a cell on an opponent's board to be tracked. Empty means it has not
 * been fired at, miss means it was fired at but not a successful hit, and hit means it was fired
 * at and contained a ship.
 */
public enum CellStatus {
  /**
   * cell status is empty.
   */
  EMPTY,
  /**
   * cell status is miss shot.
   */
  MISS,
  /**
   * cell status is hit shot.
   */
  HIT;

  /**
   * Returns the abbreviation of this CellStatus. "EMPTY" is represented as a lowercase e, the
   * others are represented as uppercase letters.
   *
   * @return - this status abbreviated as a String.
   */
  public String abbreviate() {
    if (this.equals(EMPTY)) {
      return this.toString().substring(0, 1).toLowerCase();
    } else {
      return this.toString().substring(0, 1);
    }

  }
}
