package cs3500.pa03.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa03.model.Coord;
import java.util.List;

/**
 * Represents a list of JSON coordinates.
 *
 * @param volley - the list of JSON Coordinates
 */
public record VolleyJson(@JsonProperty("coordinates") List<Coord> volley) {

  /**
   * Returns the volley associated with this object.
   *
   * @return - this object's volley.
   */
  public List<Coord> getVolley() {
    return this.volley;
  }
}
