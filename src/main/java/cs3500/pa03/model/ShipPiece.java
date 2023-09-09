package cs3500.pa03.model;

import cs3500.pa03.json.ShipAdapter;
import java.util.ArrayList;

/**
 * Represents a Ship Piece in the game of Battleship
 */
public class ShipPiece {
  private ArrayList<Coord> locatedAt = new ArrayList<>();
  private ArrayList<Coord> hitAt = new ArrayList<>();
  private ShipType type;
  private ShipDirection direction;

  /**
   * Builds a new ShipPiece that is of given ShipType.
   *
   * @param type - the type that this ShipPiece is.
   * @param direction - the direction that this ShipPiece is facing.
   */
  public ShipPiece(ShipType type, ShipDirection direction) {
    this.type = type;
    this.direction = direction;
  }

  /**
   * Updates this piece to now have a presence at given coordinate
   *
   * @param toAdd - the location this piece is now present at
   */
  public void addPresenceAt(Coord toAdd) {
    locatedAt.add(toAdd);
  }

  /**
   * Updates this piece ot now be hit at given coord.
   *
   * @param toAdd - the coordinate this piece should now be hit at.
   */
  public void addHitAt(Coord toAdd) {
    hitAt.add(toAdd);
  }


  /**
   * Returns appropriate abbreviation associated with this piece's type, in upper/lower case
   * based on given boolean.
   *
   * @param inUppercase - whether abbreviation should be delivered in uppercase or not.
   * @return - the appropriate abbreviation, either in uppercase or not.
   */
  public String abbreviate(boolean inUppercase) {
    return this.type.abbreviate(inUppercase);
  }

  /**
   * Handles counting of a certain ShipType. If this piece is of given type and is not already
   * seen in the given list, it will be added; otherwise, no changes take place.
   *
   * @param targetType  - the type the list is trying to count.
   * @param alreadySeen - the pieces already seen by the list.
   */
  public void addIfNotPresent(ShipType targetType, ArrayList<ShipPiece> alreadySeen) {
    if (this.type.equals(targetType) && !alreadySeen.contains(this)) {
      alreadySeen.add(this);
    }
  }

  /**
   * Returns whether this ship is considered sunk, meaning it has been hit at every
   * location it has a presence at.
   *
   * @return - Whether this ship has sunk
   */
  public boolean isSunk() {
    for (Coord location : locatedAt) {
      if (!hitAt.contains(location)) {
        return false;
      }
    }

    return true;
  }

  /**
   * Returns a ShipAdapter mimicking this ship which can be used as JSON. As part of converting
   * to a ShipAdapter, determines the leftmost or uppermost point at which this ship is located,
   * based on its direction.
   *
   * @return - A ShipAdapter mimicking this ship.
   */
  public ShipAdapter toJson() {
    Coord thisStartsAt;

    if (this.direction.equals(ShipDirection.HORIZONTAL)) {
      int lowestColumnPresence = Integer.MAX_VALUE;
      int constantRow = this.locatedAt.get(0).y;
      for (Coord location : this.locatedAt) {
        if (location.x < lowestColumnPresence) {
          lowestColumnPresence = location.x;
        }
      }

      thisStartsAt = new Coord(lowestColumnPresence, constantRow);
    } else {
      int lowestRowPresence = Integer.MAX_VALUE;
      int constantCol = this.locatedAt.get(0).x;
      for (Coord location : this.locatedAt) {
        if (location.y < lowestRowPresence) {
          lowestRowPresence = location.y;
        }
      }

      thisStartsAt = new Coord(constantCol, lowestRowPresence);
    }

    return new ShipAdapter(thisStartsAt, this.type.length, this.direction.toString());
  }
}
