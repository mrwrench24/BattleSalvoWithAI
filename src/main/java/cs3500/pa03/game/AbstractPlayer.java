package cs3500.pa03.game;

import cs3500.pa03.model.Coord;
import cs3500.pa03.model.GameBoard;
import cs3500.pa03.model.ShipPiece;
import cs3500.pa03.model.ShipType;
import cs3500.pa03.random.RandomDecorator;
import cs3500.pa03.random.Randomable;
import cs3500.pa03.view.GameViewer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents an abstract implementation of a player.
 */
public abstract class AbstractPlayer implements Player {

  /**
   * The random number generator for this player.
   */
  protected Randomable rand;

  /**
   * The viewer that this player will write to.
   */
  protected GameViewer viewer;

  /**
   * The board that this player is playing on.
   */
  protected GameBoard board;

  /**
   * Builds a new Abstract Player that will write to the given viewer
   *
   * @param viewer the viewer to write to
   */
  public AbstractPlayer(GameViewer viewer) {
    this.viewer = viewer;
    this.rand = new RandomDecorator();
  }

  /**
   * Builds a new Abstract Player that will write to the given viewer and use the given random
   * number generator.
   *
   * @param viewer the viewer to write to
   * @param rand   the random number generator to use
   */
  public AbstractPlayer(GameViewer viewer, Randomable rand) {
    this.viewer = viewer;
    this.rand = rand;
  }

  /**
   * Get the player's name.
   * NOTE: This may not be important to your implementation for PA03, but it will be later
   *
   * @return the player's name
   */
  @Override
  public abstract String name();

  /**
   * Given the specifications for a BattleSalvo board, return a list of ships with their locations
   * on the board.
   *
   * @param height         the height of the board, range: [6, 15] inclusive
   * @param width          the width of the board, range: [6, 15] inclusive
   * @param specifications a map of ship type to the number of occurrences each ship should
   *                       appear on the board
   * @return the placements of each ship on the board
   */
  @Override
  public ArrayList<ShipPiece> setup(int height, int width, Map<ShipType, Integer> specifications) {
    this.board = new GameBoard(height, width, rand);
    this.board.buildFleet(specifications);
    return this.board.getRemainingShips();
  }

  /**
   * Returns this player's shots on the opponent's board. The number of shots returned should
   * equal the number of ships on this player's board that have not sunk.
   *
   * @return the locations of shots on the opponent's board
   */
  @Override
  public abstract List<Coord> takeShots();

  /**
   * Takes in a list of opponent's shots taken and updates board appropriately, returns a list
   * of shots that hit a ship. Invalid indexes are ignored.
   *
   * @param opponentShotsOnBoard the opponent's shots on this player's board
   * @return - list of opponent's shots which hit a ShipPiece successfully.
   */
  @Override
  public abstract ArrayList<Coord> reportDamage(List<Coord> opponentShotsOnBoard);

  /**
   * Reports to this player what shots in their previous volley returned from takeShots()
   * successfully hit an opponent's ship.
   *
   * @param shotsThatHitOpponentShips the list of shots that successfully hit the opponent's ships
   */
  @Override
  public abstract void successfulHits(List<Coord> shotsThatHitOpponentShips);

  /**
   * Notifies the player that the game is over.
   * Win, lose, and draw should all be supported
   *
   * @param result if the player has won, lost, or forced a draw
   * @param reason the reason for the game ending
   */
  @Override
  public void endGame(GameResult result, String reason) {
    viewer.display(result + " because " + reason);
  }
}
