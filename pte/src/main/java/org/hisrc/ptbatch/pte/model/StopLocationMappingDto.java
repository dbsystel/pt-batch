package org.hisrc.ptbatch.pte.model;

import org.hisrc.ptbatch.model.StopDescription;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.LocationType;
import de.schildbach.pte.dto.Product;

@JsonPropertyOrder({ "stop_id", "stop_name", "stop_lon", "stop_lat", "location_type", "location_id",
                "location_lon", "location_lat", "location_place", "location_name",
                "location_products", "distance" })
public class StopLocationMappingDto {

    @JsonProperty("stop_id")
    private final String stopId;
    @JsonProperty("stop_name")
    private final String stopName;
    @JsonProperty("stop_lon")
    private final double stopLon;
    @JsonProperty("stop_lat")
    private final double stopLat;

    @JsonProperty("location_type")
    private final LocationType locationType;
    @JsonProperty("location_id")
    private final String locationId;
    @JsonProperty("location_lon")
    private final int locationLon;
    @JsonProperty("location_lat")
    private final int locationLat;
    @JsonProperty("location_place")
    private final String locationPlace;
    @JsonProperty("location_name")
    private final String locationName;
    @JsonProperty("location_products")
    private final Product[] locationProducts;
    @JsonProperty("distance")
    private final int distance;

    @JsonCreator
    public StopLocationMappingDto(@JsonProperty("stop_id") String stopId,
                    @JsonProperty("stop_name") String stopName,
                    @JsonProperty("stop_lon") double stopLon,
                    @JsonProperty("stop_lat") double stoplat,
                    @JsonProperty("location_type") LocationType locationType,
                    @JsonProperty("location_id") String locationId,
                    @JsonProperty("location_lon") int locationLon,
                    @JsonProperty("location_lat") int locationLat,
                    @JsonProperty("location_place") String locationPlace,
                    @JsonProperty("location_name") String locationName,
                    @JsonProperty("location_products") Product[] locationProducts,
                    @JsonProperty("distance") int distance) {
        this.stopId = stopId;
        this.stopName = stopName;
        this.stopLon = stopLon;
        this.stopLat = stoplat;
        this.locationType = locationType;
        this.locationId = locationId;
        this.locationLat = locationLat;
        this.locationLon = locationLon;
        this.locationPlace = locationPlace;
        this.locationName = locationName;
        this.locationProducts = locationProducts;
        this.distance = distance;
    }

    public String getStopId() {
        return stopId;
    }

    public String getStopName() {
        return stopName;
    }

    public double getStopLon() {
        return stopLon;
    }

    public double getStopLat() {
        return stopLat;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    public String getLocationId() {
        return locationId;
    }

    public int getLocationLat() {
        return locationLat;
    }

    public int getLocationLon() {
        return locationLon;
    }

    public String getLocationPlace() {
        return locationPlace;
    }

    public String getLocationName() {
        return locationName;
    }

    public Product[] getLocationProducts() {
        return locationProducts;
    }

    public int getDistance() {
        return distance;
    }

    public static StopLocationMappingDto of(StopLocationMapping mapping) {
        return of(mapping.getStop(), mapping.getLocation(), mapping.getDistance());
    }

    public static StopLocationMappingDto of(StopDescription stop, Location location, int distance) {
        return new StopLocationMappingDto(stop.getId(), stop.getName(), stop.getLon(),
                        stop.getLat(), location.type, location.id, location.lon, location.lat,
                        location.place, location.name,
                        location.products == null ? null
                                        : location.products.toArray(
                                                        new Product[location.products.size()]),
                        distance);

    }

}
