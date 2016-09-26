package org.hisrc.ptbatch.pte.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.function.Consumer;

import org.hisrc.ptbatch.pte.model.BriefTripDescriptionDto;
import org.hisrc.ptbatch.pte.model.TripDescription;

import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class TripDescriptionCsvWriter {

    private final CsvMapper objectMapper;
    private final CsvSchema schema;
    {
        objectMapper = new CsvMapper();
        objectMapper.registerModule(new JavaTimeModule());
        schema = objectMapper.schemaFor(BriefTripDescriptionDto.class).withHeader();
    }

    public void write(List<TripDescription> values, File file) throws IOException {
        try (final OutputStream outputStream = new FileOutputStream(file)) {
            write(values, outputStream);
        }
    }

    public void write(List<TripDescription> values, final OutputStream outputStream)
                    throws IOException {
        final SequenceWriter sequenceWriter = objectMapper.writer().with(schema)
                        .writeValues(outputStream);
        for (TripDescription value : values) {
            sequenceWriter.write(BriefTripDescriptionDto.of(value));
        }
    }

    public Consumer<TripDescription> writer(File file) throws IOException {
        final SequenceWriter sequenceWriter = objectMapper.writer().with(schema).writeValues(file);
        return tripDescription -> {
            try {
                sequenceWriter.write(BriefTripDescriptionDto.of(tripDescription));
            } catch (IOException ioex) {
                throw new RuntimeException(ioex);
            }
        };
    }
}
