package cs3500.pa03.controller;

import cs3500.pa03.model.Coord;
import cs3500.pa03.view.GameViewer;
import java.util.ArrayList;
import java.util.List;

/**
 * Controls the Salvo process of prompting a HUMAN player for the shots they need to make using
 * input.
 */
public class SalvoController {
  private int playerBoardWidth;
  private int playerBoardHeight;
  private UserInputHandler handler;
  private GameViewer viewer;

  /**
   * Builds a new SalvoContorller restricted by given width and height from the player.
   *
   * @param boardWidth  - the width of the board we are choosing shots for
   * @param boardHeight - the height of the baord we are chooingns shots for
   * @param handler     - the UIH to get input from
   * @param viewer      - the viewer to display prompts to
   */
  public SalvoController(int boardWidth, int boardHeight, UserInputHandler handler,
                         GameViewer viewer) {
    this.playerBoardWidth = boardWidth;
    this.playerBoardHeight = boardHeight;
    this.handler = handler;
    this.viewer = viewer;
  }

  /**
   * Runs this session, getting a list of coordinates from the user from the UIH to build
   * coords from.
   *
   * @param numShots - the number of shots we need to prompt the user for
   * @return - the shots input by the user
   */
  public List<Coord> runSession(int numShots) {
    ArrayList<Coord> result = new ArrayList<>();
    viewer.display("Enter " + numShots + " shots.\n");

    for (int shotNum = 1; shotNum <= numShots; shotNum++) {
      int xtouse = this.getUntil("Shot " + shotNum + " x-coordinate", 0, playerBoardWidth - 1);
      int ytouse = this.getUntil("Shot " + shotNum + " y-coordinate", 0, playerBoardHeight - 1);
      result.add(new Coord(xtouse, ytouse));
    }

    return result;
  }

  /**
   * Repeatedly prompts for input until receiving an integer between given low and high, inclusvie
   *
   * @param purpose - the reason we need input, shown to user
   * @param low     - the lowest the input can be
   * @param high    - the highest the input can be
   * @return - the first valid input from the user
   */
  private int getUntil(String purpose, int low, int high) {
    while (true) {
      try {
        viewer.promptInputFor(purpose, low, high);
        return handler.nextInput(low, high);
      } catch (IllegalArgumentException e) {
        viewer.scold(low, high);
      }
    }
  }
}
