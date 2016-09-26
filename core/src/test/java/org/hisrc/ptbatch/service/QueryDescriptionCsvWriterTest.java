package org.hisrc.ptbatch.service;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.output.WriterOutputStream;
import org.hisrc.ptbatch.model.StopDescription;
import org.hisrc.ptbatch.model.QueryDescription;
import org.junit.Before;
import org.junit.Test;

public class QueryDescriptionCsvWriterTest {

    private QueryDescriptionCsvWriter queryDescriptionCsvWriter;

    @Before
    public void setUp() {
        queryDescriptionCsvWriter = new QueryDescriptionCsvWriter();
    }

    @Test
    public void writesQueryDescription() throws IOException {
        final StringWriter sw = new StringWriter();
        try (final OutputStream os = new WriterOutputStream(sw)) {
            final List<QueryDescription> queryDescriptions = Collections.singletonList(
                            new QueryDescription(LocalDateTime.of(2016, 9, 26, 10, 15, 0),
                                            new StopDescription("a", "A", 10, 20),
                                            new StopDescription("b", "B", 30, 40)));
            queryDescriptionCsvWriter.write(queryDescriptions, os);
        }
        final String result = sw.toString();
        // System.out.println(result);
        assertEquals(134, result.length());
    }

}
