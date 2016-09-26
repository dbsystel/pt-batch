package org.hisrc.ptbatch.pte;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.EnumSet;

import org.junit.Test;

import de.schildbach.pte.BahnProvider;
import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.LocationType;
import de.schildbach.pte.dto.NearbyLocationsResult;

public class LocationTest {

    @Test
    public void findsBurgstrByCoordinates() throws IOException {
        final NetworkProvider networkProvider = new BahnProvider();
//        113311,Burgstr.,49.434352,8.682286,,0
        final NearbyLocationsResult locationsResult = networkProvider.queryNearbyLocations(EnumSet.of(LocationType.STATION),
                        new Location(LocationType.COORD, null, 49434352, 8682286), 10, 10);
        assertEquals(NearbyLocationsResult.Status.OK, locationsResult.status);
    }
    
    @Test
    public void findsUniversitaetsklinikumbyCoordinates() throws IOException {
        final NetworkProvider networkProvider = new BahnProvider();
        final NearbyLocationsResult locationsResult = networkProvider.queryNearbyLocations(EnumSet.of(LocationType.STATION),
                        new Location(LocationType.COORD, null, 49493571,8483875), 100, 10);
        assertEquals(NearbyLocationsResult.Status.OK, locationsResult.status);
    }
    
    @Test
    public void findsKirchheimRathaus() throws IOException {
        final NetworkProvider networkProvider = new BahnProvider();
        final NearbyLocationsResult result50 = networkProvider.queryNearbyLocations(EnumSet.of(LocationType.STATION),
                        new Location(LocationType.COORD, null, 
                                        49377686, 8666235), 50, 10);
        final NearbyLocationsResult result100 = networkProvider.queryNearbyLocations(EnumSet.of(LocationType.STATION),
                        new Location(LocationType.COORD, null, 
                                        49377686, 8666235), 100, 10);
        assertEquals(0, result50.locations.size());
        assertEquals(1, result100.locations.size());
    }
    
    @Test
    public void findsEbertpark() throws IOException {
        final NetworkProvider networkProvider = new BahnProvider();
        final NearbyLocationsResult result50 = networkProvider.queryNearbyLocations(EnumSet.of(LocationType.STATION),
                        new Location(LocationType.COORD, null, 
                                        49489925, 8417615), 50, 10);
        final NearbyLocationsResult result100 = networkProvider.queryNearbyLocations(EnumSet.of(LocationType.STATION),
                        new Location(LocationType.COORD, null, 
                                        49489925, 8417615), 1000, 10);
        assertEquals(0, result50.locations.size());
        assertEquals(1, result100.locations.size());
    }
    
    
//    @Test
//    public void findsBurgstrByName() throws IOException {
//        final NetworkProvider networkProvider = new BahnProvider();
////        113311,Burgstr.,49.434352,8.682286,,0
//        SuggestLocationsResult result = networkProvider.suggestLocations("Burgstr.");
//        assertEquals(SuggestLocationsResult.Status.OK, result.status);
//    }
}
