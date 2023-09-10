package cs3500.pa03.game;

import cs3500.pa03.model.Coord;

/**
 * Represents a direction relative to a specific cell on a board. Each AdjacencyDirection has a number of
 * x positions and y positions that a move in that direction would be associated with.
 */
public enum AdjacencyDirection {
    LEFT(-1, 0),
    RIGHT(1, 0),
    UPPER(0, -1),
    LOWER(0, 1),
    UPPER_LEFT(-1, -1),
    UPPER_RIGHT(1, -1),
    LOWER_LEFT(-1, 1),
    LOWER_RIGHT(1, 1);

    private int xModification;
    private int yModification;

    /**
     * An AdjacencyDirection with the determined number of x-coordinates and y-coordinates that a move in the
     * direction is associated with.
     *
     * @param xMove - the number of x-coordinates a move in the direction has.
     * @param yMove - the number of y-coordinates a move in the direction has
     */
    AdjacencyDirection(int xMove, int yMove) {
        this.xModification = xMove;
        this.yModification = yMove;
    }

    /**
     * Moves the given Coord one step in the direction of this AdjacencyDirection. For example, if called on
     * direction "left", and the given coord is (2, 2), returns coord of (1, 2), which is one step to the left.
     *
     * @param original - the original coord which we want to move one step in a certain direction
     * @return - the given coord pushed one step in the given direction
     */
    public Coord moveInDirection(Coord original) {
        int xToUse = original.x + this.xModification;
        int yToUse = original.y + this.yModification;

        return new Coord(xToUse, yToUse);
    }
}
