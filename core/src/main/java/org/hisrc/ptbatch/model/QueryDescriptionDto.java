package org.hisrc.ptbatch.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "date_time", "from_id", "from_name", "from_lon", "from_lat", "to_id", "to_name",
                "to_lon", "to_lat" })
public class QueryDescriptionDto {
    
    @JsonProperty("id")
    private final String id;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonProperty("date_time")
    private final LocalDateTime dateTime;
    @JsonProperty("from_id")
    private final String fromId;
    @JsonProperty("from_name")
    private final String fromName;
    @JsonProperty("from_lon")
    private final double fromLon;
    @JsonProperty("from_lat")
    private final double fromLat;
    @JsonProperty("to_id")
    private final String toId;
    @JsonProperty("to_name")
    private final String toName;
    @JsonProperty("to_lon")
    private final double toLon;
    @JsonProperty("to_lat")
    private final double toLat;
    @JsonProperty("optimization")
    private final Optimization optimization;

    @JsonCreator
    public QueryDescriptionDto(
                    @JsonProperty("id") String id,
                    @JsonProperty("date_time") LocalDateTime dateTime,
                    @JsonProperty("from_id") String fromId,
                    @JsonProperty("from_name") String fromName,
                    @JsonProperty("from_lon") double fromLon,
                    @JsonProperty("from_lat") double fromLat,
                    @JsonProperty("to_id") String toId,
                    @JsonProperty("to_name") String toName,
                    @JsonProperty("to_lon") double toLon,
                    @JsonProperty("to_lat") double toLat,
                    @JsonProperty("optimization") Optimization optimization) {
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
        this.optimization = optimization;
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
    
    public Optimization getOptimization() {
        return optimization;
    }

    public static QueryDescriptionDto of(QueryDescription queryDescription) {
        return new QueryDescriptionDto(
                        queryDescription.getId(),
                        queryDescription.getDateTime(),
                        queryDescription.getFrom().getId(), queryDescription.getFrom().getName(),
                        queryDescription.getFrom().getLon(), queryDescription.getFrom().getLat(),
                        queryDescription.getTo().getId(), queryDescription.getTo().getName(),
                        queryDescription.getTo().getLon(), queryDescription.getTo().getLat(),
                        queryDescription.getOptimization());
    }

}
