package cs3500.pa03.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa03.model.Coord;

/**
 * Represents a JSON Object for shots taken by our A.I. as a list of Coordinates
 *
 * @param coordinates - the shots taken by our A.I.
 */
public record TakeShotsJson(@JsonProperty("coordinates") Coord[] coordinates) {
}
