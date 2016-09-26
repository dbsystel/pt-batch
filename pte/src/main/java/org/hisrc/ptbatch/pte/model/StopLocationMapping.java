package org.hisrc.ptbatch.pte.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

import org.hisrc.ptbatch.model.StopDescription;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.Product;

public class StopLocationMapping {

    private final StopDescription stop;
    private final Location location;
    private final int distance;

    @JsonCreator
    public StopLocationMapping(@JsonProperty("stop") StopDescription stop,
                    @JsonProperty("location") Location location,
                    @JsonProperty("distance") int distance) {
        this.stop = stop;
        this.location = location;
        this.distance = distance;
    }

    public StopDescription getStop() {
        return stop;
    }

    public Location getLocation() {
        return location;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stop, location, distance);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null)
            return false;
        if (getClass() != object.getClass())
            return false;
        StopLocationMapping that = (StopLocationMapping) object;
        return Objects.equals(this.stop, that.stop) && Objects.equals(this.location, that.location)
                        && Objects.equals(this.distance, that.distance);
    }

    public static StopLocationMapping of(StopLocationMappingDto dto) {
        return new StopLocationMapping(
                        new StopDescription(dto.getStopId(), dto.getStopName(), dto.getStopLon(),
                                        dto.getStopLat()),
                        new Location(dto.getLocationType(), dto.getLocationId(),
                                        dto.getLocationLat(), dto.getLocationLon(),
                                        dto.getLocationPlace(), dto.getLocationName(),
                                        dto.getLocationProducts() == null ? null
                                                        : new HashSet<Product>(Arrays
                                                                        .asList(dto.getLocationProducts()))),
                        dto.getDistance());
    }
}
