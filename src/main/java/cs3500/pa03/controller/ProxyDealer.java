package cs3500.pa03.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa03.game.GameResult;
import cs3500.pa03.game.OpponentImpl;
import cs3500.pa03.json.EmptyJson;
import cs3500.pa03.json.FleetJson;
import cs3500.pa03.json.JoinJson;
import cs3500.pa03.json.JsonUtils;
import cs3500.pa03.json.MessageJson;
import cs3500.pa03.json.ShipAdapter;
import cs3500.pa03.json.VolleyJson;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.ShipPiece;
import cs3500.pa03.model.ShipType;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class uses the Proxy Pattern to talk to the Server and dispatch methods to the Player.
 */
public class ProxyDealer {

  private final Socket server;
  private final InputStream in;
  private final PrintStream out;
  private final OpponentImpl player;
  private final ObjectMapper mapper = new ObjectMapper();

  private static final JsonNode VOID_RESPONSE =
      new ObjectMapper().getNodeFactory().textNode("void");

  /**
   * Construct an instance of a ProxyPlayer.
   *
   * @param server the socket connection to the server
   * @param player the instance of the player
   * @throws IOException if
   */
  public ProxyDealer(Socket server, OpponentImpl player) throws IOException {
    this.server = server;
    this.in = server.getInputStream();
    this.out = new PrintStream(server.getOutputStream());
    this.player = player;
  }

  /**
   * Listens for messages from the server as JSON in the format of a MessageJSON. When a complete
   * message is sent by the server, the message is parsed and then delegated to the corresponding
   * helper method for each message. This method stops when the connection to the server is closed
   * or an IOException is thrown from parsing malformed JSON.
   */
  public void run() {
    try {
      JsonParser parser = this.mapper.getFactory().createParser(this.in);

      while (!this.server.isClosed()) {
        MessageJson message = parser.readValueAs(MessageJson.class);
        delegateMessage(message);
      }
    } catch (IOException e) {
      throw new IllegalStateException(e.getMessage());
    }
  }


  /**
   * Determines the type of request the server has sent ("guess" or "win") and delegates to the
   * corresponding helper method with the message arguments.
   *
   * @param message the MessageJSON used to determine what the server has sent
   */
  private void delegateMessage(MessageJson message) throws IOException {
    String name = message.messageName();
    JsonNode arguments = message.arguments();

    if ("join".equals(name)) {
      handleJoin();
    } else if ("setup".equals(name)) {
      handleSetup(arguments);
    } else if ("take-shots".equals(name)) {
      handleTakeShots();
    } else if ("report-damage".equals(name)) {
      handleReportDamage(arguments);
    } else if ("successful-hits".equals(name)) {
      handleSuccessfulHits(arguments);
    } else if ("end-game".equals(name)) {
      handleEndGame(arguments);
    } else {
      throw new IllegalStateException("Invalid message name given from server");
    }
  }

  /**
   * Handles behaviors required when receiving a "join" message from server.
   */
  private void handleJoin() {
    JoinJson args = new JoinJson(player.name(), "SINGLE");
    JsonNode joinArg = mapper.convertValue(args, JsonNode.class);

    MessageJson join = new MessageJson("join", joinArg);
    JsonNode jsonResponse = JsonUtils.serializeRecord(join);

    this.out.println(jsonResponse);
  }

  /**
   * Handles behaviors required when receiving a "setup" message from server. Instructs
   * our A.I. to setup based on given specs from server.
   *
   * @param arguments - the arguments from the server regarding how the player should setup
   *                  their board for the game.
   */
  private void handleSetup(JsonNode arguments) {
    int heightForGame = arguments.get("height").intValue();
    int widthForGame = arguments.get("width").intValue();

    HashMap<ShipType, Integer> specsMap = new HashMap<>();

    specsMap.put(ShipType.CARRIER, arguments.get("fleet-spec").get("CARRIER").intValue());
    specsMap.put(ShipType.BATTLESHIP, arguments.get("fleet-spec").get("BATTLESHIP").intValue());
    specsMap.put(ShipType.DESTROYER, arguments.get("fleet-spec").get("DESTROYER").intValue());
    specsMap.put(ShipType.SUBMARINE, arguments.get("fleet-spec").get("SUBMARINE").intValue());

    ArrayList<ShipPiece> setupOutput = player.setup(heightForGame, widthForGame, specsMap);

    ShipAdapter[] forJson = new ShipAdapter[setupOutput.size()];

    for (int i = 0; i < forJson.length; i++) {
      forJson[i] = setupOutput.get(i).toJson();
    }

    FleetJson fleet = new FleetJson(forJson);
    JsonNode fleetAsNode = mapper.convertValue(fleet, JsonNode.class);

    MessageJson response = new MessageJson("setup", fleetAsNode);
    JsonNode responseAsNode = JsonUtils.serializeRecord(response);

    this.out.println(responseAsNode);
  }

  /**
   * Handles behaviors required when receiving instructions from the server to takeShots.
   */
  private void handleTakeShots() {
    List<Coord> playerShots = player.takeShots();

    VolleyJson shotsVolley = new VolleyJson(playerShots);
    JsonNode shotsAsNode = mapper.convertValue(shotsVolley, JsonNode.class);

    MessageJson response = new MessageJson("take-shots", shotsAsNode);
    JsonNode responseAsNode = JsonUtils.serializeRecord(response);

    this.out.println(responseAsNode);
  }

  /**
   * Handles behaviors required when receiving instructions from the server to reportDamage based
   * on the list of shots given from the server that the other player fired at us.
   *
   * @param arguments - Arguments from the server, should contain a volley of shots that the other
   *                  player fired at our board.
   */
  private void handleReportDamage(JsonNode arguments) {
    VolleyJson volley = mapper.convertValue(arguments, VolleyJson.class);
    List<Coord> oppShots = volley.getVolley();

    List<Coord> oppSuccessful = player.reportDamage(oppShots);

    VolleyJson oppSuccessfulJson = new VolleyJson(oppSuccessful);
    JsonNode oppSuccessfulAsNode = mapper.convertValue(oppSuccessfulJson, JsonNode.class);

    MessageJson response = new MessageJson("report-damage", oppSuccessfulAsNode);
    JsonNode responseAsNode = JsonUtils.serializeRecord(response);

    this.out.println(responseAsNode);
  }

  /**
   * Handles behaviors associated with the successfulHits method. Reports given volley from
   * the server (representing successful hits) to our opponent implementation to update
   * the DecisionMaker as appropriate.
   *
   * @param arguments - JSON argument from server that should contain a volley of successful shots.
   */
  private void handleSuccessfulHits(JsonNode arguments) {
    VolleyJson volley = mapper.convertValue(arguments, VolleyJson.class);
    List<Coord> successfulShots = volley.getVolley();
    player.successfulHits(successfulShots);

    MessageJson response = new MessageJson("successful-hits",
        mapper.convertValue(new EmptyJson(), JsonNode.class));
    JsonNode responseAsNode = JsonUtils.serializeRecord(response);

    this.out.println(responseAsNode);
  }

  /**
   * Handles behaviors associated with the endGame method. Server should provide a type of game
   * end and a reason as to why the game was ended.
   *
   * @param arguments - arguments from the server as to why the game ended for the reason it did
   *                  and what type of "gameResult" it was.
   */
  private void handleEndGame(JsonNode arguments) {
    String result = arguments.get("result").toString();
    String game = arguments.get("reason").toString();
    System.out.println("Game ended with result: " + result + " and reason: " + game);

    if (result.equals(GameResult.WIN.toString())) {
      player.endGame(GameResult.WIN, arguments.get("reason").toString());
      try {
        server.close();
      } catch (IOException e) {
        System.out.println("Error closing server");
      }
    } else if (result.equals(GameResult.LOSE.toString())) {
      player.endGame(GameResult.LOSE, arguments.get("reason").toString());
      try {
        server.close();
      } catch (IOException e) {
        System.out.println("Error closing server");
      }
    } else {
      player.endGame(GameResult.TIE, arguments.get("reason").toString());
      try {
        server.close();
      } catch (IOException e) {
        System.out.println("Error closing server");
      }
    }
  }
}
