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
import de.schildbach.pte.dto.SuggestLocationsResult;

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
//        113311,Burgstr.,49.434352,8.682286,,0
        final NearbyLocationsResult locationsResult = networkProvider.queryNearbyLocations(EnumSet.of(LocationType.STATION),
                        new Location(LocationType.COORD, null, 49493571,8483875), 100, 10);
        assertEquals(NearbyLocationsResult.Status.OK, locationsResult.status);
    }
    
    
//    @Test
//    public void findsBurgstrByName() throws IOException {
//        final NetworkProvider networkProvider = new BahnProvider();
////        113311,Burgstr.,49.434352,8.682286,,0
//        SuggestLocationsResult result = networkProvider.suggestLocations("Burgstr.");
//        assertEquals(SuggestLocationsResult.Status.OK, result.status);
//    }
}
