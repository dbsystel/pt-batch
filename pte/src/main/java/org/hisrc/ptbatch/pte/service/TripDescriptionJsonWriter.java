package org.hisrc.ptbatch.pte.service;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

import org.hisrc.ptbatch.pte.model.TripDescription;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class TripDescriptionJsonWriter {

    private final ObjectMapper objectMapper = new ObjectMapper();
    {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static final DateTimeFormatter LOCAL_DATE_TIME_FORMATTER = DateTimeFormatter
                    .ofPattern("yyyyMMdd-HHmm");

    public Consumer<TripDescription> writer(File directory) throws IOException {
        return tripDescription -> {

            final String directoryName = tripDescription.getQuery().getId();

            final File dir = new File(directory, directoryName);
            dir.mkdirs();
            try {
                objectMapper.writer().writeValues(new File(dir, "query.json"))
                                .write(tripDescription.getQuery());
                objectMapper.writer().writeValues(new File(dir, "trip.json"))
                                .write(tripDescription.getTrip());
            } catch (IOException ioex) {
                throw new RuntimeException(ioex);
            }
        };
    }

}
