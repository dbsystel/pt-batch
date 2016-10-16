package org.hisrc.ptbatch.pte.service;

import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.NANO_OF_SECOND;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;

import java.io.File;
import java.io.IOException;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.util.function.Consumer;

import org.hisrc.ptbatch.pte.model.TripDescription;
import org.hisrc.ptbatch.pte.util.FilenameUtils;

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

            final String fromName = tripDescription.getQuery().getFrom().getName();
            final String toName = tripDescription.getQuery().getTo().getName();
            final String unsafeName = tripDescription.getQuery().getId() + "-"
                            + tripDescription.getQuery().getDateTime()
                                            .format(LOCAL_DATE_TIME_FORMATTER) + "-"
                            + (fromName == null ? "unknown" : fromName) + "-"
                            + (toName == null ? "unknown" : toName);
            final String safeName = FilenameUtils.toFileSystemSafeName(unsafeName, false, 255);

            final File dir = new File(directory, safeName);
            dir.mkdirs();
            try {
                objectMapper.writer().writeValues(new File(dir, "query.json"))
                                .write(tripDescription.getQuery());
                objectMapper.writer().writeValues(new File(dir, "leastDurationTrip.json"))
                                .write(tripDescription.getLeastDurationTrip());
                objectMapper.writer().writeValues(new File(dir, "leastChangesTrip.json"))
                                .write(tripDescription.getLeastChangesTrip());
            } catch (IOException ioex) {
                throw new RuntimeException(ioex);
            }
        };
    }

}
