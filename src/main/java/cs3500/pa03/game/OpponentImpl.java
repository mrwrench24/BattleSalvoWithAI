package cs3500.pa03.game;

import cs3500.pa03.model.BattleCell;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.ShipPiece;
import cs3500.pa03.model.ShipType;
import cs3500.pa03.random.RandomDecorator;
import cs3500.pa03.random.Randomable;
import cs3500.pa03.view.GameViewer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * An opponent, AI Player in a game of battleship
 */
public class OpponentImpl extends AbstractPlayer {
  private DecisionMaker decisionMaker;

  /**
   * Builds a new Opponent player
   *
   * @param viewer the viewer to write to
   */
  public OpponentImpl(GameViewer viewer) {
    super(viewer);
  }

  /**
   * Builds a new Opponent player using given Random
   *
   * @param rand the random number generator to use
   * @param viewer the viewer to write to
   */
  public OpponentImpl(GameViewer viewer, Randomable rand) {
    super(viewer, rand);
  }

  /**
   * Get the player's name.
   * NOTE: This may not be important to your implementation for PA03, but it will be later
   *
   * @return the player's name
   */
  @Override
  public String name() {
    return "pa04-ilovemakingtemplates";
  }

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
    ArrayList<ShipPiece> toReturn = super.setup(height, width, specifications);

    // modified to show the board since WE are the "opponent" in this case
    viewer.display(this.board.toString(true));
    this.decisionMaker =
        new DecisionMaker(this.board.getHeight(), this.board.getWidth(), this.rand);
    return toReturn;
  }

  /**
   * Returns this player's shots on the opponent's board. The number of shots returned should
   * equal the number of ships on this player's board that have not sunk.
   *
   * @return the locations of shots on the opponent's board
   */
  @Override
  public List<Coord> takeShots() {
    int numShots = this.board.getRemainingShips().size();
    return this.decisionMaker.takeNumShots(numShots);
  }

  /**
   * Given the list of shots the opponent has fired on this player's board, report which
   * shots hit a ship on this player's board.
   *
   * @param opponentShotsOnBoard the opponent's shots on this player's board
   * @return - a filtered list of the given shots that contain all locations of shots that hit a
   *           ship on this board
   */
  public ArrayList<Coord> reportDamage(List<Coord> opponentShotsOnBoard) {
    ArrayList<Coord> hits = this.board.hitAndRespond(opponentShotsOnBoard);
    viewer.display(this.board.toString(true));
    return hits;
  }

  /**
   * Reports to this player what shots in their previous volley returned from takeShots()
   * successfully hit an opponent's ship.
   *
   * @param shotsThatHitOpponentShips the list of shots that successfully hit the opponent's ships
   */
  @Override
  public void successfulHits(List<Coord> shotsThatHitOpponentShips) {
    decisionMaker.reportSuccessful(shotsThatHitOpponentShips);
  }
}
