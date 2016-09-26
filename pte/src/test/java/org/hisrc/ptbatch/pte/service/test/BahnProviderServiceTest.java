package org.hisrc.ptbatch.pte.service.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import org.hisrc.ptbatch.model.StopDescription;
import org.hisrc.ptbatch.pte.model.StopLocationMapping;
import org.hisrc.ptbatch.pte.service.BahnProviderService;
import org.junit.Test;

import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.LocationType;
import de.schildbach.pte.dto.Product;

public class BahnProviderServiceTest {

    private BahnProviderService bahnProviderService = new BahnProviderService();

    @Test
    public void resolvesExistingStop() throws IOException {
        final StopLocationMapping mapping = bahnProviderService.resolveStop(
                        new StopDescription("554412", "Universitätsklinikum", 8.48448, 49.49388));
        assertEquals(new Location(LocationType.STATION, "508144", 49493690, 8484109, "Mannheim",
                        "Universitätsklinikum",
                        new HashSet<Product>(Arrays.asList(Product.TRAM, Product.BUS))), mapping.getLocation());
    }

    @Test
    public void resolvesNonExistingStop() throws IOException {
        final StopLocationMapping mapping = bahnProviderService
                        .resolveStop(new StopDescription("None", "None", 10, 10));
        assertEquals(null, mapping);
    }

}
