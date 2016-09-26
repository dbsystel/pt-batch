package org.hisrc.ptbatch.pte.service.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.io.output.WriterOutputStream;
import org.hisrc.ptbatch.model.StopDescription;
import org.hisrc.ptbatch.pte.model.StopLocationMapping;
import org.hisrc.ptbatch.pte.service.StopLocationMappingCsvReader;
import org.hisrc.ptbatch.pte.service.StopLocationMappingCsvWriter;
import org.junit.Before;
import org.junit.Test;

import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.LocationType;
import de.schildbach.pte.dto.Product;

public class StopLocationMappingCsvTest {

    private StopLocationMappingCsvReader reader;
    private StopLocationMappingCsvWriter writer;

    @Before
    public void setUp() {
        reader = new StopLocationMappingCsvReader();
        writer = new StopLocationMappingCsvWriter();
    }

    @Test
    public void writesAndReadsStopLocationMappings() throws IOException {
        final StopLocationMapping mapping = new StopLocationMapping(
                        new StopDescription("554412", "Universitätsklinikum", 8.48448, 49.49388),
                        new Location(LocationType.STATION, "508144", 49493690, 8484109, "Mannheim",
                                        "Universitätsklinikum", new HashSet<Product>(
                                                        Arrays.asList(Product.TRAM, Product.BUS))), 100);
        final List<StopLocationMapping> originalMappings = Arrays.asList(mapping);
        final StringWriter sw = new StringWriter();
        try (OutputStream os = new WriterOutputStream(sw)) {
            writer.write(originalMappings, os);
        }
        final String result = sw.toString();
        // System.out.println(result);
        assertEquals(251, result.length());
        final List<StopLocationMapping> readMappings;
        try (InputStream is = new ReaderInputStream(new StringReader(result))) {
            readMappings = reader.read(is);
        }
        assertEquals(originalMappings, readMappings);
    }
}
