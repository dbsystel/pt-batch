package org.hisrc.ptbatch.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.hisrc.ptbatch.model.QueryDescription;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class QueryDescriptionJsonWriter {

    private final ObjectMapper objectMapper = new ObjectMapper();
    {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void write(List<QueryDescription> queryDescriptions, File file) throws IOException {
        try (final OutputStream os = new FileOutputStream(file)) {
            write(queryDescriptions, os);
        }
    }

    public void write(List<QueryDescription> queryDescriptions, final OutputStream outputStream)
                    throws IOException {
        final SequenceWriter writeValues = objectMapper.writer().writeValuesAsArray(outputStream);
        writeValues.writeAll(queryDescriptions);
    }
}
