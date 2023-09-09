package cs3500.pa03;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.pa03.game.CellStatus;
import cs3500.pa03.game.OpponentBoardCell;
import cs3500.pa03.game.OpponentBoardTracker;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.ShipType;
import cs3500.pa03.random.MockRandomFour;
import cs3500.pa03.random.Randomable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the opponent board tracker
 */
public class OpponentBoardTrackerTest {
  OpponentBoardTracker testEightByEight;
  OpponentBoardTracker testTwoByThree;

  Randomable mockForFour = new MockRandomFour();

  /**
   * Resets the boards after each test.
   */
  @BeforeEach
  public void initData() {
    mockForFour = new MockRandomFour();
    testEightByEight = new OpponentBoardTracker(8, 8, mockForFour);
    testTwoByThree = new OpponentBoardTracker(2, 3, mockForFour);
  }

  /**
   * Tests the modify method
   */
  @Test
  public void testModify() {
    OpponentBoardCell testCell = testEightByEight.getCellAt(0, 0);
    assertEquals(CellStatus.EMPTY, testCell.getStatus());

    testEightByEight.modify(new Coord(0, 0), CellStatus.HIT);
    assertEquals(CellStatus.HIT, testCell.getStatus());

    testEightByEight.modify(new Coord(0, 0), CellStatus.MISS);
    assertEquals(CellStatus.MISS, testCell.getStatus());
  }

  /**
   * Tests the initial coverage, with cells closer to the middle being given a higher
   * priority value
   */
  @Test
  public void testInitialCoverage() {
    assertEquals(0, testEightByEight.getCellAt(0, 0).getPriority());
    assertEquals(0, testEightByEight.getCellAt(7, 7).getPriority());

    assertEquals(10, testEightByEight.getCellAt(0, 4).getPriority());
    assertEquals(10, testEightByEight.getCellAt(7, 4).getPriority());

    assertEquals(10, testEightByEight.getCellAt(3, 7).getPriority());
    assertEquals(10, testEightByEight.getCellAt(3, 0).getPriority());

    assertEquals(15, testEightByEight.getCellAt(3, 3).getPriority());
    assertEquals(15, testEightByEight.getCellAt(4, 4).getPriority());
  }

  /**
   * Tests the notifyHit method
   */
  @Test
  public void testNotifyHit() {
    testEightByEight.notifyHitAround(new Coord(3, 3));

    // randomly chosen directions to prioritize/not prioritize
    assertEquals(95, testEightByEight.getCellAt(3, 2).getPriority());
    assertEquals(80, testEightByEight.getCellAt(3, 1).getPriority());
    assertEquals(65, testEightByEight.getCellAt(3, 0).getPriority());

    assertEquals(100, testEightByEight.getCellAt(3, 4).getPriority());
    assertEquals(85, testEightByEight.getCellAt(3, 5).getPriority());
    assertEquals(65, testEightByEight.getCellAt(3, 6).getPriority());

    assertEquals(55, testEightByEight.getCellAt(2, 3).getPriority());
    assertEquals(40, testEightByEight.getCellAt(1, 3).getPriority());
    assertEquals(25, testEightByEight.getCellAt(0, 3).getPriority());

    assertEquals(60, testEightByEight.getCellAt(4, 3).getPriority());
    assertEquals(45, testEightByEight.getCellAt(5, 3).getPriority());
    assertEquals(25, testEightByEight.getCellAt(6, 3).getPriority());
  }

  /**
   * Tests teh notify hit method when it needs to ignore certain directions
   * because of a previous miss.
   */
  @Test
  public void testNotifyIgnoreDirections() {
    /*
        M
      M H M
        M
     */

    testEightByEight.getCellAt(3, 3).setStatus(CellStatus.MISS);
    testEightByEight.getCellAt(4, 2).setStatus(CellStatus.MISS);
    testEightByEight.getCellAt(4, 4).setStatus(CellStatus.MISS);
    testEightByEight.getCellAt(5, 3).setStatus(CellStatus.MISS);

    // based on the boundaries...
    assertEquals(15, testEightByEight.getCellAt(3, 3).getPriority());
    assertEquals(10, testEightByEight.getCellAt(4, 2).getPriority());
    assertEquals(15, testEightByEight.getCellAt(4, 4).getPriority());
    assertEquals(15, testEightByEight.getCellAt(5, 3).getPriority());

    testEightByEight.notifyHitAround(new Coord(3, 4));

    // should be unchanged
    assertEquals(15, testEightByEight.getCellAt(3, 3).getPriority());
    assertEquals(10, testEightByEight.getCellAt(4, 2).getPriority());
    assertEquals(15, testEightByEight.getCellAt(4, 4).getPriority());
    assertEquals(15, testEightByEight.getCellAt(5, 3).getPriority());

    // cells in the "continuing" directions shouldn't have higher values either
    assertEquals(10, testEightByEight.getCellAt(2, 3).getPriority());
    assertEquals(10, testEightByEight.getCellAt(4, 1).getPriority());
    assertEquals(15, testEightByEight.getCellAt(4, 5).getPriority());
    assertEquals(10, testEightByEight.getCellAt(6, 3).getPriority());
  }

  /**
   * Tests the toString method
   */
  @Test
  public void testToString() {
    assertEquals("Opponent's Board: \neee\neee\n", testTwoByThree.toString(true));
    assertEquals("Opponent's Board: \n0 (e) 0 (e) 0 (e) \n10 (e) 10 (e) 10 (e) \n",
        testTwoByThree.toString(false));

    testTwoByThree.getCellAt(0, 0).setStatus(CellStatus.MISS);
    testTwoByThree.getCellAt(1, 1).setStatus(CellStatus.HIT);
    testTwoByThree.getCellAt(0, 2).modifyPriority(24);

    assertEquals("Opponent's Board: \nMee\neHe\n", testTwoByThree.toString(true));
    assertEquals("Opponent's Board: \n0 (M) 0 (e) 24 (e) \n10 (e) 10 (H) 10 (e) \n",
        testTwoByThree.toString(false));
  }

  /**
   * Ensures that the DecisionMaker is reasonable in determining which directions should be
   * prioritized randomly to shoot at first.
   */
  @Test
  public void testDeterminePriorityVertical() {
    /*      H
    E E M M X M E E
            E
     */

    testEightByEight.modify(new Coord(4, 0), CellStatus.HIT);

    testEightByEight.modify(new Coord(2, 1), CellStatus.MISS);
    testEightByEight.modify(new Coord(3, 1), CellStatus.MISS);
    testEightByEight.modify(new Coord(5, 1), CellStatus.MISS);

    assertEquals(0, testEightByEight.getCellAt(1, 2).getPriority());
    assertEquals(10, testEightByEight.getCellAt(1, 3).getPriority());
    assertEquals(10, testEightByEight.getCellAt(1, 5).getPriority());

    // the cell that should end up being prioritized
    assertEquals(10, testEightByEight.getCellAt(2, 4).getPriority());

    testEightByEight.notifyHitAround(new Coord(4, 1));

    assertEquals(0, testEightByEight.getCellAt(1, 2).getPriority());
    assertEquals(10, testEightByEight.getCellAt(1, 3).getPriority());
    assertEquals(10, testEightByEight.getCellAt(1, 5).getPriority());

    // the cell that should end up being prioritized
    assertEquals(95, testEightByEight.getCellAt(2, 4).getPriority());
  }

  /**
   * Ensures that the DecisionMaker is reasonable in determining which directions should be
   * prioritized randomly to shoot at first.
   */
  @Test
  public void testDeterminePriorityHorizontal() {
    /* M
     E X H H E E
       M
       E
     */

    testEightByEight.modify(new Coord(2, 1), CellStatus.HIT);
    testEightByEight.modify(new Coord(3, 1), CellStatus.HIT);

    testEightByEight.modify(new Coord(1, 0), CellStatus.MISS);
    testEightByEight.modify(new Coord(1, 2), CellStatus.MISS);

    assertEquals(0, testEightByEight.getCellAt(0, 1).getPriority());
    assertEquals(0, testEightByEight.getCellAt(2, 1).getPriority());
    assertEquals(10, testEightByEight.getCellAt(3, 1).getPriority());

    // cells that should be prioritized
    assertEquals(0, testEightByEight.getCellAt(1, 0).getPriority());

    testEightByEight.notifyHitAround(new Coord(1, 1));

    assertEquals(0, testEightByEight.getCellAt(0, 1).getPriority());
    assertEquals(0, testEightByEight.getCellAt(2, 1).getPriority());
    assertEquals(10, testEightByEight.getCellAt(3, 1).getPriority());

    // cells that should be prioritized
    assertEquals(85, testEightByEight.getCellAt(1, 0).getPriority());
  }

  /**
   * Tests the notifyMissAround method
   */
  @Test
  public void testNotifyMissAround() {
    /*
     E E E E
     E H E E
     E E E E
     */

    testEightByEight.notifyHitAround(new Coord(1, 1));
    assertEquals(85, testEightByEight.getCellAt(1, 2).getPriority());
    assertEquals(45, testEightByEight.getCellAt(0, 1).getPriority());
    assertEquals(45, testEightByEight.getCellAt(2, 1).getPriority());

    testEightByEight.notifyMissAround(new Coord(2, 1));
    assertEquals(63, testEightByEight.getCellAt(0, 1).getPriority());
    assertEquals(63, testEightByEight.getCellAt(2, 1).getPriority());
    assertEquals(28, testEightByEight.getCellAt(0, 3).getPriority());
    assertEquals(28, testEightByEight.getCellAt(2, 3).getPriority());
  }

  /**
   * Tests the getMostImportant (takeShots) method, seeing whether it takes reasonable shots
   */
  @Test
  public void testGetMostImportant() {
    // note how the shots are close/clustered to the center but also spread out...
    assertEquals("[(3, 3), (4, 3), (5, 4)]",
        testEightByEight.getMostImportant(3).toString());

    testEightByEight.getCellAt(0, 0).modifyPriority(100);

    assertEquals("[(0, 0)]", testEightByEight.getMostImportant(1).toString());

    testEightByEight.notifyHitAround(new Coord(3, 3));

    // hits 0 0 b/c of the priority i gave it, but note how it is clustered around the hit in one
    // direction specifically
    assertEquals("[(0, 0), (1, 3), (2, 3), (4, 3)]",
        testEightByEight.getMostImportant(4).toString());

    testEightByEight.notifyMissAround(new Coord(2, 3));
    testEightByEight.notifyMissAround(new Coord(4, 3));
    testEightByEight.notifyMissAround(new Coord(5, 3));

    testEightByEight.modify(new Coord(2, 3), CellStatus.MISS);
    testEightByEight.modify(new Coord(4, 3), CellStatus.MISS);
    testEightByEight.modify(new Coord(5, 3), CellStatus.MISS);

    // and then it goes more in the other direction
    assertEquals("[(3, 4), (6, 4), (3, 1), (0, 0), (0, 3)]",
        testEightByEight.getMostImportant(5).toString());
  }

}
