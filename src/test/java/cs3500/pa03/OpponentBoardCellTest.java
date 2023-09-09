package cs3500.pa03;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.pa03.game.CellStatus;
import cs3500.pa03.game.OpponentBoardCell;
import cs3500.pa03.model.Coord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Tests the OpponentBoardCell and its methods
 */
public class OpponentBoardCellTest {
  OpponentBoardCell testCell = new OpponentBoardCell(new Coord(1, 1));

  /**
   * Initializes the example cell again because we are mutating it left and right
   */
  @BeforeEach
  public void initData() {
    testCell = new OpponentBoardCell(new Coord(1, 1));
  }

  /**
   * Tests the getStatus method
   */
  @Test
  public void testGetStatus() {
    assertEquals(CellStatus.EMPTY, testCell.getStatus());

    testCell.setStatus(CellStatus.HIT);
    assertEquals(CellStatus.HIT, testCell.getStatus());

    testCell.setStatus(CellStatus.MISS);
    assertEquals(CellStatus.MISS, testCell.getStatus());
  }

  /**
   * Tests the setStatus method
   */
  @Test
  public void testSetStatus() {
    testCell.setStatus(CellStatus.HIT);
    assertEquals(CellStatus.HIT, testCell.getStatus());

    testCell.setStatus(CellStatus.MISS);
    assertEquals(CellStatus.MISS, testCell.getStatus());

    testCell.setStatus(CellStatus.EMPTY);
    assertEquals(CellStatus.EMPTY, testCell.getStatus());
  }

  /**
   * Tests the getPriority method
   */
  @Test
  public void testGetPriority() {
    assertEquals(0, testCell.getPriority());

    testCell.modifyPriority(10);
    assertEquals(10, testCell.getPriority());

    testCell.modifyPriority(-100);
    assertEquals(0, testCell.getPriority());
  }

  /**
   * Tests the modifyPriority method
   */

  @Test
  public void testModifyPriority() {

    testCell.modifyPriority(10);
    assertEquals(10, testCell.getPriority());

    testCell.modifyPriority(15);
    assertEquals(25, testCell.getPriority());

    testCell.modifyPriority(-100);
    assertEquals(0, testCell.getPriority());

    testCell.modifyPriority(300);
    assertEquals(100, testCell.getPriority());
  }
}
