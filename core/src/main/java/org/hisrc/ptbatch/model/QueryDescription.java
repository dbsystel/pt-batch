package org.hisrc.ptbatch.model;

import java.time.LocalDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class QueryDescription {

    private LocalDateTime dateTime;
    private Location from;
    private Location to;

    @JsonCreator
    public QueryDescription(@JsonProperty("dateTime") LocalDateTime dateTime,
                    @JsonProperty("from") Location from, @JsonProperty("to") Location to) {
        this.dateTime = dateTime;
        this.from = from;
        this.to = to;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime, from, to);
    }

    @Override
    public String toString() {
        return "QueryDescription [dateTime=" + dateTime + ", from=" + from + ", to=" + to + "]";
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
        return Objects.equals(this.dateTime, that.dateTime) && Objects.equals(this.from, that.from)
                        && Objects.equals(this.to, that.to);
    }

}
