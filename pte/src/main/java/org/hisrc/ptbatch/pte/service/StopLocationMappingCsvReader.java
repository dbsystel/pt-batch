package org.hisrc.ptbatch.pte.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.hisrc.ptbatch.pte.model.StopLocationMapping;
import org.hisrc.ptbatch.pte.model.StopLocationMappingDto;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class StopLocationMappingCsvReader {
    private final CsvMapper objectMapper;
    private final CsvSchema schema;
    {
        objectMapper = new CsvMapper();
        objectMapper.registerModule(new JavaTimeModule());
        schema = objectMapper.schemaFor(StopLocationMappingDto.class).withHeader();
    }

    public List<StopLocationMapping> read(final File file) throws IOException {
        try (InputStream inputStream = new FileInputStream(file)) {
            return read(inputStream);
        }
    }

    public List<StopLocationMapping> read(final InputStream inputStream) throws IOException {
        final List<StopLocationMappingDto> stopLocationMappingDtos = objectMapper.readerFor(StopLocationMappingDto.class)
                        .with(schema).<StopLocationMappingDto>readValues(inputStream).readAll();
        return stopLocationMappingDtos.stream().map(StopLocationMapping::of)
                        .collect(Collectors.toList());
    }

}
