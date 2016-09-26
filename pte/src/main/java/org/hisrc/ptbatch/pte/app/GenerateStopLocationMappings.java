package org.hisrc.ptbatch.pte.app;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import org.hisrc.ptbatch.model.StopDescription;
import org.hisrc.ptbatch.pte.model.StopLocationMapping;
import org.hisrc.ptbatch.pte.service.BahnProviderService;
import org.hisrc.ptbatch.pte.service.StopLocationMappingCsvReader;
import org.hisrc.ptbatch.pte.service.StopLocationMappingCsvWriter;
import org.hisrc.ptbatch.service.GtfsReader;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.onebusaway.gtfs.model.Stop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenerateStopLocationMappings {

    private static final Logger LOGGER = LoggerFactory
                    .getLogger(GenerateStopLocationMappings.class);

    public static class Configuration {
        @Option(name = "-?", aliases = { "-help",
                        "--help" }, help = true, usage = "Prints this help message")
        private boolean help;

        @Option(name = "-g", aliases = {
                        "--gtfs-file" }, metaVar = "GTFS_FILE", usage = "GTFS file containing stops, gtfs.zip by default")
        private File gtfsFile = new File("gtfs.zip");

        @Option(name = "-slm-csv", aliases = {
                        "--stop-location-mappings-csv-file" }, metaVar = "STOP_LOCATION_MAPPINGS_CSV_FILE", usage = "Stop/locations mappings CSV file, stop-location-mappings.csv by default")
        private File stopLocationMappingsCsvFile = new File("stop-location-mappings.csv");

        public boolean isHelp() {
            return help;
        }

        public File getGtfsFile() {
            return gtfsFile;
        }

        public File getStopLocationMappingsCsvFile() {
            return stopLocationMappingsCsvFile;
        }

    }

    public static void main(String[] args) throws IOException {
        final Configuration configuration = new Configuration();
        CmdLineParser parser = new CmdLineParser(configuration);
        try {
            parser.parseArgument(args);
            if (configuration.isHelp()) {
                printUsage(parser);
            } else {
                final GtfsReader gtfsReader = new GtfsReader(configuration.getGtfsFile());
                final Collection<Stop> stops = gtfsReader.getStops();
                final BahnProviderService providerService = new BahnProviderService();

                final File csvFile = configuration.getStopLocationMappingsCsvFile();

                final List<StopLocationMapping> existingStopLocationMappings;
                final Set<StopDescription> resolvedStops = new HashSet<>();
                if (csvFile.exists()) {
                    final StopLocationMappingCsvReader reader = new StopLocationMappingCsvReader();
                    existingStopLocationMappings = reader.read(csvFile);
                    existingStopLocationMappings.stream().map(StopLocationMapping::getStop)
                                    .forEach(resolvedStops::add);
                } else {
                    existingStopLocationMappings = Collections.emptyList();
                }
                final Consumer<StopLocationMapping> writeStopLocationMapping = new StopLocationMappingCsvWriter()
                                .writer(csvFile);
                existingStopLocationMappings.forEach(writeStopLocationMapping);

                stops.stream().map(StopDescription::of)
                                .filter(stopDescription -> !resolvedStops.contains(stopDescription))
                                .map(stopDescription -> {
                                    try {
                                        final StopLocationMapping mapping = providerService
                                                        .resolveStop(stopDescription);
                                        if (mapping == null) {
                                            LOGGER.warn("Could not find location for the stop [{}].",
                                                            stopDescription);
                                            return null;
                                        } else {
                                            return mapping;
                                        }
                                    } catch (IOException ioex) {
                                        LOGGER.warn("Error resolving location for stop [{}].",
                                                        stopDescription, ioex);
                                        return null;
                                    }
                                }).filter(Objects::nonNull).forEach(writeStopLocationMapping);
            }
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            printUsage(parser);
        }
    }

    private static void printUsage(CmdLineParser parser) {
        System.out.println("Usage: java -cp pt-batch-pte.jar "
                        + GenerateStopLocationMappings.class.getName() + "[options...]");
        parser.printUsage(System.out);
        System.out.println();
    }

}
