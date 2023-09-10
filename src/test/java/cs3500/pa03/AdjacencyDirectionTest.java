package cs3500.pa03;

import cs3500.pa03.game.AdjacencyDirection;
import org.junit.jupiter.api.Test;
import cs3500.pa03.model.Coord;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the AdjacencyDirection enum, mainly the moveInDirection method.
 */
public class AdjacencyDirectionTest {

    /**
     * Tests the moveInDirection method
     */
    @Test
    public void testMoveInDirection() {
        Coord oneOne = new Coord(1, 1);

        assertEquals(new Coord(0, 1), AdjacencyDirection.LEFT.moveInDirection(oneOne));
        assertEquals(new Coord(2, 1), AdjacencyDirection.RIGHT.moveInDirection(oneOne));
        assertEquals(new Coord(1, 0), AdjacencyDirection.UPPER.moveInDirection(oneOne));
        assertEquals(new Coord(1, 2), AdjacencyDirection.LOWER.moveInDirection(oneOne));

        assertEquals(new Coord(0, 0), AdjacencyDirection.UPPER_LEFT.moveInDirection(oneOne));
        assertEquals(new Coord(2, 0), AdjacencyDirection.UPPER_RIGHT.moveInDirection(oneOne));
        assertEquals(new Coord(0, 2), AdjacencyDirection.LOWER_LEFT.moveInDirection(oneOne));
        assertEquals(new Coord(2, 2), AdjacencyDirection.LOWER_RIGHT.moveInDirection(oneOne));
    }
}
