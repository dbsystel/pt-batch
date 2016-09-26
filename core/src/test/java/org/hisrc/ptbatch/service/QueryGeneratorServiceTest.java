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
    private QueryGenerator queryGenerator;

    @Before
    public void setUp() {
        gtfsFile = new File("files/rnv.zip");
        queryGenerator = new QueryGenerator(new GtfsReader(gtfsFile));
    }

    @Test
    public void generatesQueries() {
        final List<QueryDescription> generatedQueryDescriptions = queryGenerator
                        .generateQueries(10, LocalDate.now(), LocalDate.now().plusMonths(1));
        assertEquals(10, generatedQueryDescriptions.size());
    }

}
