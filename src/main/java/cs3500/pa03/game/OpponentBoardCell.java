package cs3500.pa03.game;

import cs3500.pa03.model.Coord;

/**
 * Represents a cell in an opponent's board that we are trying to track and
 * make inferences about.
 */
public class OpponentBoardCell {
  private int priorityValue = 0;
  private CellStatus status = CellStatus.EMPTY;
  private Coord thisLoc;

  /**
   * Represents a cell of an Opponent's board we are trying to track.
   *
   * @param thisLoc - the coordinate of this cell.
   */
  public OpponentBoardCell(Coord thisLoc) {
    this.thisLoc = thisLoc;
  }

  /**
   * Updates the status of this cell.
   *
   * @param newStatus - the new status this cell has
   */
  public void setStatus(CellStatus newStatus) {
    this.status = newStatus;
  }

  /**
   * Returns the status of this cell.
   *
   * @return - the status of this cell.
   */
  public CellStatus getStatus() {
    return this.status;
  }

  /**
   * Modifies the priority value of this cell by adding the given value to it. (To
   * subtract, just supply a negative number)
   *
   * @param toChange - the amount by which we want to increase/decrease this cell's
   *                 priority value.
   */
  public void modifyPriority(int toChange) {
    this.priorityValue += toChange;

    // keep it between 100 and 0
    this.priorityValue = Math.min(100, this.priorityValue);
    this.priorityValue = Math.max(0, this.priorityValue);
  }

  /**
   * Returns the priority value of this cell.
   *
   * @return - The priority value of this cell.
   */
  public int getPriority() {
    return this.priorityValue;
  }

  /**
   * Returns the coordinate of this cell.
   *
   * @return - the coordinate of this cell.
   */
  public Coord getCoord() {
    return this.thisLoc;
  }
}
