package client;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa03.json.FleetJson;
import cs3500.pa03.model.ShipType;
import java.util.HashMap;

/**
 * A mock json object that mimics setup instructions.
 *
 * @param width - the width to use for board
 * @param height - the height to use for board
 * @param map - the shiptypes and number of times each should show up
 */
public record SetupMock(@JsonProperty ("width") int width, @JsonProperty ("height") int height,
                        @JsonProperty ("fleet-spec") HashMap<ShipType, Integer> map) {
}
