package org.hisrc.ptbatch.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.hisrc.ptbatch.model.QueryDescription;
import org.hisrc.ptbatch.model.QueryDescriptionDto;

import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class QueryDescriptionCsvWriter {

    private final CsvMapper objectMapper;
    private final CsvSchema schema;
    {
        objectMapper = new CsvMapper();
        objectMapper.registerModule(new JavaTimeModule());
        schema = objectMapper.schemaFor(QueryDescriptionDto.class).withHeader();
    }

    public void write(List<QueryDescription> queryDescriptions, File file) throws IOException {
        try (final OutputStream outputStream = new FileOutputStream(file)) {
            write(queryDescriptions, outputStream);
        }
    }

    public void write(List<QueryDescription> queryDescriptions, final OutputStream outputStream)
                    throws IOException {
        final SequenceWriter sequenceWriter = objectMapper.writer().with(schema).writeValues(outputStream);
        for (QueryDescription queryDescription : queryDescriptions)
        {
            sequenceWriter.write(QueryDescriptionDto.of(queryDescription));
        }
    }
}
