package org.hisrc.ptbatch.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.hisrc.ptbatch.model.QueryDescription;
import org.hisrc.ptbatch.model.QueryDescriptionDto;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class QueryDescriptionCsvReader {

    private final CsvMapper objectMapper;
    private final CsvSchema schema;
    {
        objectMapper = new CsvMapper();
        objectMapper.registerModule(new JavaTimeModule());
        schema = objectMapper.schemaFor(QueryDescriptionDto.class).withHeader();
    }

    public List<QueryDescription> read(final File file) throws IOException {
        try (InputStream inputStream = new FileInputStream(file)) {
            return read(inputStream);
        }
    }

    public List<QueryDescription> read(final InputStream inputStream) throws IOException {
        final List<QueryDescriptionDto> stopLocationMappingDtos = objectMapper
                        .readerFor(QueryDescriptionDto.class).with(schema)
                        .<QueryDescriptionDto>readValues(inputStream).readAll();
        return stopLocationMappingDtos.stream().map(QueryDescription::of)
                        .collect(Collectors.toList());
    }
}
