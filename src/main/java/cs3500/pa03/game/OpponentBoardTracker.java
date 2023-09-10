package cs3500.pa03.game;

import cs3500.pa03.model.Coord;
import cs3500.pa03.random.Randomable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an opponent's board which we are trying to keep track of, regarding locations
 * that we have fired at and whether they were successful or not. Contains method and information
 * regarding shots to generate/infer spots at which there may be a ship.
 */
public class OpponentBoardTracker {
    private OpponentBoardCell[][] tracker;
    private Randomable rand;

    private final double CUTOFF_PERCENTAGE = 0.35;
    private final int DOUBLE_BOUND_PRIORITY = 15;
    private final int SINGLE_BOUND_PRIORITY = 10;

    private final int HIT_BONUS = 60;
    private final double MISS_MODIFICATION_ADJACENT = 0.8;
    private final int MISS_MODIFICATION_DIAGONAL = 18;

    private final int IRRELEVANT_DIVERGENCE = 5;

    private final int DISTANCE_DECAY = 15;

    private final int ADJACENT_MISS_INFERENCE = 10;
    private final int ADJACENT_HIT_INFERENCE = 25;

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

        int rowsBoundaryCutoff = (int) (numRows * CUTOFF_PERCENTAGE);
        int colsBoundaryCutoff = (int) (numCols * CUTOFF_PERCENTAGE);

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
     * Updates cells around given Coord which reflects a successful hit. Determines a direction (horizontal
     * or vertical) which should receive extra priority based on nearby cells and their status.
     *
     * @param justHit - the coordinate we just had a successful hit at.
     */
    public void notifyHitAround(Coord justHit) {
        int verticalAmountToAdd = HIT_BONUS;
        int horizontalAmountToAdd = HIT_BONUS;

        // looks at information regarding cell status near this successful hit. Chooses a direction
        // (vertical or horizontal) which receives additional priority so the model is more likely to
        // take shots fully in one direction rather than spread it out over multiple turns.
        // I.E. it is better to get a hit and on your next turn shoot vertically and miss and then
        // shoot horizontally than to try both on the next turn and still have to finish it off
        // next turn anyways.
        if (this.determinePriority(justHit.y, justHit.x)) {
            horizontalAmountToAdd = 100;
        } else {
            verticalAmountToAdd = 100;
        }

        boolean missUpper = false;
        boolean missLower = false;
        boolean missLeft = false;
        boolean missRight = false;

        for (int i = 1; i <= 3; i++) {
            int copyI = i;
            int copyHorizontalAmountToAdd = horizontalAmountToAdd;
            CellVisitor<Integer> modifyHorizontalAndReturnEmpty = toModify -> {
                if (toModify.getStatus().equals(CellStatus.MISS)) {
                    return 1;
                } else {
                    toModify.modifyPriority(copyHorizontalAmountToAdd - (copyI * DISTANCE_DECAY));
                    return 0;
                }
            };

            if (!missLeft) {
                missLeft = this.visitInNumIncrements(justHit, modifyHorizontalAndReturnEmpty, AdjacencyDirection.LEFT, i) == 1;
            }

            if (!missRight) {
                missRight = this.visitInNumIncrements(justHit, modifyHorizontalAndReturnEmpty, AdjacencyDirection.RIGHT, i) == 1;
            }

            int copyVerticalAmountToAdd = verticalAmountToAdd;
            CellVisitor<Integer> modifyVerticalAndReturnEmpty = toModify -> {
                if (toModify.getStatus().equals(CellStatus.MISS)) {
                    return 1;
                } else {
                    toModify.modifyPriority(copyVerticalAmountToAdd - (copyI * DISTANCE_DECAY));
                    return 0;
                }
            };

            if (!missUpper) {
                missUpper = this.visitInNumIncrements(justHit, modifyVerticalAndReturnEmpty, AdjacencyDirection.UPPER, i) == 1;
            }

            if (!missLower) {
                missLower = this.visitInNumIncrements(justHit, modifyVerticalAndReturnEmpty, AdjacencyDirection.LOWER, i) == 1;
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

        // if the difference is pretty small, just choose a random direction
        if (Math.abs(divergence) <= IRRELEVANT_DIVERGENCE) {
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
     * Calculates a score based on cells to the left and right of the given row and column index.
     * The score is used to determine how likely another spot with a ship is to the left or right
     * of the given coordinate.
     *
     * @param row - the row index to analyze.
     * @param col - the column index to analyze.
     * @return - the priority score based on adjacent horizontal cells and their contents.
     */
    private int calculateHorizontalScore(int row, int col) {
        Coord loc = new Coord(col, row);

        CellVisitor<Integer> modifier = (CellVisitor) toModify -> {
            if (toModify.getStatus().equals(CellStatus.EMPTY)) {
                return ADJACENT_MISS_INFERENCE;
            } else if (toModify.getStatus().equals(CellStatus.HIT)) {
                return ADJACENT_HIT_INFERENCE;
            }

            return 0;
        };

        return this.visitInNumIncrements(loc, modifier, AdjacencyDirection.LEFT, 1)
                + this.visitInNumIncrements(loc, modifier, AdjacencyDirection.LEFT, 2)
                + this.visitInNumIncrements(loc, modifier, AdjacencyDirection.RIGHT, 1)
                + this.visitInNumIncrements(loc, modifier, AdjacencyDirection.RIGHT, 2);
    }

    /**
     * Calculates a score based on cells above and below the given row and column index.
     * The score is used to determine how likely another spot with a ship is above or below
     * the given coordinate.
     *
     * @param row - the row index to analyze.
     * @param col - the column index to analyze.
     * @return - the priority score based on adjacent vertical cells and their contents.
     */
    private int calculateVerticalScore(int row, int col) {
        Coord loc = new Coord(col, row);

        CellVisitor<Integer> modifier = (CellVisitor) toModify -> {
            if (toModify.getStatus().equals(CellStatus.EMPTY)) {
                return ADJACENT_MISS_INFERENCE;
            } else if (toModify.getStatus().equals(CellStatus.HIT)) {
                return ADJACENT_HIT_INFERENCE;
            }

            return 0;
        };

        return this.visitInNumIncrements(loc, modifier, AdjacencyDirection.UPPER, 1)
                + this.visitInNumIncrements(loc, modifier, AdjacencyDirection.UPPER, 2)
                + this.visitInNumIncrements(loc, modifier, AdjacencyDirection.LOWER, 1)
                + this.visitInNumIncrements(loc, modifier, AdjacencyDirection.LOWER, 2);
    }

    /**
     * Updates priority values based on information that hit at given cell was a miss.
     * Adjacent cells have their priority decreased by a certain percentage, while diagonal
     * cells have their priority increased modestly.
     *
     * @param missed - coordinate of a shot we missed.
     */
    public void notifyMissAround(Coord missed) {
        CellVisitor adjacentModifier = new CellVisitor() {
            @Override
            public Void apply(OpponentBoardCell toModify) {
                toModify.modifyPriority((int) (-1 * MISS_MODIFICATION_ADJACENT * toModify.getPriority()));
                return null;
            }
        };
        this.updateAdjacent(missed, adjacentModifier);

        CellVisitor diagonalModifier = new CellVisitor() {
            @Override
            public Void apply(OpponentBoardCell toModify) {
                toModify.modifyPriority(MISS_MODIFICATION_DIAGONAL);
                return null;
            }
        };
        this.updateDiagonal(missed, diagonalModifier);
    }

    /**
     * Initializes coverage of the given cell based on its location and the "cutoff" values
     * at which a cell is in the "middle" or not. Cells within both boundaries are prioritized
     * more, within one of them are prioritized somewhat more, and within neither will start
     * with the default priority value (as designated in the OppBoardCell class itself)
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
            tracker[row][col].modifyPriority(DOUBLE_BOUND_PRIORITY);
        } else if (withinRows && !withinCols) {
            tracker[row][col].modifyPriority(SINGLE_BOUND_PRIORITY);
        } else if (!withinRows && withinCols) {
            tracker[row][col].modifyPriority(SINGLE_BOUND_PRIORITY);
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
     * @param important - the list of cells we have already marked as important (to shoot at)
     * @param toAnalyze - the cell we want to analyze and consider adding.
     * @param numWanted - the number of cells we will want in the list at most.
     */
    private void handlePotentiallyAdding(ArrayList<OpponentBoardCell> important,
                                         OpponentBoardCell toAnalyze, int numWanted) {
        // start by just adding cells. we may as well take shots and the server gets mad if we
        // don't return the correct number
        if (important.size() < numWanted && !important.contains(toAnalyze)) {
            important.add(toAnalyze);
        } else {
            for (OpponentBoardCell cellInList : important) {
                // only consider replacing Coords already in the list with ones that have a higher priority val
                if (toAnalyze.getPriority() > cellInList.getPriority()
                        && toAnalyze.getCoord() != cellInList.getCoord()) {

                    // if the cell we would add is close to the one we want to replace, it becomes a little trickier
                    // for it to be added to the list. But, if it's far away, we just add it anyways to have more random spread
                    if (Math.abs(toAnalyze.getCoord().x - cellInList.getCoord().y) < 3
                            && Math.abs(toAnalyze.getCoord().y - cellInList.getCoord().y) < 3) {
                        // the higher the priority, the higher probability we "override" and use this cell
                        boolean addAnyways = rand.nextInt(100) < toAnalyze.getPriority();
                        if (!addAnyways) {
                            return;
                        }
                    }
                    important.remove(cellInList);
                    important.add(toAnalyze);
                    // break the loop if we add this cell. don't compare it to the rest in the list
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
        StringBuilder result = new StringBuilder("Opponent's Board: (Shots taken by A.I.) \n");

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

    /**
     * Applies given MathModifier to cells immediately adjacent to the given Coord. Any indices/cells which are
     * out of bounds are simply ignored.
     *
     * @param location - the Coord of the cell for which we want to modify adjacent cells.
     * @param modifier - the modifier we want to apply to cells.
     */
    private void updateAdjacent(Coord location, CellVisitor modifier) {
        Coord leftCoord = AdjacencyDirection.LEFT.moveInDirection(location);
        Coord rightCoord = AdjacencyDirection.RIGHT.moveInDirection(location);
        Coord upperCoord = AdjacencyDirection.UPPER.moveInDirection(location);
        Coord lowerCoord = AdjacencyDirection.LOWER.moveInDirection(location);

        Coord[] coordsToTry = new Coord[]{leftCoord, rightCoord, upperCoord, lowerCoord};

        for (Coord individualCoord : coordsToTry) {
            try {
                modifier.apply(this.getCellAt(individualCoord.y, individualCoord.x));
            } catch (IndexOutOfBoundsException e) {
                // ignore
            }
        }
    }

    /**
     * Applies given MathModifier to cells diagonal to the given Coord. Any indices/cells which are
     * out of bounds are simply ignored.
     *
     * @param location - the Coord of the cell for which we want to modify diagonal cells.
     * @param modifier - the modifier we want to apply to cells.
     */
    private void updateDiagonal(Coord location, CellVisitor modifier) {
        Coord upperLeftCoord = AdjacencyDirection.UPPER_LEFT.moveInDirection(location);
        Coord upperRightCoord = AdjacencyDirection.UPPER_RIGHT.moveInDirection(location);
        Coord lowerLeftCoord = AdjacencyDirection.LOWER_LEFT.moveInDirection(location);
        Coord lowerRightCoord = AdjacencyDirection.LOWER_RIGHT.moveInDirection(location);

        Coord[] coordsToTry = new Coord[]{upperLeftCoord, upperRightCoord, lowerLeftCoord, lowerRightCoord};

        for (Coord individualCoord : coordsToTry) {
            try {
                modifier.apply(this.getCellAt(individualCoord.y, individualCoord.x));
            } catch (IndexOutOfBoundsException e) {
                // ignore
            }
        }
    }

    /**
     * Applies a modifier which returns an integer to a cell that is a given number of steps in a direction from the given location.
     * Any out of bounds situations are simply ignored, 0 is returned.
     *
     * @param location      - the original coord we want to visit a number of steps from.
     * @param modifier      - The modifier we want to apply to the cell in the direction/number of steps given, must return an integer
     * @param direction     - the direction in which we want to move from the original coord
     * @param numIncrements - the number of steps in the given direction we want to take from the given coordinate.
     * @return - the result of applying the modifier to the cell, or 0 if the theoretical cell was out of bounds
     */
    private int visitInNumIncrements(Coord location, CellVisitor<Integer> modifier, AdjacencyDirection direction, int numIncrements) {
        Coord locationToCheck = location;

        // takes a step in direction for each given increment
        for (int i = 0; i < numIncrements; i++) {
            locationToCheck = direction.moveInDirection(locationToCheck);
        }

        try {
            return modifier.apply(this.getCellAt(locationToCheck.y, locationToCheck.x));
        } catch (IndexOutOfBoundsException e) {
            return 0;
        }
    }
}