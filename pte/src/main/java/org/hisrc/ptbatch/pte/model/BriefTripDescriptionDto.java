package org.hisrc.ptbatch.pte.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.hisrc.ptbatch.model.QueryDescription;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.schildbach.pte.dto.Trip;

@JsonPropertyOrder({ "query_id", "query_date_time", "query_from_id", "query_from_name",
                "query_from_lon", "query_from_lat", "query_to_id", "query_to_name", "query_to_lon",
                "query_to_lat", "trip_first_departure_date_time", "trip_last_arrival_date_time",
                "trip_legs_count" })
public class BriefTripDescriptionDto {

    @JsonProperty("query_id")
    private final String id;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonProperty("query_date_time")
    private final LocalDateTime dateTime;
    @JsonProperty("query_from_id")
    private final String fromId;
    @JsonProperty("query_from_name")
    private final String fromName;
    @JsonProperty("query_from_lon")
    private final double fromLon;
    @JsonProperty("query_from_lat")
    private final double fromLat;
    @JsonProperty("query_to_id")
    private final String toId;
    @JsonProperty("query_to_name")
    private final String toName;
    @JsonProperty("query_to_lon")
    private final double toLon;
    @JsonProperty("query_to_lat")
    private final double toLat;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonProperty("trip_first_departure_date_time")
    private final LocalDateTime firstDepartureDateTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonProperty("trip_last_arrival_date_time")
    private final LocalDateTime lastArrivalDateTime;
    @JsonProperty("trip_legs_count")
    private final int legsCount;

    @JsonCreator
    public BriefTripDescriptionDto(@JsonProperty("query_id") String id,
                    @JsonProperty("query_date_time") LocalDateTime dateTime,
                    @JsonProperty("query_from_id") String fromId,
                    @JsonProperty("query_from_name") String fromName,
                    @JsonProperty("query_from_lon") double fromLon,
                    @JsonProperty("query_from_lat") double fromLat,
                    @JsonProperty("query_to_id") String toId,
                    @JsonProperty("query_to_name") String toName,
                    @JsonProperty("query_to_lon") double toLon,
                    @JsonProperty("query_to_lat") double toLat,
                    @JsonProperty("trip_first_departure_date_time") LocalDateTime firstDepartureDateTime,
                    @JsonProperty("trip_last_arrival_date_time") LocalDateTime lastArrivalDateTime,
                    @JsonProperty("trip_legs_count") int legsCount) {
        this.id = id;
        this.dateTime = dateTime;
        this.fromId = fromId;
        this.fromName = fromName;
        this.fromLon = fromLon;
        this.fromLat = fromLat;
        this.toId = toId;
        this.toName = toName;
        this.toLon = toLon;
        this.toLat = toLat;
        this.firstDepartureDateTime = firstDepartureDateTime;
        this.lastArrivalDateTime = lastArrivalDateTime;
        this.legsCount = legsCount;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getFromId() {
        return fromId;
    }

    public String getFromName() {
        return fromName;
    }

    public double getFromLon() {
        return fromLon;
    }

    public double getFromLat() {
        return fromLat;
    }

    public String getToId() {
        return toId;
    }

    public String getToName() {
        return toName;
    }

    public double getToLon() {
        return toLon;
    }

    public double getToLat() {
        return toLat;
    }

    public LocalDateTime getFirstDepartureDateTime() {
        return firstDepartureDateTime;
    }

    public LocalDateTime getLastArrivalDateTime() {
        return lastArrivalDateTime;
    }

    public int getLegsCount() {
        return legsCount;
    }

    public static Object of(TripDescription value) {
        return of(value.getQuery(), value.getTrip());
    }

    public static BriefTripDescriptionDto of(QueryDescription queryDescription, Trip trip) {
        return new BriefTripDescriptionDto(queryDescription.getId(), queryDescription.getDateTime(),
                        queryDescription.getFrom().getId(), queryDescription.getFrom().getName(),
                        queryDescription.getFrom().getLon(), queryDescription.getFrom().getLat(),
                        queryDescription.getTo().getId(), queryDescription.getTo().getName(),
                        queryDescription.getTo().getLon(), queryDescription.getTo().getLat(),
                        LocalDateTime.ofInstant(
                                        Instant.ofEpochMilli(
                                                        trip.getFirstDepartureTime().getTime()),
                                        ZoneId.systemDefault()),
                        LocalDateTime.ofInstant(
                                        Instant.ofEpochMilli(trip.getLastArrivalTime().getTime()),
                                        ZoneId.systemDefault()),
                        trip.legs.size());
    }

}
