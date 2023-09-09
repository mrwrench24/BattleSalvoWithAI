package cs3500.pa03.model;

import cs3500.pa03.random.Randomable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents the board in a game of Battleship.
 */
public class GameBoard {
  private BattleCell[][] board;
  private Randomable rand;

  /**
   * Builds a new game board of given length and width, using the default constructor for cells
   * for each space in the board.
   *
   * @param rowLength - the number of rows / length the board should be.
   * @param colWidth  - the number of columns / width the board should be.
   * @param rand      - the random number generator to use.
   */
  public GameBoard(int rowLength, int colWidth, Randomable rand) {
    this.initBoard(rowLength, colWidth);
    this.rand = rand;
  }

  /**
   * Starts a new game board of given dimensions, each cell is initialized as empty/default
   * values.
   *
   * @param numRows - number of rows in board to build
   * @param numCols - number of columns in board to build
   */
  private void initBoard(int numRows, int numCols) {
    this.board = new BattleCell[numRows][numCols];

    for (int row = 0; row < board.length; row++) {
      for (int col = 0; col < board[row].length; col++) {
        board[row][col] = new BattleCell(row, col);
      }
    }
  }

  /**
   * Builds a new gameboard using given 2D array of cells as the board (for testing!).
   *
   * @param seededBoard - a 2D Array of cells this gameboard will reference. for testing.
   */
  public GameBoard(BattleCell[][] seededBoard) {
    this.board = seededBoard;
  }

  /**
   * Represents this board as a String, either making its immediate contents visible (for a user)
   * or not (for an opponent) based on given boolean value.
   *
   * @param visible - whether this board's contents should be made visible (meaning ships should
   *                be displayed) or not
   * @return - this board formatted as a String
   */
  public String toString(boolean visible) {
    StringBuilder boardString = new StringBuilder();

    if (visible) {
      boardString.append("Your Board:\n");
    } else {
      boardString.append("Opponent's Board:\n");
    }

    for (int row = 0; row < board.length; row++) {
      StringBuilder lineToAdd = new StringBuilder();

      for (int col = 0; col < board[row].length; col++) {
        BattleCell cell = board[row][col];
        if (visible) {
          lineToAdd.append(cell.playerToString());
        } else {
          lineToAdd.append(cell.opponentToString());
        }
      }

      lineToAdd.append("\n");
      boardString.append(lineToAdd);
    }

    return boardString.toString();
  }


  /**
   * Hits every single Coordinate in list and returns a list of those which
   * hit a ShipPiece. Invalid coordinates (given by opponent) are simply ignored - that's their
   * loss!
   *
   * @param shots - the list of shots fired
   * @return - the list of shots fired that hit a ship piece
   */
  public ArrayList<Coord> hitAndRespond(List<Coord> shots) {
    ArrayList<Coord> successfulHits = new ArrayList<>();

    for (Coord shot : shots) {
      try {
        BattleCell toHit = this.board[shot.y][shot.x];
        if (toHit.hitThis()) {
          successfulHits.add(shot);
        }
      } catch (IndexOutOfBoundsException e) {
        System.out.println("Invalid shot: " + shot.toString());
      }
    }

    return successfulHits;
  }

  /**
   * Builds a fleet of given specifications.
   *
   * @param specs - types of ships mapped to the number of times they should appear
   */
  public void buildFleet(Map<ShipType, Integer> specs) {
    ShipType[] types = ShipType.values();

    for (int tryCount = 0; tryCount < 200; tryCount++) {
      // resets the board at the beginning in case this isn't the first try
      this.initBoard(board.length, board[0].length);
      // done in reverse order so the biggest ships are placed first
      for (int i = types.length - 1; i >= 0; i--) {
        ShipType currentType = types[i];
        if (specs.containsKey(currentType)) {
          // if the placement is NOT successful
          if (!this.placeShipsOfType(currentType, specs.get(currentType))) {
            // resets the whole board placement process if caught in an impossible situation
            this.initBoard(board.length, board[0].length);
            i = types.length;
          }
        }
      }

      // if there aren't any ships touching each other, we can let this board be "the" board
      if (!this.hasAnyAdjacent()) {
        return;
      }
    }


  }

  /**
   * Returns true if there are no cells which have a ship and are adjacent to another cell with
   * a ship.
   *
   * @return - whether the ships are all at least one cell apart from each other.
   */
  public boolean hasAnyAdjacent() {
    for (int row = 0; row < board.length; row++) {
      for (int col = 0; col < board[row].length; col++) {
        if (board[row][col].alreadyHasShip()) {
          // if this cell is adjacent to another ship
          if (this.checkAround(row, col, board[row][col].getShip())) {
            return true;
          }
        }
      }
    }

    return false;
  }

  /**
   * Returns whether any cells adjacent to given row and column values have a ship.
   *
   * @param row        - the row we want to check around.
   * @param col        - the column we want to check around
   * @param okayToHave - the ship that the cell we are checking around has, and it's okay
   *                   if it is adjacent to it
   * @return - whether any cells around given row and column have ships.
   */
  private boolean checkAround(int row, int col, ShipPiece okayToHave) {
    try {
      if (board[row][col - 1].alreadyHasShip()
          && !board[row][col - 1].getShip().equals(okayToHave)) {
        return true;
      }
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Index out of bounds: " + row + ", " + col);
    }
    try {
      if (board[row][col + 1].alreadyHasShip()
          && !board[row][col + 1].getShip().equals(okayToHave)) {
        return true;
      }
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Index out of bounds: " + row + ", " + col);
    }
    try {
      if (board[row + 1][col].alreadyHasShip()
          && !board[row + 1][col].getShip().equals(okayToHave)) {
        return true;
      }
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Index out of bounds: " + row + ", " + col);
    }
    try {
      if (board[row - 1][col].alreadyHasShip()
          && !board[row - 1][col].getShip().equals(okayToHave)) {
        return true;
      }
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Index out of bounds: " + row + ", " + col);
    }

    return false;
  }

  /**
   * Places a certain amount of ships of the given type on this board.
   *
   * @param type      - the type of ship to be placed.
   * @param numOfType - the number of given type to be placed.
   */
  private boolean placeShipsOfType(ShipType type, int numOfType) {
    for (int i = 0; i < numOfType; i++) {
      int randomX = rand.nextInt(this.getWidth());
      int randomY = rand.nextInt(this.getHeight());
      ShipDirection[] directions = ShipDirection.values();
      ShipDirection randomDirection = directions[rand.nextInt(2)];

      int tryCount = 0;
      while (!this.validate(randomX, randomY, randomDirection, type.length)) {
        randomX = rand.nextInt(this.getWidth());
        randomY = rand.nextInt(this.getHeight());
        tryCount++;

        // if it tries to get a valid placement 100 times but is not successful, return false
        // and the process will restart.
        if (tryCount > 100) {
          return false;
        }
      }

      if (this.validate(randomX, randomY, randomDirection, type.length)) {
        this.addPiece(type, randomDirection, new Coord(randomX, randomY));
      } else {
        i--;
      }
    }

    return true;
  }

  /**
   * Returns whether there is enough space for a ship of hypothetical length to be placed on board
   * at given starting x and y location in given direction. Ships cannot overlap others, ones that
   * would would be considered invalid.
   *
   * @param startX    - the theoretical starting x position of the ship
   * @param startY    - the theoretical starting y position of the ship
   * @param direction - the direction the ship would be placed in
   * @param length    - how long the ship would be.
   * @return - whether a ship could be placed here and would not overlap anything.
   */
  private boolean validate(int startX, int startY, ShipDirection direction, int length) {
    try {
      if (direction.equals(ShipDirection.HORIZONTAL)) {
        for (int i = 0; i < length; i++) {
          if (this.board[startY][startX + i].alreadyHasShip()) {
            return false;
          }
        }
      } else {
        for (int i = 0; i < length; i++) {
          if (this.board[startY + i][startX].alreadyHasShip()) {
            return false;
          }
        }
      }

      return true;
    } catch (IndexOutOfBoundsException e) {
      return false;
    }
  }

  /**
   * Creates a new piece of given type at given coordinate on the board in the given
   * direction.
   *
   * @param type      - the type of ship we want to add to the board
   * @param direction - the direction we want the ship to be placed in
   * @param startAt   - where the ship should start from.
   */
  public void addPiece(ShipType type, ShipDirection direction, Coord startAt) {
    ShipPiece toAdd = new ShipPiece(type, direction);

    if (direction.equals(ShipDirection.HORIZONTAL)) {
      for (int i = 0; i < type.length; i++) {
        // set contents updates the piece's coordinates as well.
        board[startAt.y][startAt.x + i].setContents(toAdd);
      }
    } else {
      for (int i = 0; i < type.length; i++) {
        board[startAt.y + i][startAt.x].setContents(toAdd);
      }
    }
  }

  /**
   * returns this board's width
   *
   * @return - this board's width
   */
  public int getWidth() {
    return this.board[0].length;
  }

  /**
   * Returns this board's height
   *
   * @return - this board's height
   */
  public int getHeight() {
    return this.board.length;
  }

  /**
   * Counts the number of remaining, non-sunk ships on the board
   *
   * @return - the number of remaining, non-sunk ships on the board
   */
  public ArrayList<ShipPiece> getRemainingShips() {
    ArrayList<ShipPiece> remainingShips = new ArrayList<>();

    for (BattleCell[] row : this.board) {
      for (BattleCell cell : row) {
        cell.addIfAlive(remainingShips);
      }
    }

    return remainingShips;
  }
}
