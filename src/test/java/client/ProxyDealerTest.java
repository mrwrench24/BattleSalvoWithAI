package client;

import static com.fasterxml.jackson.databind.type.LogicalType.Array;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa03.controller.ProxyDealer;
import cs3500.pa03.game.OpponentImpl;
import cs3500.pa03.json.FleetJson;
import cs3500.pa03.json.JoinJson;
import cs3500.pa03.json.JsonUtils;
import cs3500.pa03.json.MessageJson;
import cs3500.pa03.json.ReportDamageJson;
import cs3500.pa03.json.SetupJson;
import cs3500.pa03.json.ShipAdapter;
import cs3500.pa03.json.TakeShotsJson;
import cs3500.pa03.json.VolleyJson;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.ShipPiece;
import cs3500.pa03.model.ShipType;
import cs3500.pa03.random.RandomDecorator;
import cs3500.pa03.random.Randomable;
import cs3500.pa03.view.GameViewer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Test correct responses for different requests from the socket using a Mock Socket (mocket)
 */
public class ProxyDealerTest {
  private ByteArrayOutputStream testLog;
  private ProxyDealer dealer;

  /**
   * Reset the test log before each test is run.
   */
  @BeforeEach
  public void setup() {
    this.testLog = new ByteArrayOutputStream(2048);
    assertEquals("", logToString());
  }

  /**
   * Tests the joining process
   */
  @Test
  public void testJoin() {
    // Prepare sample message
    JoinJson winJson = new JoinJson("kaamil2", "SINGLE");
    JsonNode sampleMessage = createSampleMessage("join", winJson);
    EndGameJson endGameJson = new EndGameJson("LOSE",
        "Your player did not return a valid volley");
    JsonNode sampleMessage2 = createSampleMessage("end-game", endGameJson);
    //List<String> servermessage = Arrays.asList("{\"method-name\":\"join\",\"arguments\":{}}");
    List<String> serverMessage = Arrays.asList(sampleMessage.toString(), sampleMessage2.toString());

    // Create the client with all necessary messages
    Mocket socket = new Mocket(this.testLog, serverMessage);

    Appendable appendable = new StringBuilder();
    GameViewer view = new GameViewer(appendable);

    // Create a Dealer
    try {
      this.dealer = new ProxyDealer(socket, new OpponentImpl(view));
      this.dealer.run();
    } catch (IOException e) {
      fail();
    }

    String expected = "{\"method-name\":\"join\",\"arguments\""
        + ":{\"name\":\"pa04-ilovemakingtemplates\",\"game-type\":\"SINGLE\"}}\n";


    assertEquals(expected, logToString());
  }

  /**
   * Tests the ability to setup accurately
   */
  @Test
  public void testSetup() {
    // Prepare sample message


    SetupMock setupMock = new SetupMock(10, 12, new HashMap<ShipType, Integer>() {
      {
        put(ShipType.BATTLESHIP, 1);
        put(ShipType.DESTROYER, 1);
        put(ShipType.SUBMARINE, 1);
        put(ShipType.CARRIER, 2);
      }
    });

    JsonNode setupSample = createSampleMessage("setup", setupMock);

    EndGameJson endGameJson = new EndGameJson("LOSE",
        "Your player did not return a valid volley");
    JsonNode sampleMessage2 = createSampleMessage("end-game", endGameJson);
    //List<String> servermessage = Arrays.asList("{\"method-name\":\"join\",\"arguments\":{}}");
    List<String> serverMessage = Arrays.asList(setupSample.toString(),
        sampleMessage2.toString());

    List<String> servermessage = Arrays.asList("{\"method-name\":\"setup\",\"arguments\":"
            + "{\"height\":10,\"width\":12,\"fleet-spec\":{"
            + "\"BATTLESHIP\":1,\"DESTROYER\":1,\"SUBMARINE\":1,\"CARRIER\":2}}}\n",
        "{\"method-name\":\"end-game\",\"arguments\":{\"result\":\"LOSE\", "
            + "\"reason\" : \"Your player did not return a valid volley\"}}\n");

    Mocket socket = new Mocket(this.testLog, serverMessage);

    Appendable appendable = new StringBuilder();
    GameViewer view = new GameViewer(appendable);
    Randomable rand = new RandomDecorator(22);
    OpponentImpl opp = new OpponentImpl(view, rand);

    // Create a Dealer
    try {
      this.dealer = new ProxyDealer(socket, new OpponentImpl(view, rand));
      this.dealer.run();
    } catch (IOException e) {
      fail();
    }

    // run the dealer and verify the response
    String expected =
        "{\"method-name\":\"setup\",\"arguments\":{\"fleet\":[{\"coord\":{\"x\":1,\"y\":1},"
            + "\"length\":3,\"direction\":\"HORIZONTAL\"},{\"coord\":{\"x\":5,\"y\":1},\"length\""
            + ":5,\"direction\":\"HORIZONTAL\"},{\"coord\":{\"x\":1,\"y\":5},\"length\":6,"
            + "\"direction\":\"VERTICAL\"},{\"coord\":{\"x\":3,\"y\":5},\"length\":4,\"direction\""
            + ":\"HORIZONTAL\"},{\"coord\":{\"x\":7,\"y\":6},\"length\":6,\"direction\":\""
            + "VERTICAL\"}]}}\n";

    assertEquals(expected, logToString());
  }

  /**
   * Tests the ability to takeshots with a volley
   */
  @Test
  public void testVolley() {
    JoinJson winJson = new JoinJson("kaamil2", "SINGLE");
    JsonNode sampleMessage = createSampleMessage("join", winJson);
    SetupMock setupMock = new SetupMock(10, 12, new HashMap<ShipType, Integer>() {
      {
        put(ShipType.BATTLESHIP, 1);
        put(ShipType.DESTROYER, 1);
        put(ShipType.SUBMARINE, 1);
        put(ShipType.CARRIER, 2);
      }
    });

    JsonNode setupSample = createSampleMessage("setup", setupMock);

    TakeShotsJson volley = new TakeShotsJson(new Coord[] {
        new Coord(1, 0),
        new Coord(7, 1),
        new Coord(3, 2),
        new Coord(2, 6),
        new Coord(1, 9)
    });

    JsonNode volleySample = createSampleMessage("take-shots", volley);

    EndGameJson endGameJson = new EndGameJson("LOSE",
        "Your player did not return a valid volley");
    JsonNode sampleMessage2 = createSampleMessage("end-game", endGameJson);
    //List<String> servermessage = Arrays.asList("{\"method-name\":\"join\",\"arguments\":{}}");
    List<String> serverMessage = Arrays.asList(sampleMessage.toString(), setupSample.toString(),
        volleySample.toString(),
        sampleMessage2.toString());

    Mocket socket = new Mocket(this.testLog, serverMessage);

    Appendable appendable = new StringBuilder();
    GameViewer view = new GameViewer(appendable);
    Randomable rand = new RandomDecorator(22);
    OpponentImpl opp = new OpponentImpl(view, rand);

    // Create a Dealer
    try {
      this.dealer = new ProxyDealer(socket, new OpponentImpl(view, rand));
      this.dealer.run();
    } catch (IOException e) {
      fail();
    }

    String expected =
        "{\"method-name\":\"join\",\"arguments\":{\"name\":\"pa04-ilovemakingtemplates\","
            + "\"game-type\":\"SINGLE\"}}\n"
            + "{\"method-name\":\"setup\",\"arguments\":{\"fleet\":[{\"coord\":{\"x\":1,\"y\":1},"
            + "\"length\":3,\"direction\":\"HORIZONTAL\"},{\"coord\":{\"x\":5,\"y\":1"
            + "},\"length\":5,\"direction\":\"HORIZONTAL\"},"
            + "{\"coord\":{\"x\":1,\"y\":5},\"length\":6,\"direction\":\"VERTICAL\"},{\"co"
            + "ord\":{\"x\":3,\"y\":5},\"length\":4,\"direction\":\"HORIZONTAL\"}"
            + ",{\"coord\":{\"x\":7,\"y\":6},\"length\":6,\"direction\":\"VERTICAL\"}]}}\n"
            + "{\"method-name\":\"take-shots\",\"arguments\":{\"coordinates\":[{\"x\":4,\"y\":5},"
            + "{\"x\":5,\"y\":5},{\"x\":6,\"y\":5},{\"x\":4,\"y\":7},{\"x\":5,\"y\":7}]}}\n";

    assertEquals(expected, logToString());
  }

  /**
   * Tests the ability to report damage
   */
  @Test
  public void testReportDamage() {
    JoinJson winJson = new JoinJson("kaamil2", "SINGLE");
    JsonNode sampleMessage = createSampleMessage("join", winJson);
    SetupMock setupMock = new SetupMock(10, 12, new HashMap<ShipType, Integer>() {
      {
        put(ShipType.BATTLESHIP, 1);
        put(ShipType.DESTROYER, 1);
        put(ShipType.SUBMARINE, 1);
        put(ShipType.CARRIER, 2);
      }
    });

    JsonNode setupSample = createSampleMessage("setup", setupMock);

    ReportDamageJson volley = new ReportDamageJson(new Coord[] {
        new Coord(1, 0),
        new Coord(7, 1),
        new Coord(3, 2),
        new Coord(2, 6),
        new Coord(1, 9)
    });

    JsonNode volleySample = createSampleMessage("report-damage", volley);

    EndGameJson endGameJson = new EndGameJson("LOSE",
        "Your player did not return a valid volley");
    JsonNode sampleMessage2 = createSampleMessage("end-game", endGameJson);
    //List<String> servermessage = Arrays.asList("{\"method-name\":\"join\",\"arguments\":{}}");
    List<String> serverMessage = Arrays.asList(sampleMessage.toString(), setupSample.toString(),
        volleySample.toString(),
        sampleMessage2.toString());

    Mocket socket = new Mocket(this.testLog, serverMessage);

    Appendable appendable = new StringBuilder();
    GameViewer view = new GameViewer(appendable);
    Randomable rand = new RandomDecorator(22);
    OpponentImpl opp = new OpponentImpl(view, rand);

    // Create a Dealer
    try {
      this.dealer = new ProxyDealer(socket, new OpponentImpl(view, rand));
      this.dealer.run();
    } catch (IOException e) {
      fail();
    }

    String expected =
        "{\"method-name\":\"join\",\"arguments\":{\"name\":\"pa04-ilovemakingtemplates\","
            + "\"game-type\":\"SINGLE\"}}\n{\"method-name\":\"setup\",\"arguments\":{\"fleet\":"
            + "[{\"coord\":{\"x\":1,\"y\":1},\"length\":3,\"direction\":\"HORIZONTAL\"},"
            + "{\"coord\":{\"x\":5,\"y\":1},\"length\":5,\"direction\":\"HORIZONTAL\"},"
            + "{\"coord\":{\"x\":1,\"y\":5},\"length\":6,\"direction\":\"VER"
            + "TICAL\"},{\"coord\":{\"x\":3,\"y\":5},\"length\":4,\""
            + "direction\":\"HORIZONTAL\"},{\"coord\":{\"x\":7,\"y\":6},\"length\":6,\"direction\":"
            + "\"VERTICAL\"}]}}\n{\"method-name\":\"report-damage\","
            + "\"arguments\":{\"coordinates\":[{\"x\":7,\"y\":1},{\"x\":1,\"y\":9}]}}\n";

    assertEquals(expected, logToString());
  }

  /**
   * Tests the ability to do successfulHits cross-checking
   */
  @Test
  public void testSuccesfulHits() {

    JoinJson winJson = new JoinJson("kaamil2", "SINGLE");
    JsonNode sampleMessage = createSampleMessage("join", winJson);
    SetupMock setupMock = new SetupMock(10, 12, new HashMap<ShipType, Integer>() {
      {
        put(ShipType.BATTLESHIP, 1);
        put(ShipType.DESTROYER, 1);
        put(ShipType.SUBMARINE, 1);
        put(ShipType.CARRIER, 2);
      }
    });

    JsonNode setupSample = createSampleMessage("setup", setupMock);

    VolleyJson volley = new VolleyJson(new ArrayList<Coord>() {
      {
        add(new Coord(1, 0));
        add(new Coord(7, 1));
        add(new Coord(2, 11));
        add(new Coord(4, 11));
        add(new Coord(5, 2));
      }
    });

    JsonNode volleySample = createSampleMessage("successful-hits", volley);

    EndGameJson endGameJson = new EndGameJson("WIN",
        "Your player did not return a valid volley");
    JsonNode sampleMessage2 = createSampleMessage("end-game", endGameJson);
    //List<String> servermessage = Arrays.asList("{\"method-name\":\"join\",\"arguments\":{}}");
    List<String> serverMessage = Arrays.asList(sampleMessage.toString(), setupSample.toString(),
        volleySample.toString(),
        sampleMessage2.toString());

    Mocket socket = new Mocket(this.testLog, serverMessage);

    Appendable appendable = new StringBuilder();
    GameViewer view = new GameViewer(appendable);
    Randomable rand = new RandomDecorator(22);
    OpponentImpl opp = new OpponentImpl(view, rand);


    // Create a Dealer
    try {
      this.dealer = new ProxyDealer(socket, new OpponentImpl(view, rand));
      this.dealer.run();
    } catch (IOException e) {
      fail();
    }

    String expected =
        "{\"method-name\":\"join\",\"arguments\":{\"name\":\"pa04-ilovemakingtemplates\",\""
            + "game-type\":\"SINGLE\"}}\n"
            + "{\"method-name\":\"setup\",\"arguments\":{\"fleet\":[{"
            + "\"coord\":{\"x\":1,\"y\":1},\"l"
            + "ength\":3,\"direction\":\"HORIZONTAL\"},{\"coord\":{\"x\":5,\"y\":1},\"length\":"
            + "5,\"direction\":\"HORIZONTAL\"},{\"coord\":{\"x\":1,\"y\":5},\"length\":6,\"dir"
            + "ection\":\"VERTICAL\"},{\"coord\":{\"x\":3,\"y\":5},\"length\":4,\"direction\":\"HO"
            + "RIZONTAL\"},{\"coord\":{\"x\":7,\"y\":6},\"length\":"
            + "6,\"direction\":\"VERTICAL\"}]}}\n"
            + "{\"method-name\":\"successful-hits\",\"arguments\":{}}\n";


    assertEquals(expected, logToString());

  }

  /**
   * Tests the ability to end the game
   *
   * @throws IOException - if there is an issue with output to view
   */
  @Test
  public void testEnd() throws IOException {

    EndGameJson endGameJson = new EndGameJson("WIN",
        "Your player did not return a valid volley");
    JsonNode sampleMessage2 = createSampleMessage("end-game", endGameJson);
    //List<String> servermessage = Arrays.asList("{\"method-name\":\"join\",\"arguments\":{}}");
    List<String> serverMessage = Arrays.asList(
        sampleMessage2.toString());

    Mocket socket = new Mocket(this.testLog, serverMessage);

    Appendable appendable = new StringBuilder();
    GameViewer view = new GameViewer(appendable);
    Randomable rand = new RandomDecorator(22);
    OpponentImpl opp = new OpponentImpl(view, rand);


    // Create a Dealer
    try {
      this.dealer = new ProxyDealer(socket, new OpponentImpl(view, rand));
      this.dealer.run();
    } catch (IOException e) {
      fail();
    }

    String expected = "";
    assertEquals(expected, logToString());
  }

  /**
   * Tests end game with a loss
   *
   * @throws IOException - if there is a problem with output
   */
  @Test
  public void testEndLose() throws IOException {
    EndGameJson endGameJson = new EndGameJson("\"LOSE\"",
        "Your player did not return a valid volley");
    JsonNode sampleMessage2 = createSampleMessage("end-game", endGameJson);
    //List<String> servermessage = Arrays.asList("{\"method-name\":\"join\",\"arguments\":{}}");
    List<String> serverMessage = Arrays.asList(
        sampleMessage2.toString());

    Mocket socket = new Mocket(this.testLog, serverMessage);

    Appendable appendable = new StringBuilder();
    GameViewer view = new GameViewer(appendable);
    Randomable rand = new RandomDecorator(22);
    OpponentImpl opp = new OpponentImpl(view, rand);


    // Create a Dealer
    try {
      this.dealer = new ProxyDealer(socket, new OpponentImpl(view, rand));
      this.dealer.run();
    } catch (IOException e) {
      fail();
    }

    String expected = "";
    assertEquals(expected, logToString());
  }


  @Test
  public void testEndTie() {
    EndGameJson endGameJson = new EndGameJson("\"TIE\"",
        "Your player did not return a valid volley");
    JsonNode sampleMessage2 = createSampleMessage("end-game", endGameJson);
    //List<String> servermessage = Arrays.asList("{\"method-name\":\"join\",\"arguments\":{}}");
    List<String> serverMessage = Arrays.asList(
        sampleMessage2.toString());

    Mocket socket = new Mocket(this.testLog, serverMessage);

    Appendable appendable = new StringBuilder();
    GameViewer view = new GameViewer(appendable);
    Randomable rand = new RandomDecorator(22);
    OpponentImpl opp = new OpponentImpl(view, rand);


    // Create a Dealer
    try {
      this.dealer = new ProxyDealer(socket, new OpponentImpl(view, rand));
      this.dealer.run();
    } catch (IOException e) {
      fail();
    }

    String expected = "";
    assertEquals(expected, logToString());
  }


  /**
   * Tests when there is an error with sockets
   *
   * @throws IOException - error thrown with sockets
   */
  @Test
  void errorInrunTest() throws IOException {

    List<String> serverMessage = Arrays.asList();

    Mocket server = new Mocket(this.testLog, serverMessage);

    Appendable appendable = new StringBuilder();
    GameViewer view = new GameViewer(appendable);
    Randomable rand = new RandomDecorator(22);
    OpponentImpl opp = new OpponentImpl(view, rand);

    ProxyDealer proxyDealer = new ProxyDealer(server, opp);
    assertThrows(IllegalStateException.class, proxyDealer::run);
  }

  /**
   * Tests when there is an error with names and sockets
   *
   * @throws IOException - error thrown with names and sockets
   */
  @Test
  void errorInName() throws IOException {

    JoinJson winJson = new JoinJson("kaamil2", "SINGLE");
    JsonNode sampleMessage = createSampleMessage("jo", winJson);

    List<String> serverMessage = Arrays.asList(sampleMessage.toString());

    Mocket server = new Mocket(this.testLog, serverMessage);

    Appendable appendable = new StringBuilder();
    GameViewer view = new GameViewer(appendable);
    Randomable rand = new RandomDecorator(22);
    OpponentImpl opp = new OpponentImpl(view, rand);

    ProxyDealer proxyDealer = new ProxyDealer(server, opp);
    assertThrows(IllegalStateException.class, proxyDealer::run);

  }


  /**
   * Converts the ByteArrayOutputStream log to a string in UTF_8 format
   *
   * @return String representing the current log buffer
   */
  private String logToString() {
    return testLog.toString(StandardCharsets.UTF_8);
  }


  /**
   * Try converting the current test log to a string of a certain class.
   *
   * @param classRef Type to try converting the current test stream to.
   * @param <T>      Type to try converting the current test stream to.
   */
  private <T> void responseToClass(@SuppressWarnings("SameParameterValue") Class<T> classRef) {
    try {
      JsonParser jsonParser = new ObjectMapper().createParser(logToString());
      jsonParser.readValueAs(classRef);
      // No error thrown when parsing to a GuessJson, test passes!
    } catch (IOException e) {
      // Could not read
      // -> exception thrown
      // -> test fails since it must have been the wrong type of response.
      fail();
    }
  }

  /**
   * Create a MessageJson for some name and arguments.
   *
   * @param messageName   name of the type of message; "hint" or "win"
   * @param messageObject object to embed in a message json
   * @return a MessageJson for the object
   */
  private JsonNode createSampleMessage(String messageName, Record messageObject) {
    MessageJson messageJson =
        new MessageJson(messageName, JsonUtils.serializeRecord(messageObject));
    return JsonUtils.serializeRecord(messageJson);
  }
}
