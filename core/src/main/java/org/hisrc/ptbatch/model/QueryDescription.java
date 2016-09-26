package org.hisrc.ptbatch.model;

import java.time.LocalDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class QueryDescription {

    private LocalDateTime dateTime;
    private StopDescription from;
    private StopDescription to;

    @JsonCreator
    public QueryDescription(@JsonProperty("dateTime") LocalDateTime dateTime,
                    @JsonProperty("from") StopDescription from, @JsonProperty("to") StopDescription to) {
        this.dateTime = dateTime;
        this.from = from;
        this.to = to;
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
