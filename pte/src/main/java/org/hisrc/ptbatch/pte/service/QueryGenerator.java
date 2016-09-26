package org.hisrc.ptbatch.pte.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

import org.hisrc.ptbatch.model.QueryDescription;
import org.hisrc.ptbatch.model.StopDescription;
import org.hisrc.ptbatch.pte.model.StopLocationMapping;

public class QueryGenerator {

    private final Random random = new Random();

    private static final int MAX_DISTANCE = 50;

    public List<QueryDescription> generateQueries(List<StopLocationMapping> stopLocationMappings,
                    int count, LocalDate startDate, LocalDate endDate) {
        final List<QueryDescription> queryDescriptions = new ArrayList<>(count);
        final List<StopDescription> stopDescriptions = stopLocationMappings.stream().filter(
                        stopLocationMapping -> stopLocationMapping.getDistance() <= MAX_DISTANCE)
                        .map(StopLocationMapping::getStop).collect(Collectors.toList());

        if (stopDescriptions.size() < 2) {
            return Collections.emptyList();
        }

        for (int index = 0; index < count; index++) {
            final StopDescription firstStopDescription = selectRandomStop(stopDescriptions, null);
            final StopDescription secondStopDescription = selectRandomStop(stopDescriptions,
                            firstStopDescription);
            final LocalDateTime dateTime = selectRandomDateTime(startDate, endDate);
            final QueryDescription queryDescription = new QueryDescription(dateTime,
                            firstStopDescription, secondStopDescription);
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

    private StopDescription selectRandomStop(List<StopDescription> stops,
                    StopDescription excludedStop) {
        StopDescription randomStop = null;
        do {
            randomStop = stops.get(random.nextInt(stops.size()));
        } while (Objects.equals(randomStop, excludedStop));
        return randomStop;
    }
}
