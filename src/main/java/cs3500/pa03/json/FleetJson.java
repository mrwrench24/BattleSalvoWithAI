package cs3500.pa03.json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a fleet (list of ships) in JSON.
 *
 * @param fleet - a list of Ship objects in JSON form (as a ShipAdapter)
 */
public record FleetJson(@JsonProperty("fleet") ShipAdapter[] fleet) {
}
