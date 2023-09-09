package cs3500.pa03.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * JSON to be sent to the server in response to the "setup" method.
 *
 * @param fleet - a fleet JSON object representing a list of Ships
 */
public record SetupJson(@JsonProperty("fleet") JsonNode fleet) {
}
