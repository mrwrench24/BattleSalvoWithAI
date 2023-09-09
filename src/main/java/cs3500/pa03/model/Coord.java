package cs3500.pa03.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a position of X and Y coordinates.
 */
public class Coord {
  /**
   * The x position of this Coord
   */
  public int x;
  /**
   * The y position of this Coord
   */
  public int y;

  /**
   * Creates a new posn using given x, y coordinates
   *
   * @param x - the x position to use
   * @param y - the y position to use
   */
  @JsonCreator
  public Coord(@JsonProperty("x") int x,
               @JsonProperty("y") int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Represents this Coord as a string.
   *
   * @return - this Coord as a String.
   */
  public String toString() {
    return "(" + this.x + ", " + this.y + ")";
  }

  /**
   * Overrides .equals to compare fields
   *
   * @param that - another object to compare to
   * @return - true if both are coords with equal x and y values
   */
  @Override
  public boolean equals(Object that) {
    if (that instanceof Coord) {
      Coord thatCoord = (Coord) that;
      return this.x == thatCoord.x && this.y == thatCoord.y;
    } else {
      return false;
    }
  }
}
