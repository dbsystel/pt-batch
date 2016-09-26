package org.hisrc.ptbatch.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import org.hisrc.ptbatch.model.QueryDescription;
import org.junit.Before;
import org.junit.Test;

public class QueryGeneratorServiceTest {

    private File gtfsFile;
    private QueryGeneratorService queryGeneratorService;

    @Before
    public void setUp() {
        gtfsFile = new File("files/rnv.zip");
        queryGeneratorService = new QueryGeneratorService(new GtfsService(gtfsFile));
    }

    @Test
    public void generatesQueries() {
        final List<QueryDescription> generatedQueryDescriptions = queryGeneratorService
                        .generateQueries(10, LocalDate.now(), LocalDate.now().plusMonths(1));
        assertEquals(10, generatedQueryDescriptions);
    }

}
