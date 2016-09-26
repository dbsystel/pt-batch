package org.hisrc.ptbatch.model;

import java.util.Objects;

import org.onebusaway.gtfs.model.Stop;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Location {

    private String id;
    private String name;
    private double lon;
    private double lat;

    @JsonCreator
    public Location(@JsonProperty("id") String id, @JsonProperty("name") String name,
                    @JsonProperty("lon") double lon, @JsonProperty("lat") double lat) {
        this.id = id;
        this.name = name;
        this.lon = lon;
        this.lat = lat;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }

    @Override
    public String toString() {
        return "Location [id=" + id + ", name=" + name + ", lon=" + lon + ", lat=" + lat + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lon, lat);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null)
            return false;
        if (getClass() != object.getClass())
            return false;
        final Location that = (Location) object;
        return Objects.equals(this.id, that.id) && Objects.equals(this.name, that.name)
                        && Double.doubleToLongBits(this.lon) == Double.doubleToLongBits(that.lon)
                        && Double.doubleToLongBits(this.lat) == Double.doubleToLongBits(that.lat);
    }

    public static Location of(Stop stop) {
        return new Location(stop.getId().getId(), stop.getName(), stop.getLon(), stop.getLat());
    }

}
