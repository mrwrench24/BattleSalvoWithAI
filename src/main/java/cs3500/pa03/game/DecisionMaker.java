package cs3500.pa03.game;

import cs3500.pa03.model.Coord;
import cs3500.pa03.random.Randomable;
import java.util.ArrayList;
import java.util.List;

/**
 * The A.I. behind the OpponentImpl. Has a "board tracker" which keeps track of what has happened
 * to the opponent's board so far and uses the information it has received to decide where shots
 * are most likely to be hits.
 */
public class DecisionMaker {
  private OpponentBoardTracker oppBoard;
  private Randomable rand;

  // temporarily holds the list of shots taken to update and then inform the board which were
  // actually misses
  private List<Coord> previousShots = new ArrayList<>();

  /**
   * builds a new DecisionMaker for a game of given dimensions using given randomable when needed.
   *
   * @param numRows - the number of rows in the game
   * @param numCols - the number of columns in the game
   * @param rand    - the randomable object to use when needed.
   */
  public DecisionMaker(int numRows, int numCols, Randomable rand) {
    this.oppBoard = new OpponentBoardTracker(numRows, numCols, rand);
    this.rand = rand;
  }

  /**
   * The main objective of this class. Forms a number of shots given at what it deems to be an
   * optimal location.
   *
   * @param numToTake - the number of shots to take
   *
   * @return - A list of shots to fire at the opponent deemed as "most likely" to have a ship.
   */
  public List<Coord> takeNumShots(int numToTake) {
    List<Coord> shotsToTake = this.oppBoard.getMostImportant(numToTake);

    for (Coord shot : shotsToTake) {
      // default each shot to be a miss, update to hit if we are lucky later.
      oppBoard.modify(shot, CellStatus.MISS);
    }

    this.previousShots = shotsToTake;
    return shotsToTake;
  }

  /**
   * Tells this decision maker that the given list of shots was successful and hit a ship.
   *
   * @param successfulHits - the list of shots that were successful
   */
  public void reportSuccessful(List<Coord> successfulHits) {
    for (Coord shot : successfulHits) {
      oppBoard.modify(shot, CellStatus.HIT);
      previousShots.remove(shot);
    }

    // now we know what "really" was a miss, can update that
    for (Coord shot : previousShots) {
      oppBoard.notifyMissAround(shot);
    }

    // reset list so never notifying a miss twice
    previousShots = new ArrayList<>();

    // does this after/separately so the 75% miss modification won't affect a successful hit.
    for (Coord shot : successfulHits) {
      oppBoard.notifyHitAround(shot);
    }

    System.out.println(oppBoard.toString(true));
    System.out.println(oppBoard.toString(false));
  }
}
