package cs3500.pa03.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa03.controller.ProxyDealer;
import cs3500.pa03.controller.UserInputHandler;
import cs3500.pa03.json.FleetJson;
import cs3500.pa03.json.ShipAdapter;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.ShipDirection;
import cs3500.pa03.model.ShipPiece;
import cs3500.pa03.model.ShipType;
import cs3500.pa03.view.GameViewer;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * This is the main driver of this project.
 */
public class Driver {
  /**
   * Runs a game of battleship based on given argumnets. No arguments runs a human vs ai
   * game, two arguments runs a networked game (should be IP address and port no.), any
   * other amount is not accepted.
   *
   * @param args - no command line args required
   */
  public static void main(String[] args) {
    if (args.length == 0) {
      runClassicGame();
    } else if (args.length == 2) {
      runNetworkedGame(args);
    } else {
      System.err.println("Why would you enter " + args.length + " arguments??");
    }
  }

  /**
   * Runs a classic game of battleship with human vs ai. Viewer goes to system out and
   * input is read from system in.
   */
  private static void runClassicGame() {
    Appendable toWriteTo = new PrintStream(System.out);
    GameViewer viewerToUse = new GameViewer(toWriteTo);

    Readable toReadFrom = new InputStreamReader(System.in);
    UserInputHandler handlerToUse = new UserInputHandler(toReadFrom);

    Player player = new PlayerImpl(handlerToUse, viewerToUse);
    Player computer = new OpponentImpl(viewerToUse);

    BattleshipGame game = new BattleshipGame(player, computer, handlerToUse, viewerToUse);
    game.runGame();
  }

  /**
   * Runs a networked game of Battleship using given arguments which should contain first
   * an IP address and second a port number.
   *
   * @param args - command line args which should be Network information to run the game.
   */
  private static void runNetworkedGame(String[] args) {
    String host = args[0];
    int port = Integer.parseInt(args[1]);

    Appendable toWriteTo = new PrintStream(System.out);
    GameViewer viewerToUse = new GameViewer(toWriteTo);
    OpponentImpl player = new OpponentImpl(viewerToUse);

    try {
      Driver.runClient(host, port, player);
    } catch (IOException e) {
      System.err.println("We got an unexpected error! " + e);
    }
  }

  /**
   * This method connects to the server at the given host and port, builds a proxy referee
   * to handle communication with the server, and sets up a client player.
   *
   * @param host the server host
   * @param port the server port
   * @throws IOException if there is a communication issue with the server
   */
  private static void runClient(String host, int port, OpponentImpl p)
      throws IOException, IllegalStateException {
    Socket server = new Socket(host, port);
    ProxyDealer proxyDealer = new ProxyDealer(server, p);
    proxyDealer.run();
  }
}