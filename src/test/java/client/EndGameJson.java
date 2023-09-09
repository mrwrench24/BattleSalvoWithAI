package client;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa03.json.FleetJson;
import cs3500.pa03.model.ShipType;
import java.util.HashMap;

/**
 * Represents a message for the end of a Battleship game in JSON.
 *
 * @param result - either "WIN" "LOSE" or "TIE"
 * @param reason - a description as to why the game was ended by the server.
 */
public record EndGameJson(@JsonProperty("result") String result,
                          @JsonProperty("reason") String reason) {
}