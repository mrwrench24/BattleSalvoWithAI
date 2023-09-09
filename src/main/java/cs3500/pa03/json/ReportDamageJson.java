package cs3500.pa03.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa03.model.Coord;

/**
 * Represents a JSON object for reporting shots that damaged ships on a board.
 *
 * @param coordinates - shots from an opponent that hit ships
 */
public record ReportDamageJson(@JsonProperty("coordinates") Coord[] coordinates) {
}
