package org.hisrc.ptbatch.pte.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.onebusaway.gtfs.model.Stop;

public class GtfsServiceTest {

    private File gtfsFile;
    private GtfsService gtfsService;

    @Before
    public void setUp() {
        gtfsFile = new File("files/rnv.zip");
        gtfsService = new GtfsService(gtfsFile);
    }

    @Test
    public void testStops() {
        Collection<Stop> stops = gtfsService.getStops();
        assertEquals(2070, stops.size());
    }

}
