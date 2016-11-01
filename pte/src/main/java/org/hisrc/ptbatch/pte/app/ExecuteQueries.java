package org.hisrc.ptbatch.pte.app;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.hisrc.ptbatch.model.Optimization;
import org.hisrc.ptbatch.model.QueryDescription;
import org.hisrc.ptbatch.model.StopDescription;
import org.hisrc.ptbatch.pte.model.StopLocationMapping;
import org.hisrc.ptbatch.pte.model.TripDescription;
import org.hisrc.ptbatch.pte.service.BahnProviderService;
import org.hisrc.ptbatch.pte.service.StopLocationMappingCsvReader;
import org.hisrc.ptbatch.pte.service.TripDescriptionCsvWriter;
import org.hisrc.ptbatch.pte.service.TripDescriptionJsonWriter;
import org.hisrc.ptbatch.service.QueryDescriptionCsvReader;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.Trip;

public class ExecuteQueries {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecuteQueries.class);

    public static class Configuration {
        @Option(name = "-?", aliases = { "-help",
                        "--help" }, help = true, usage = "Prints this help message")
        private boolean help;

        @Option(name = "-slm-csv", aliases = {
                        "--stop-location-mappings-csv-file" }, metaVar = "STOP_LOCATION_MAPPINGS_CSV_FILE", usage = "Stop/locations mappings CSV file, stop-location-mappings.csv by default")
        private File stopLocationMappingsCsvFile = new File("stop-location-mappings.csv");

        @Option(name = "-q-csv", aliases = {
                        "--queries-csv-file" }, metaVar = "QUERIES_CSV_FILE", usage = "Output CSV file with queries, queries.csv by default")
        private File queriesCsvFile = new File("queries.csv");

        @Option(name = "-t-csv", aliases = {
                        "--trips-csv-file" }, metaVar = "TRIPS_CSV_FILE", usage = "Output CSV file with trips, trips.csv by default")
        private File tripsCsvFile = new File("trips.csv");

        @Option(name = "-t-json", aliases = {
                        "--trips-json-directory" }, metaVar = "TRIPS_CSV_FILE", usage = "Output for JSON files of trips, current directory by default")
        private File tripsJsonDirectory = new File(".");

        public boolean isHelp() {
            return help;
        }

        public File getStopLocationMappingsCsvFile() {
            return stopLocationMappingsCsvFile;
        }

        public File getQueriesCsvFile() {
            return queriesCsvFile;
        }

        public File getTripsCsvFile() {
            return tripsCsvFile;
        }

        public File getTripsJsonDirectory() {
            return tripsJsonDirectory;
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

                final File stopLocationMappingsCsvFile = configuration
                                .getStopLocationMappingsCsvFile();
                final StopLocationMappingCsvReader stopLocationMappingCsvReader = new StopLocationMappingCsvReader();
                final List<StopLocationMapping> stopLocationMappings = stopLocationMappingCsvReader
                                .read(stopLocationMappingsCsvFile);

                final Map<StopDescription, Location> stopLocationMap = stopLocationMappings.stream()
                                .collect(Collectors.toMap(StopLocationMapping::getStop,
                                                StopLocationMapping::getLocation));

                final File queriesCsvFile = configuration.getQueriesCsvFile();
                final QueryDescriptionCsvReader queryDescriptionCsvReader = new QueryDescriptionCsvReader();
                final List<QueryDescription> queryDescriptions = queryDescriptionCsvReader
                                .read(queriesCsvFile);

                final BahnProviderService providerService = new BahnProviderService();

                final TripDescriptionCsvWriter tripDescriptionCsvWriter = new TripDescriptionCsvWriter();
                final TripDescriptionJsonWriter tripDescriptionJsonWriter = new TripDescriptionJsonWriter();
                final Consumer<TripDescription> csvWriter = tripDescriptionCsvWriter
                                .writer(configuration.getTripsCsvFile());
                
                final Consumer<TripDescription> jsonWriter = tripDescriptionJsonWriter
                                .writer(configuration.getTripsJsonDirectory());
                
                final Consumer<TripDescription> writers = tripDescription -> {
                    csvWriter.accept(tripDescription);
                    jsonWriter.accept(tripDescription);
                };

                queryDescriptions.stream().map(queryDescription -> {

                    final LocalDateTime dateTime = queryDescription.getDateTime();
                    final Location from = stopLocationMap.get(queryDescription.getFrom());
                    final Location to = stopLocationMap.get(queryDescription.getTo());
                    final Optimization optimization = queryDescription.getOptimization();
                    if (from == null) {
                        LOGGER.warn("Could not find \"from\" location [{}].",
                                        queryDescription.getFrom());
                        return null;
                    }
                    if (to == null) {
                        LOGGER.warn("Could not find \"to\" location [{}].",
                                        queryDescription.getTo());
                        return null;
                    }

                    try {
                        final Trip trip = providerService.findTrip(dateTime,
                                        from, to, optimization);
                        if (trip == null) {
                            LOGGER.warn("Could not find connection between [{}] and [{}] on [{}] optimized by [{}].",
                                            from, to, dateTime, optimization);
                            return null;
                        }
                         return new TripDescription(queryDescription, trip);
                        
                    } catch (IOException ioex) {
                        LOGGER.warn("Error querying for connection between [{}] and [{}] on [{}].",
                                        from, to, dateTime, ioex);
                        return null;
                    }
                }).filter(Objects::nonNull).forEach(writers);

            }
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            printUsage(parser);
        }
    }

    private static void printUsage(CmdLineParser parser) {
        System.out.println("Usage: java -cp pt-batch-pte.jar " + ExecuteQueries.class.getName()
                        + "[options...]");
        parser.printUsage(System.out);
        System.out.println();
    }

}
