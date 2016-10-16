package org.hisrc.ptbatch.pte.model;

import org.hisrc.ptbatch.model.QueryDescription;

import de.schildbach.pte.dto.Trip;

public class TripDescription {
    
    private final QueryDescription query;
    
    private final Trip leastDurationTrip;

    private final Trip leastChangesTrip;

    public TripDescription(QueryDescription query, Trip leastDurationTrip, Trip leastChangesTrip) {
        this.query = query;
        this.leastDurationTrip = leastDurationTrip;
        this.leastChangesTrip = leastChangesTrip;
    }

    public QueryDescription getQuery() {
        return query;
    }
    
    public Trip getLeastDurationTrip() {
        return leastDurationTrip;
    }
    
    public Trip getLeastChangesTrip() {
        return leastChangesTrip;
    }
}
