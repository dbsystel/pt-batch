package org.hisrc.ptbatch.pte.service;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.hisrc.ptbatch.model.Optimization;
import org.hisrc.ptbatch.model.StopDescription;
import org.hisrc.ptbatch.pte.model.StopLocationMapping;
import org.hisrc.ptbatch.util.LonLatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.NetworkProvider.Accessibility;
import de.schildbach.pte.NetworkProvider.Optimize;
import de.schildbach.pte.NetworkProvider.WalkSpeed;
import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.LocationType;
import de.schildbach.pte.dto.NearbyLocationsResult;
import de.schildbach.pte.dto.Product;
import de.schildbach.pte.dto.QueryTripsResult;
import de.schildbach.pte.dto.Trip;

public abstract class AbstractNetworkProviderService {

    private static final double MULTIPLIER = 1000000;
    private static final int MAX_DISTANCE = 1000;
    private static final int MAX_NUMBER_OF_LOCATIONS = 16;
    private static final Logger LOGGER = LoggerFactory
                    .getLogger(AbstractNetworkProviderService.class);

    private final NetworkProvider networkProvider;

    public AbstractNetworkProviderService(NetworkProvider networkProvider) {
        this.networkProvider = networkProvider;
    }

    public Trip findTrip(LocalDateTime dateTime, Location from, Location to,
                    Optimization optimization) throws IOException {
        final List<Trip> trips = new ArrayList<>(
                        findTrips(optimization(optimization), dateTime, from, to));
        Collections.sort(trips, comparator(optimization));
        return trips.isEmpty() ? null : trips.get(0);
    }

    private Comparator<Trip> comparator(Optimization optimization) {
        switch (optimization) {
        case LEAST_CHANGES:
            return Comparator.<Trip, Integer>comparing(trip -> trip.legs.size())
                            .thenComparing(trip -> trip.getDuration());
        case LEAST_DURATION:
            return Comparator.<Trip, Long>comparing(trip -> trip.getDuration())
                            .thenComparing(trip -> trip.legs.size());
        case LEAST_WALKING:
            // TODO this is not quite corrct
            return Comparator.<Trip, Long>comparing(trip -> trip.getDuration())
                            .thenComparing(trip -> trip.legs.size());
        default:
            throw new IllegalArgumentException();
        }
    }

    private Optimize optimization(Optimization optimization) {
        switch (optimization) {
        case LEAST_CHANGES:
            return Optimize.LEAST_CHANGES;
        case LEAST_DURATION:
            return Optimize.LEAST_DURATION;
        case LEAST_WALKING:
            return Optimize.LEAST_WALKING;
        default:
            throw new IllegalArgumentException();
        }
    }

    private Collection<Trip> findTrips(Optimize optimize, LocalDateTime dateTime, Location from,
                    Location to) throws IOException {
        final long timestamp = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        final Instant instant = dateTime.atZone(ZoneId.systemDefault()).toInstant();
        final Date date = Date.from(instant);

        final Function<NetworkProvider, QueryTripsResult> query = networkProvider -> {
            try {
                final QueryTripsResult result = networkProvider.queryTrips(from, null, to, date,
                                true, Product.ALL, optimize, WalkSpeed.NORMAL,
                                Accessibility.NEUTRAL, null);
                return result;
            } catch (IOException ioex) {
                throw new RuntimeException(ioex);
            }
        };
        final Function<QueryTripsResult, Boolean> statusCheck = result -> result.status != QueryTripsResult.Status.SERVICE_DOWN;
        final QueryTripsResult result = execute(query, statusCheck);
        if (result.trips == null) {
            return Collections.emptyList();
        } else {
            // This is needed because bahn.de uses a substracts buffer of 10 minutes from the start time,
            // so it can deliver trips departing earlier.
            return result.trips.stream().filter(trip -> trip.getFirstDepartureTime().getTime() >= timestamp).collect(Collectors.toList());
        }
    }

    public StopLocationMapping resolveStopLocationMapping(StopDescription stop) throws IOException {
        final Function<NetworkProvider, NearbyLocationsResult> query = networkProvider -> {
            try {
                final NearbyLocationsResult result = this.networkProvider.queryNearbyLocations(
                                EnumSet.of(LocationType.STATION),
                                new Location(LocationType.COORD, null,
                                                toIntCoordinate(stop.getLat()),
                                                toIntCoordinate(stop.getLon())),
                                MAX_DISTANCE, MAX_NUMBER_OF_LOCATIONS);
                return result;
            } catch (IOException ioex) {
                throw new RuntimeException(ioex);
            }
        };
        final Function<NearbyLocationsResult, Boolean> statusCheck = result -> result.status != NearbyLocationsResult.Status.SERVICE_DOWN;

        final NearbyLocationsResult result = execute(query, statusCheck);

        final List<Location> locations = new ArrayList<>(result.locations);

        Collections.sort(locations, Comparator.comparingDouble(location -> {
            final double dlon = stop.getLon() - (location.lon / MULTIPLIER);
            final double dlat = stop.getLat() - (location.lat / MULTIPLIER);
            return dlon * dlon + dlat * dlat;
        }));

        if (!Objects.equals(locations, result.locations)) {
            LOGGER.warn("Returned locations seem not to be sorted accourding to distances to the stop ["
                            + stop + "]. Original order:\n" + result.locations + "\nSorted order:\n"
                            + locations);
        }
        if (!locations.isEmpty()) {
            final Location location = locations.get(0);
            final int distance1 = (int) Math.round(LonLatUtils.distance(stop.getLon(),
                            stop.getLat(), location.getLonAsDouble(), location.getLatAsDouble()));
            return new StopLocationMapping(stop, location, distance1);
        } else {
            return null;
        }
    }

    private final long INITIAL_PAUSE = 125;
    private long lastTimestamp = 0;
    private long pause = INITIAL_PAUSE;

    private <R> R execute(Function<NetworkProvider, R> query, Function<R, Boolean> statusCheck)
                    throws IOException {
        boolean done = false;
        R result;
        do {
            try {
                long currentTimestamp = System.currentTimeMillis();
                if (currentTimestamp < lastTimestamp + pause) {
                    try {
                        final long sleepTime = lastTimestamp + pause - currentTimestamp;
                        LOGGER.debug("Sleeping for {} ms.", sleepTime);
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException ignored) {
                    }
                }
                lastTimestamp = System.currentTimeMillis();
                result = query.apply(this.networkProvider);
            } catch (RuntimeException rex) {
                if (rex.getCause() instanceof IOException) {
                    throw (IOException) rex.getCause();
                } else {
                    throw rex;
                }
            }
            if (statusCheck.apply(result)) {
                pause = INITIAL_PAUSE;
                done = true;
            } else {
                pause *= 2;
                LOGGER.warn("Result failed status check, increasing the pause to {}ms.", pause);
            }
        } while (!done);
        return result;
    }

    private int toIntCoordinate(double coordinate) {
        return (int) Math.round(coordinate * MULTIPLIER);
    }
}
