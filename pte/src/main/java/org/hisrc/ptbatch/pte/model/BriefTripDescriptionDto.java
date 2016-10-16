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
                "query_to_lat",
                "least_duration_trip_first_departure_date_time",
                "least_duration_trip_last_arrival_date_time",
                "least_duration_trip_legs_count",
                "least_changes_trip_first_departure_date_time",
                "least_changes_trip_last_arrival_date_time",
                "least_changes_trip_legs_count"})
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
    @JsonProperty("least_duration_trip_first_departure_date_time")
    private final LocalDateTime leastDurationTripFirstDepartureDateTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonProperty("least_duration_trip_last_arrival_date_time")
    private final LocalDateTime leastDurationTripLastArrivalDateTime;
    @JsonProperty("least_duration_trip_legs_count")
    private final int leastDurationTripLegsCount;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonProperty("least_changes_trip_first_departure_date_time")
    private final LocalDateTime leastChangesTripFirstDepartureDateTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonProperty("least_changes_trip_last_arrival_date_time")
    private final LocalDateTime leastChangesTripLastArrivalDateTime;
    @JsonProperty("least_changes_trip_legs_count")
    private final int leastChangesTripLegsCount;

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
                    @JsonProperty("least_duration_trip_first_departure_date_time") LocalDateTime leastDurationTripFirstDepartureDateTime,
                    @JsonProperty("least_duration_trip_last_arrival_date_time") LocalDateTime leastDurationTripLastArrivalDateTime,
                    @JsonProperty("least_duration_trip_legs_count") int leastDurationTripLegsCount,
                    @JsonProperty("least_changes_trip_first_departure_date_time") LocalDateTime leastChangesTripFirstDepartureDateTime,
                    @JsonProperty("least_changes_trip_last_arrival_date_time") LocalDateTime leastChangesTripLastArrivalDateTime,
                    @JsonProperty("least_changes_trip_legs_count") int leastChangesTripLegsCount) {
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
        this.leastDurationTripFirstDepartureDateTime = leastDurationTripFirstDepartureDateTime;
        this.leastDurationTripLastArrivalDateTime = leastDurationTripLastArrivalDateTime;
        this.leastDurationTripLegsCount = leastDurationTripLegsCount;
        this.leastChangesTripFirstDepartureDateTime = leastChangesTripFirstDepartureDateTime;
        this.leastChangesTripLastArrivalDateTime = leastChangesTripLastArrivalDateTime;
        this.leastChangesTripLegsCount = leastChangesTripLegsCount;
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

    public LocalDateTime getLeastDurationTripFirstDepartureDateTime() {
        return leastDurationTripFirstDepartureDateTime;
    }

    public LocalDateTime getLeastDurationTripLastArrivalDateTime() {
        return leastDurationTripLastArrivalDateTime;
    }

    public int getLeastDurationTripLegsCount() {
        return leastDurationTripLegsCount;
    }
    
    public LocalDateTime getLeastChangesTripFirstDepartureDateTime() {
        return leastChangesTripFirstDepartureDateTime;
    }
    
    public LocalDateTime getLeastChangesTripLastArrivalDateTime() {
        return leastChangesTripLastArrivalDateTime;
    }
    
    public int getLeastChangesTripLegsCount() {
        return leastChangesTripLegsCount;
    }

    public static Object of(TripDescription value) {
        return of(value.getQuery(), value.getLeastDurationTrip(), value.getLeastChangesTrip());
    }

    public static BriefTripDescriptionDto of(QueryDescription queryDescription, Trip leastDurationTrip, Trip leastChangesTrip) {
        return new BriefTripDescriptionDto(queryDescription.getId(), queryDescription.getDateTime(),
                        queryDescription.getFrom().getId(), queryDescription.getFrom().getName(),
                        queryDescription.getFrom().getLon(), queryDescription.getFrom().getLat(),
                        queryDescription.getTo().getId(), queryDescription.getTo().getName(),
                        queryDescription.getTo().getLon(), queryDescription.getTo().getLat(),
                        LocalDateTime.ofInstant(
                                        Instant.ofEpochMilli(
                                                        leastDurationTrip.getFirstDepartureTime().getTime()),
                                        ZoneId.systemDefault()),
                        LocalDateTime.ofInstant(
                                        Instant.ofEpochMilli(leastDurationTrip.getLastArrivalTime().getTime()),
                                        ZoneId.systemDefault()),
                        leastDurationTrip.legs.size(),
                        LocalDateTime.ofInstant(
                                        Instant.ofEpochMilli(
                                                        leastChangesTrip.getFirstDepartureTime().getTime()),
                                        ZoneId.systemDefault()),
                        LocalDateTime.ofInstant(
                                        Instant.ofEpochMilli(leastChangesTrip.getLastArrivalTime().getTime()),
                                        ZoneId.systemDefault()),
                        leastChangesTrip.legs.size());
    }

}
