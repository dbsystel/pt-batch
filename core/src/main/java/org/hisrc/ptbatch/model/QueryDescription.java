package org.hisrc.ptbatch.model;

import java.time.LocalDateTime;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class QueryDescription {

    private String id;
    private LocalDateTime dateTime;
    private StopDescription from;
    private StopDescription to;

    public QueryDescription(@JsonProperty("dateTime") LocalDateTime dateTime,
                    @JsonProperty("from") StopDescription from,
                    @JsonProperty("to") StopDescription to) {
        this.id = StringUtils.leftPad(Integer.toHexString(Objects.hash(dateTime, from, to)), 8, '0');
        this.dateTime = dateTime;
        this.from = from;
        this.to = to;
    }

    @JsonCreator
    public QueryDescription(@JsonProperty("id") String id,
                    @JsonProperty("dateTime") LocalDateTime dateTime,
                    @JsonProperty("from") StopDescription from,
                    @JsonProperty("to") StopDescription to) {
        this.id = id;
        this.dateTime = dateTime;
        this.from = from;
        this.to = to;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public StopDescription getFrom() {
        return from;
    }

    public StopDescription getTo() {
        return to;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateTime, from, to);
    }

    @Override
    public String toString() {
        return "QueryDescription [id=" + id + ", dateTime=" + dateTime + ", from=" + from + ", to="
                        + to + "]";
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null)
            return false;
        if (getClass() != object.getClass())
            return false;
        final QueryDescription that = (QueryDescription) object;
        return Objects.equals(this.id, that.id) && Objects.equals(this.dateTime, that.dateTime)
                        && Objects.equals(this.from, that.from) && Objects.equals(this.to, that.to);
    }

    public static QueryDescription of(QueryDescriptionDto dto) {
        return new QueryDescription(dto.getDateTime(),
                        new StopDescription(dto.getFromId(), dto.getFromName(), dto.getFromLon(),
                                        dto.getFromLat()),
                        new StopDescription(dto.getToId(), dto.getToName(), dto.getToLon(),
                                        dto.getToLat()));
    }
}
