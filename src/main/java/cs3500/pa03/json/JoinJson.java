
package cs3500.pa03.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

/**
 * Represents a JSON object used for joining the game, providing information needed by the server
 * to begin a game of Battleship.
 *
 * @param name     - The GitHub username which should be credited with a massive victory (hopefully)
 * @param gameType - The type of game we want to play, either SINGLE or MULTI.
 */
public record JoinJson(@JsonProperty("name") String name,
                       @JsonProperty("game-type") String gameType) {
}














