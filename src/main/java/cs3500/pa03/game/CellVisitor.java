package cs3500.pa03.game;

/**
 * An interface for modifying an OpponentBoardCell, based on specific implementations
 */
public interface CellVisitor<T> {

    /**
     * Applies a mathematical operation to the given value, returning the new value it now has after the changes.
     *
     * @param toModify - the cell we want to modify
     * @return - the modified number
     */
     public T apply(OpponentBoardCell toModify);
}
