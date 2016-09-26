package org.hisrc.ptbatch.pte.service;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

import org.hisrc.ptbatch.pte.model.TripDescription;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class TripDescriptionJsonWriter {

    private final ObjectMapper objectMapper = new ObjectMapper();
    {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public Consumer<TripDescription> writer(File directory) throws IOException {
        return tripDescription -> {
            directory.mkdirs();
            final File file = new File(directory, tripDescription.getQuery().getId() + ".json");
            try {
                final SequenceWriter sequenceWriter = objectMapper.writer().writeValues(file);
                sequenceWriter.write(tripDescription);
            } catch (IOException ioex) {
                throw new RuntimeException(ioex);
            }
        };
    }
}
