package org.hisrc.ptbatch.pte.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.function.Consumer;

import org.hisrc.ptbatch.pte.model.StopLocationMapping;
import org.hisrc.ptbatch.pte.model.StopLocationMappingDto;

import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class StopLocationMappingCsvWriter {

    private final CsvMapper objectMapper;
    private final CsvSchema schema;
    {
        objectMapper = new CsvMapper();
        objectMapper.registerModule(new JavaTimeModule());
        schema = objectMapper.schemaFor(StopLocationMappingDto.class).withHeader();
    }

    public void write(List<StopLocationMapping> values, File file) throws IOException {
        try (final OutputStream outputStream = new FileOutputStream(file)) {
            write(values, outputStream);
        }
    }

    public void write(List<StopLocationMapping> values, final OutputStream outputStream)
                    throws IOException {
        final SequenceWriter sequenceWriter = objectMapper.writer().with(schema)
                        .writeValues(outputStream);
        for (StopLocationMapping value : values) {
            sequenceWriter.write(StopLocationMappingDto.of(value));
        }
    }

    public Consumer<StopLocationMapping> writer(File file) throws IOException {
        final SequenceWriter sequenceWriter = objectMapper.writer().with(schema).writeValues(file);
        return stopLocationMapping -> {
            try {
                sequenceWriter.write(StopLocationMappingDto.of(stopLocationMapping));
            } catch (IOException ioex) {
                throw new RuntimeException(ioex);
            }
        };
    }
}
