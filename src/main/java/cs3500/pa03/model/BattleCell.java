package cs3500.pa03.model;

import java.util.ArrayList;

/**
 * Represents a cell on the board of the Battleship game.
 */
public class BattleCell {
  private ShipPiece contents;
  private boolean hit;
  private Coord location;

  /**
   * Creates a new BattleCell, setting its priorityValue to -1 as a default.
   *
   * @param row - the row of this cell.
   * @param col - the column of this cell.
   */
  public BattleCell(int row, int col) {
    contents = null;
    hit = false;
    // column = x, row = y. how many times do i have to remind you
    location = new Coord(col, row);
  }

  /**
   * Sets this cell to have given ship as its contents.
   *
   * @param ship - the ship that this BattleCell should contain.
   */
  public void setContents(ShipPiece ship) {
    this.contents = ship;
    ship.addPresenceAt(this.location);
  }

  /**
   * Returns this board represented as a String in the appropriate format for displaying an
   * opponent's board, meaning board contents are not directly revealed.
   *
   * @return - this board formatted as a String like an opponent's board.
   */
  public String opponentToString() {
    if (this.hit) {
      if (this.contents == null) {
        return "E";
      } else {
        return "X";
      }
    } else {
      return "o";
    }
  }

  /**
   * Returns this board represented as a String as if it belongs to the player and is their board,
   * meaning the contents are displayed.
   *
   * @return - this board formatted as a String with contents visible.
   */
  public String playerToString() {
    if (this.hit) {
      if (this.contents == null) {
        return "E";
      } else {
        return this.contents.abbreviate(true);
      }
    } else {
      if (this.contents == null) {
        return "o";
      } else {
        return this.contents.abbreviate(false);
      }
    }
  }

  /**
   * Handles the process of counting a certain type of ships. If this is just an empty cell
   * (no ship), no changes take place, otherwise, the counting process is delegated to the ship.
   *
   * @param targetType  - the type of ship we are trying to count
   * @param alreadySeen - the ships we have already seen
   */
  public void handleCounting(ShipType targetType, ArrayList<ShipPiece> alreadySeen) {
    if (this.contents != null) {
      this.contents.addIfNotPresent(targetType, alreadySeen);
    }
  }

  /**
   * Runs processes associated with hitting a cell in battleship.
   *
   * @return - whether the hit was successful, meaning it hit a ShipPiece.
   */
  public boolean hitThis() {
    this.hit = true;

    if (this.contents != null) {
      this.contents.addHitAt(this.location);
      return true;
    }

    return false;
  }

  /**
   * Adds this ship to the given list if it is not already present and is not sunk
   *
   * @param pieces - the list of pieces we have already seen
   */
  public void addIfAlive(ArrayList<ShipPiece> pieces) {
    if (contents != null && !pieces.contains(this.contents) && !this.contents.isSunk()) {
      pieces.add(this.contents);
    }
  }

  /**
   * Returns true if this cell has a ship, false if not.
   *
   * @return - whether this cell has a ship already or not.
   */
  public boolean alreadyHasShip() {
    return this.contents != null;
  }

  /**
   * Returns the contents of this cell.
   *
   * @return - the contents of this cell.
   */
  public ShipPiece getShip() {
    return this.contents;
  }
}
