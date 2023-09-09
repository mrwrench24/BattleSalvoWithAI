package cs3500.pa03.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa03.model.Coord;

/**
 * Transforms a ShipPiece into a JSON object.
 */
public class ShipAdapter {
  /**
   * The starting position of this ship. either the leftmost or upmost position of the ship, based
   */
  public Coord coord;
  /**
   * The length of this ship
   */
  public int length;
  /**
   * The direction of this ship, either VERTICAL or HORIZONTAL.
   */
  public String direction;

  /**
   * Constructs a new ShipAdapter which can be easily transformed into JSON.
   *
   * @param coord     - the starting position of this ship. either the leftmost or upmost position
   *                  of the ship, based on its direction.
   * @param length    - the length of this ship
   * @param direction - the direction of this ship, either VERTICAL or HORIZONTAL.
   */
  @JsonCreator
  public ShipAdapter(@JsonProperty("coord") Coord coord,
                     @JsonProperty("length") int length,
                     @JsonProperty("direction") String direction) {
    this.coord = coord;
    this.length = length;
    this.direction = direction;
  }
}
