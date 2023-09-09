package cs3500.pa03.game;

import cs3500.pa03.model.Coord;
import cs3500.pa03.random.Randomable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an opponent's board which we are trying to keep track of, regarding locations
 * that we have fired at and whether they were successful or not.
 */
public class OpponentBoardTracker {
  private OpponentBoardCell[][] tracker;
  private Randomable rand;

  private final double cutoffPercentage = 0.35;
  private final int doubleBoundPriority = 15;
  private final int singleBoundPriority = 10;

  private final int hitBonus = 60;
  private final double missModificationAdjacent = 0.8;
  private final int missModificationDiagonal = 18;

  /**
   * Builds a new OpponentBoardTracker of given dimensions. Used to keep track of shots fired
   * and whether they were successful or not.
   *
   * @param numRows - The number of rows in the boards for the game of Battleship to track.
   * @param numCols - The number of columns in the boards for the game of Battleship to track.
   * @param rand    - The randomable object to use when needed.
   */
  public OpponentBoardTracker(int numRows, int numCols, Randomable rand) {
    tracker = new OpponentBoardCell[numRows][numCols];
    this.rand = rand;

    int rowsBoundaryCutoff = (int) (numRows * cutoffPercentage);
    int colsBoundaryCutoff = (int) (numCols * cutoffPercentage);

    for (int row = 0; row < numRows; row++) {
      for (int col = 0; col < numCols; col++) {
        tracker[row][col] = new OpponentBoardCell(new Coord(col, row));
        this.initializeCoverage(row, col, rowsBoundaryCutoff, colsBoundaryCutoff, numRows, numCols);
      }
    }
  }

  /**
   * Updates the cell at given Coord in this board to have the given status.
   *
   * @param toChange  - the Coord of the cell we want to change.
   * @param newStatus - the status the cell has now.
   */
  public void modify(Coord toChange, CellStatus newStatus) {
    tracker[toChange.y][toChange.x].setStatus(newStatus);
  }

  /**
   * Updates cells around to reflect a "hit"
   *
   * @param justHit - the coordinate we just had a successful hit at.
   */
  public void notifyHitAround(Coord justHit) {
    int row = justHit.y;
    int col = justHit.x;

    int verticalAmountToAdd = hitBonus;
    int horizontalAmountToAdd = hitBonus;

    // randomly prioritize horizontal or vertical shots
    if (this.determinePriority(row, col)) {
      horizontalAmountToAdd = 100;
    } else {
      verticalAmountToAdd = 100;
    }

    boolean missUp = false;
    boolean missDown = false;
    boolean missLeft = false;
    boolean missRight = false;

    for (int i = 1; i <= 3; i++) {
      try {
        OpponentBoardCell cell = tracker[row][col - i];
        if (missLeft || cell.getStatus().equals(CellStatus.MISS)) {
          // prevents it from happening on the next cell
          missLeft = true;
        } else {
          cell.modifyPriority(horizontalAmountToAdd - (i * 15));
        }
      } catch (IndexOutOfBoundsException e) {
        // just ignore.
      }
      try {
        OpponentBoardCell cell = tracker[row][col + i];
        if (missRight || cell.getStatus().equals(CellStatus.MISS)) {
          missRight = true;
        } else {
          cell.modifyPriority(horizontalAmountToAdd - (i * 15));
        }
      } catch (IndexOutOfBoundsException e) {
        // just ignore.
      }
      try {
        OpponentBoardCell cell = tracker[row - i][col];
        if (missUp || cell.getStatus().equals(CellStatus.MISS)) {
          missUp = true;
        } else {
          cell.modifyPriority(verticalAmountToAdd - (i * 15));
        }
      } catch (IndexOutOfBoundsException e) {
        // just ignore.
      }
      try {
        OpponentBoardCell cell = tracker[row + i][col];
        if (missDown || cell.getStatus().equals(CellStatus.MISS)) {
          missDown = true;
        } else {
          cell.modifyPriority(verticalAmountToAdd - (i * 15));
        }
      } catch (IndexOutOfBoundsException e) {
        // just ignore.
      }
    }
  }

  /**
   * Returns true if horizontal shots should be prioritized, false if vertical should be.
   * Looks at the board and tries to make an educated decision about which should be prioritized,
   * if not, it randomly chooses one of the directions.
   *
   * @return - whether horizontal shots should be prioritized or not.
   */
  private boolean determinePriority(int row, int col) {
    int horizontalScore = this.calculateHorizontalScore(row, col);
    int verticalScore = this.calculateVerticalScore(row, col);

    int divergence = horizontalScore - verticalScore;

    // if the difference is pretty small, just do a random one
    if (divergence >= -5 && divergence <= 5) {
      return rand.nextInt(2) == 1;
    } else {
      if (horizontalScore > verticalScore) {
        return true;
      } else {
        return false;
      }
    }
  }

  /**
   * Calculates a priority score based on horizontal board information.
   *
   * @param row - the row index to analyze.
   * @param col - the column index to analyze.
   * @return - the priority score based on adjacent horizontal cells and their contents.
   */
  private int calculateHorizontalScore(int row, int col) {
    int score = 0;

    try {
      if (tracker[row][col + 1].getStatus().equals(CellStatus.EMPTY)) {
        score += 10;
      } else if (tracker[row][col + 1].getStatus().equals(CellStatus.HIT)) {
        score += 25;
      }
      if (tracker[row][col + 2].getStatus().equals(CellStatus.EMPTY)) {
        score += 15;
      } else if (tracker[row][col + 2].getStatus().equals(CellStatus.HIT)) {
        score += 25;
      }
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Out of bounds");
    }

    try {
      if (tracker[row][col - 1].getStatus().equals(CellStatus.EMPTY)) {
        score += 10;
      } else if (tracker[row][col - 1].getStatus().equals(CellStatus.HIT)) {
        score += 25;
      }
      if (tracker[row][col - 2].getStatus().equals(CellStatus.EMPTY)) {
        score += 15;
      } else if (tracker[row][col - 2].getStatus().equals(CellStatus.HIT)) {
        score += 25;
      }
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Out of bounds");
    }

    return score;
  }

  /**
   * Calculates a priority score based on vertical board information.
   *
   * @param row - the row index to analyze.
   * @param col - the column index to analyze.
   * @return - the priority score based on adjacent vertical cells and their contents.
   */
  private int calculateVerticalScore(int row, int col) {
    int score = 0;

    try {
      if (tracker[row + 1][col].getStatus().equals(CellStatus.EMPTY)) {
        score += 10;
      } else if (tracker[row + 1][col].getStatus().equals(CellStatus.HIT)) {
        score += 25;
      }
      if (tracker[row + 2][col].getStatus().equals(CellStatus.EMPTY)) {
        score += 15;
      } else if (tracker[row + 2][col].getStatus().equals(CellStatus.HIT)) {
        score += 25;
      }
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Out of bounds");
    }

    try {
      if (tracker[row - 1][col].getStatus().equals(CellStatus.EMPTY)) {
        score += 10;
      } else if (tracker[row - 1][col].getStatus().equals(CellStatus.HIT)) {
        score += 25;
      }
      if (tracker[row - 2][col].getStatus().equals(CellStatus.EMPTY)) {
        score += 15;
      } else if (tracker[row - 2][col].getStatus().equals(CellStatus.HIT)) {
        score += 25;
      }
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Out of bounds");
    }

    return score;
  }

  /**
   * Updates priority values based on information that hit at given cell was a miss.
   * Adjacent cells have their priority decreased by a certain percentage, while diagonal
   * cells have their priority increased somewhat.
   *
   * @param missed - coordinate of a shot we missed.
   */
  public void notifyMissAround(Coord missed) {
    int row = missed.y;
    int col = missed.x;

    try {
      OpponentBoardCell cell = tracker[row][col - 1];
      cell.modifyPriority((int) (-1 * missModificationAdjacent * cell.getPriority()));
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Out of bounds");
    }
    try {
      OpponentBoardCell cell = tracker[row][col + 1];
      cell.modifyPriority((int) (-1 * missModificationAdjacent * cell.getPriority()));
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Out of bounds");
    }
    try {
      OpponentBoardCell cell = tracker[row - 1][col];
      cell.modifyPriority((int) (-1 * missModificationAdjacent * cell.getPriority()));
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Out of bounds");
    }
    try {
      OpponentBoardCell cell = tracker[row + 1][col];
      cell.modifyPriority((int) (-1 * missModificationAdjacent * cell.getPriority()));
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Out of bounds");
    }

    try {
      OpponentBoardCell cell = tracker[row - 1][col - 1];
      cell.modifyPriority(missModificationDiagonal);
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Out of bounds");
    }
    try {
      OpponentBoardCell cell = tracker[row + 1][col - 1];
      cell.modifyPriority(missModificationDiagonal);
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Out of bounds");
    }
    try {
      OpponentBoardCell cell = tracker[row - 1][col + 1];
      cell.modifyPriority(missModificationDiagonal);
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Out of bounds");
    }
    try {
      OpponentBoardCell cell = tracker[row + 1][col + 1];
      cell.modifyPriority(missModificationDiagonal);
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Out of bounds");
    }
  }

  /**
   * Initializes coverage of the given cell based on its location and the "cutoff" values
   * at which a cell is in the "middle" or not. Cells within the boundaries are prioritized
   * more, within one of them are prioritized somewhat more, and within neither will start
   * with the default priority value.
   *
   * @param row       - the row index of the cell we are starting coverage on.
   * @param col       - the column index of the cell we are starting coverage on.
   * @param rowCutoff - the cutoff at which a row is either in the "middle" of the board
   *                  or not.
   * @param colCutoff - the cutoff at which a column is either in the "middle" of the board
   *                  or not.
   */
  private void initializeCoverage(int row, int col, int rowCutoff, int colCutoff, int totalRows,
                                  int totalCols) {
    boolean withinRows = (row > rowCutoff && row < totalRows - rowCutoff);
    boolean withinCols = (col > colCutoff && col < totalCols - colCutoff);

    if (withinRows && withinCols) {
      tracker[row][col].modifyPriority(doubleBoundPriority);
    } else if (withinRows && !withinCols) {
      tracker[row][col].modifyPriority(singleBoundPriority);
    } else if (!withinRows && withinCols) {
      tracker[row][col].modifyPriority(singleBoundPriority);
    }
  }

  /**
   * Returns cell at given index.
   *
   * @param row - row index of the cell desired.
   * @param col - column index of the cell desired
   * @return - requested cell at given indices
   */
  public OpponentBoardCell getCellAt(int row, int col) {
    return this.tracker[row][col];
  }

  /**
   * The main point of this class - determines the most valuable shots to take on the opponent's
   * board based on information that we know about it. It iterates over rows in random order to
   * look at each cell and consider adding it to the list if it is empty.
   *
   * @param numWanted - the number of shots we are allowed to take.
   * @return - the shots determined as most prudent to take.
   */
  public List<Coord> getMostImportant(int numWanted) {
    ArrayList<OpponentBoardCell> highestPriority = new ArrayList<>();
    ArrayList<Integer> rowIndexes = new ArrayList<>();

    for (int i = 0; i < tracker.length; i++) {
      rowIndexes.add(i);
    }

    while (!rowIndexes.isEmpty()) {
      int remainingRows = rowIndexes.size();
      int nextRowIndex = rowIndexes.remove(rand.nextInt(remainingRows));

      for (int i = 0; i < tracker[nextRowIndex].length; i++) {
        if (tracker[nextRowIndex][i].getStatus().equals(CellStatus.EMPTY)) {
          this.handlePotentiallyAdding(highestPriority, tracker[nextRowIndex][i], numWanted);
        }
      }
    }

    // convert each to a coordinate
    ArrayList<Coord> coordList = new ArrayList<>();
    for (OpponentBoardCell cell : highestPriority) {
      coordList.add(cell.getCoord());
    }
    return coordList;
  }

  /**
   * Handles potentially adding given cell to the given list based on the number of cells
   * allowed to be in the list. The cell is added no matter what if there are "empty" spots
   * in the list (and it's not already in there!). Otherwise, if the list is "full", it will
   * compare itself to cells in the list. If it finds one for which it has a higher priority value
   * than, next steps are taken: if the cells are close together, then the cell is either added
   * (replacing the one already in the list) or not based on its priority value as a %age. If the
   * cells are far apart (again, it has a higher priority value anyways) then it is added anyways.
   *
   * @param important - the list of cells we have already marked as important.
   * @param toAnalyze - the cell we want to analyze and consider adding.
   * @param numWanted - the number of cells we will want in the list at most.
   */
  private void handlePotentiallyAdding(ArrayList<OpponentBoardCell> important,
                                       OpponentBoardCell toAnalyze, int numWanted) {
    if (important.size() < numWanted && !important.contains(toAnalyze)) {
      important.add(toAnalyze);
    } else {
      for (OpponentBoardCell cellInList : important) {
        if (toAnalyze.getPriority() > cellInList.getPriority()
            && toAnalyze.getCoord() != cellInList.getCoord()) {

          // the cell we would add is close to the one we want to replace... if it's far away,
          // we just add it anyways to add more random spread
          if (Math.abs(toAnalyze.getCoord().x - cellInList.getCoord().y) < 3
              && Math.abs(toAnalyze.getCoord().y - cellInList.getCoord().y) < 3) {
            // the higher the priority, the higher we will "override" and use this cell
            boolean addAnyways = rand.nextInt(100) < toAnalyze.getPriority();
            if (!addAnyways) {
              return;
            }
          }
          important.remove(cellInList);
          important.add(toAnalyze);
          // break the loop if we add this cell.
          return;
        }
      }
    }
  }

  /**
   * Returns this board as a string. Based on supplied booleans, either returns abbreviations
   * for each cell or the priority value associated with each cell next to its contents abbreviated.
   *
   * @param useAbbreviations - whether to just use abbreviations.
   * @return - this board tracker represented as a String.
   */
  public String toString(boolean useAbbreviations) {
    StringBuilder result = new StringBuilder("Opponent's Board: \n");

    for (int row = 0; row < this.tracker.length; row++) {
      StringBuilder toAdd = new StringBuilder();

      for (int col = 0; col < this.tracker[0].length; col++) {
        if (useAbbreviations) {
          toAdd.append(this.tracker[row][col].getStatus().abbreviate());
        } else {
          toAdd.append(this.tracker[row][col].getPriority() + " " + "("
              + this.tracker[row][col].getStatus().abbreviate() + ") ");
        }
      }

      result.append(toAdd + "\n");
    }

    return result.toString();
  }
}