package org.hisrc.ptbatch.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.hisrc.ptbatch.model.StopDescription;
import org.hisrc.ptbatch.model.QueryDescription;
import org.onebusaway.gtfs.model.Stop;

public class QueryGenerator {

    private final Random random = new Random();

    private final GtfsReader gtfsReader;

    public QueryGenerator(GtfsReader gtfsService) {
        this.gtfsReader = gtfsService;
    }

    public List<QueryDescription> generateQueries(int count, LocalDate startDate,
                    LocalDate endDate) {
        final List<QueryDescription> queryDescriptions = new ArrayList<>(count);
        final List<Stop> stops = new ArrayList<>(gtfsReader.getStops());
        for (int index = 0; index < count; index++) {
            final Stop firstStop = selectRandomStop(stops, null);
            final Stop secondStop = selectRandomStop(stops, firstStop);
            final StopDescription firstStopDescription = StopDescription.of(firstStop);
            final StopDescription secondStopDescription = StopDescription.of(secondStop);
            final LocalDateTime dateTime = selectRandomDateTime(startDate, endDate);
            final QueryDescription queryDescription = new QueryDescription(dateTime, firstStopDescription,
                            secondStopDescription);
            queryDescriptions.add(queryDescription);
        }
        Collections.sort(queryDescriptions, Comparator.comparing(QueryDescription::getDateTime));
        return queryDescriptions;
    }

    private LocalDateTime selectRandomDateTime(LocalDate startDate, LocalDate endDate) {

        final LocalDateTime startDateTime = startDate.atTime(0, 0);
        final LocalDateTime endDateTime = endDate.atTime(23, 59);
        final long minutesBetween = ChronoUnit.MINUTES.between(startDateTime, endDateTime) + 1;
        if (minutesBetween > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Range between start date [" + startDate
                            + "] and end date [" + endDate + "] is too big.");
        }
        final int randomMinutes = random.nextInt((int) minutesBetween);

        return startDateTime.plusMinutes(randomMinutes);
    }

    private Stop selectRandomStop(List<Stop> stops, Stop excludedStop) {
        Stop randomStop = null;
        do {
            randomStop = stops.get(random.nextInt(stops.size()));
        } while (randomStop == excludedStop);
        return randomStop;
    }

}
