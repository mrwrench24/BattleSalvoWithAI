package client;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa03.json.FleetJson;
import cs3500.pa03.model.ShipType;
import java.util.HashMap;

/**
 * JSON object used for mocking the end of a game.
 *
 * @param result - either "WIN" "LOSE" or "TIE"
 * @param reason - why the game ended
 */
public record MockEndGame(@JsonProperty("result") String result,
                          @JsonProperty("reason") String reason) {
}