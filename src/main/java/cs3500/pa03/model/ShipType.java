package cs3500.pa03.model;

/**
 * Represents a Battleship Ship piece. Has an associated length.
 */
public enum ShipType {
  /**
   * The sumbarine type ship.
   */
  SUBMARINE(3),
  /**
   * The destroyer type ship.
   */
  DESTROYER(4),
  /**
   * The battleship type ship.
   */
  BATTLESHIP(5),
  /**
   * The carrier type ship.
   */
  CARRIER(6);


  /**
   * The length of this ShipPiece.
   */
  public final int length;

  /**
   * Creates a new ship piece with given length.
   *
   * @param length - the length of the new ShipPiece.
   */
  private ShipType(int length) {
    this.length = length;
  }

  /**
   * Returns this ShipPiece's abbreviation, meaning the first letter of its name.
   *
   * @param inUppercase - whether the abbreviation should be delivered in uppercase or not.
   * @return - the abbreviation associated with this ShipPiece.
   */
  public String abbreviate(boolean inUppercase) {
    if (inUppercase) {
      return this.toString().substring(0, 1);
    } else {
      return this.toString().substring(0, 1).toLowerCase();
    }
  }
}
