package org.hisrc.ptbatch.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import javax.management.Query;

import org.hisrc.ptbatch.model.Location;
import org.hisrc.ptbatch.model.QueryDescription;
import org.onebusaway.gtfs.model.Stop;

public class QueryGeneratorService {

    private Random random = new Random();

    private final GtfsService gtfsService;

    public QueryGeneratorService(GtfsService gtfsService) {
        this.gtfsService = gtfsService;
    }

    public List<QueryDescription> generateQueries(int count, LocalDate startDate,
                    LocalDate endDate) {
        final List<QueryDescription> queryDescriptions = new ArrayList<>(count);
        final List<Stop> stops = new ArrayList<>(gtfsService.getStops());
        for (int index = 0; index < count; index++) {
            final Stop firstStop = selectRandomStop(stops, null);
            final Stop secondStop = selectRandomStop(stops, firstStop);
            final Location firstLocation = Location.of(firstStop);
            final Location secondLocation = Location.of(secondStop);
            final LocalDateTime dateTime = selectRandomDateTime(startDate, endDate);
            final QueryDescription queryDescription = new QueryDescription(dateTime, firstLocation,
                            secondLocation);
            queryDescriptions.add(queryDescription);
        }
        Collections.sort(queryDescriptions, Comparator.comparing(QueryDescription::getDateTime));
        return queryDescriptions;
    }

    private LocalDateTime selectRandomDateTime(LocalDate startDate, LocalDate endDate) {

        final LocalDateTime startDateTime = startDate.atTime(0, 0);
        final LocalDateTime endDateTime = endDate.atTime(23, 59);
        final long minutesBetween = ChronoUnit.MINUTES.between(startDateTime,
                        endDateTime) + 1;
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
