package org.hisrc.ptbatch.pte.service.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashSet;

import org.hisrc.ptbatch.model.StopDescription;
import org.hisrc.ptbatch.pte.model.StopLocationMapping;
import org.hisrc.ptbatch.pte.service.BahnProviderService;
import org.junit.Test;

import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.LocationType;
import de.schildbach.pte.dto.Product;
import de.schildbach.pte.dto.Trip;

public class BahnProviderServiceTest {

    private BahnProviderService bahnProviderService = new BahnProviderService();

    @Test
    public void resolvesExistingStop() throws IOException {
        final StopLocationMapping mapping = bahnProviderService.resolveStopLocationMapping(
                        new StopDescription("554412", "Universitätsklinikum", 8.48448, 49.49388));
        assertEquals(new Location(LocationType.STATION, "508144", 49493690, 8484109, "Mannheim",
                        "Universitätsklinikum",
                        new HashSet<Product>(Arrays.asList(Product.TRAM, Product.BUS))),
                        mapping.getLocation());
    }

    @Test
    public void resolvesNonExistingStop() throws IOException {
        final StopLocationMapping mapping = bahnProviderService
                        .resolveStopLocationMapping(new StopDescription("None", "None", 10, 10));
        assertEquals(null, mapping);
    }

    @Test
    public void findsTripWithEarliestArrival() throws IOException {

        Trip trip = bahnProviderService.findTripWithEarliestArrival(
                        LocalDateTime.parse("2016-10-01T00:33:00"),
                        new Location(LocationType.STATION, "508176", 49489186, 8462472, "Mannheim",
                                        "Rathaus/Reiss-Museum",
                                        new HashSet<Product>(
                                                        Arrays.asList(Product.TRAM, Product.BUS))),
                        new Location(LocationType.STATION, "508010", 49487784, 8426506,
                                        "Ludwigshafen am Rhein", "Heinrich-Ries-Halle",
                                        new HashSet<Product>(
                                                        Arrays.asList(Product.TRAM, Product.BUS))));

        LocalDateTime firstDepartureDateTime = LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(trip.getFirstDepartureTime().getTime()),
                        ZoneId.systemDefault());
        LocalDateTime lastArrivalDateTime = LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(trip.getLastArrivalTime().getTime()),
                        ZoneId.systemDefault());

        assertEquals(LocalDateTime.parse("2016-10-01T01:21:00"), firstDepartureDateTime);
        assertEquals(LocalDateTime.parse("2016-10-01T01:39:00"), lastArrivalDateTime);
    }

}
