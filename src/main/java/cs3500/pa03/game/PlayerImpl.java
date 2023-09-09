package cs3500.pa03.game;

import cs3500.pa03.controller.SalvoController;
import cs3500.pa03.controller.UserInputHandler;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.ShipPiece;
import cs3500.pa03.model.ShipType;
import cs3500.pa03.random.Randomable;
import cs3500.pa03.view.GameViewer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * An implementation of the Player interface for the Battleship game.
 */
public class PlayerImpl extends AbstractPlayer {
  private UserInputHandler handler;

  /**
   * Builds a new Human player, input is read from given UIH.
   *
   * @param toReadFrom the UIH to read from
   * @param viewer the viewer to write to
   */
  public PlayerImpl(UserInputHandler toReadFrom, GameViewer viewer) {
    super(viewer);
    this.handler = toReadFrom;
  }

  /**
   * Builds a new Human player, using given random. Input is read from given
   * UIH.
   *
   * @param toReadFrom the UIH to read from
   * @param viewer the viewer to write to
   * @param rand the random to use
   */
  public PlayerImpl(UserInputHandler toReadFrom, GameViewer viewer, Randomable rand) {
    super(viewer, rand);
    this.handler = toReadFrom;
  }

  /**
   * Get the player's name.
   * NOTE: This may not be important to your implementation for PA03, but it will be later
   *
   * @return the player's name
   */
  @Override
  public String name() {
    return "Human Being";
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
    viewer.display(this.board.toString(true));
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
    SalvoController controller = new SalvoController(board.getWidth(), board.getHeight(),
        this.handler, this.viewer);
    return controller.runSession(board.getRemainingShips().size());
  }

  /**
   * Reports to this player what shots in their previous volley returned from takeShots()
   * successfully hit an opponent's ship.
   *
   * @param shotsThatHitOpponentShips the list of shots that successfully hit the opponent's ships
   */
  @Override
  public void successfulHits(List<Coord> shotsThatHitOpponentShips) {
    for (Coord shot : shotsThatHitOpponentShips) {
      viewer.display("Successful Hit: " + shot.toString());
    }
  }

  /**
   * Given the list of shots the opponent has fired on this player's board, report which
   * shots hit a ship on this player's board.
   *
   * @param opponentShotsOnBoard - the opponent's shots on this player's board
   * @return - a filtered list of the given shots that contain all locations of shots that hit a
   *           ship on this board
   */
  @Override
  public ArrayList<Coord> reportDamage(List<Coord> opponentShotsOnBoard) {
    ArrayList<Coord> hits = this.board.hitAndRespond(opponentShotsOnBoard);
    viewer.display(this.board.toString(true));
    return hits;
  }

  /**
   * Notifies the player that the game is over.
   * Win, lose, and draw should all be supported
   *
   * @param result if the player has won, lost, or forced a draw
   * @param reason the reason for the game ending
   */
  @Override
  public void endGame(GameResult result, String reason) {
    return;
  }
}
