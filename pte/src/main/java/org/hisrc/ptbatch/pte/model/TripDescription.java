package org.hisrc.ptbatch.pte.model;

import org.hisrc.ptbatch.model.QueryDescription;

import de.schildbach.pte.dto.Trip;

public class TripDescription {
    
    private final QueryDescription query;
    
    private final Trip trip;

    public TripDescription(QueryDescription query, Trip trip) {
        this.query = query;
        this.trip = trip;
    }

    public QueryDescription getQuery() {
        return query;
    }
    
    public Trip getTrip() {
        return trip;
    }
}
