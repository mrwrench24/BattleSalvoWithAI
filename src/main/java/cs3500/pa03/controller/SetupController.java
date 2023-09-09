package cs3500.pa03.controller;

import cs3500.pa03.game.Player;
import cs3500.pa03.model.ShipType;
import cs3500.pa03.view.GameViewer;
import java.util.HashMap;

/**
 * Represents a controller used in setting up the game of Battleship (asking for board size,
 * fleet size, etc).
 */
public class SetupController {
  Player player1;
  Player player2;

  UserInputHandler handler;
  GameViewer viewer;

  final int minDimension = 6;
  final int maxDimension = 15;

  /**
   * Builds a new SetupController that prompts input from the default UIH and ultimately calls
   * setup using validated input on the given Players.
   *
   * @param player1 - The player to have their board setup by this controller's prompted input
   * @param player2 - The player to have their board setup by this controller's prompted input
   * @param viewer - The viewer to display messages to
   * @param handler - The UIH to prompt input from
   */
  public SetupController(Player player1, Player player2,
                         UserInputHandler handler, GameViewer viewer) {
    this.player1 = player1;
    this.player2 = player2;
    this.handler = handler;
    this.viewer = viewer;
  }

  /**
   * Runs setup for a game of Battleship. Prompts UIH for board length, board width, then repeatedly
   * prompts shipTypes one by one and starts fleet building over if too many ships are entered.
   * Using given input, setup is then called on both players.
   */
  public void runSession() {
    viewer.display("Welcome to BattleSalvo. Let's get setup.");

    int chosenHeight = this.getUntil("Board Length", minDimension, maxDimension);
    int chosenWidth = this.getUntil("Board Width", minDimension, maxDimension);

    HashMap<ShipType, Integer> fleetSpecs = this.promptFleet(Math.min(chosenWidth, chosenHeight));

    player1.setup(chosenHeight, chosenWidth, fleetSpecs);
    player2.setup(chosenHeight, chosenWidth, fleetSpecs);
  }


  /**
   * Prompts the user to build a fleet up to given size.
   *
   * @param maxSize - the max number of ships the fleet may contain.
   */
  private HashMap<ShipType, Integer> promptFleet(int maxSize) {
    while (true) {
      HashMap<ShipType, Integer> fleet = new HashMap<>();
      viewer.display("Your fleet may have up to " + maxSize + " ships.");
      int totalShipsEntered = 0;

      for (ShipType ship : ShipType.values()) {
        int numOfType = this.getUntil(ship.toString(), 1, maxSize);
        fleet.put(ship, numOfType);
        totalShipsEntered += numOfType;
      }

      if (totalShipsEntered <= maxSize) {
        // only way it exits the loop!
        return fleet;
      } else {
        viewer.display(totalShipsEntered + " exceeds " + maxSize);
      }
    }
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
