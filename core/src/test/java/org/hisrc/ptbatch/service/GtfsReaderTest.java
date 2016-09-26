package org.hisrc.ptbatch.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Collection;

import org.hisrc.ptbatch.service.GtfsReader;
import org.junit.Before;
import org.junit.Test;
import org.onebusaway.gtfs.model.Stop;

public class GtfsReaderTest {

    private File gtfsFile;
    private GtfsReader gtfsReader;

    @Before
    public void setUp() {
        gtfsFile = new File("files/rnv.zip");
        gtfsReader = new GtfsReader(gtfsFile);
    }

    @Test
    public void testStops() {
        Collection<Stop> stops = gtfsReader.getStops();
        assertEquals(2070, stops.size());
    }

}
