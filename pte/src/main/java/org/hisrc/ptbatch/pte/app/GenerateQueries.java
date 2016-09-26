package org.hisrc.ptbatch.pte.app;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.hisrc.ptbatch.args4j.spi.LocalDateOptionHandler;
import org.hisrc.ptbatch.model.QueryDescription;
import org.hisrc.ptbatch.pte.model.StopLocationMapping;
import org.hisrc.ptbatch.pte.service.QueryGenerator;
import org.hisrc.ptbatch.pte.service.StopLocationMappingCsvReader;
import org.hisrc.ptbatch.service.QueryDescriptionCsvWriter;
import org.hisrc.ptbatch.service.QueryDescriptionJsonWriter;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class GenerateQueries {

    public static class Configuration {
        @Option(name = "-?", aliases = { "-help",
                        "--help" }, help = true, usage = "Prints this help message")
        private boolean help;

        @Option(name = "-c", aliases = {
                        "--count" }, metaVar = "COUNT", usage = "Number of queries to generate, 1000 by default")
        private int count = 1000;

        @Option(name = "-s", aliases = {
                        "--start-date" }, metaVar = "START_DATE", usage = "Start date (yyyy-MM-dd) for query generation, inclusive, first day of the next month by default", handler = LocalDateOptionHandler.class)
        private LocalDate startDate = LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth());

        @Option(name = "-e", aliases = {
                        "--end-date" }, metaVar = "END_DATE", usage = "End date (yyyy-MM-dd) for query generation, inclusive, last day of the START_DATE month by default", handler = LocalDateOptionHandler.class)
        private LocalDate endDate = null;

        @Option(name = "-slm-csv", aliases = {
                        "--stop-location-mappings-csv-file" }, metaVar = "STOP_LOCATION_MAPPINGS_CSV_FILE", usage = "Stop/locations mappings CSV file, stop-location-mappings.csv by default")
        private File stopLocationMappingsCsvFile = new File("stop-location-mappings.csv");

        @Option(name = "-q-csv", aliases = {
                        "--queries-csv-file" }, metaVar = "QUERIES_CSV_FILE", usage = "Output CSV file with queries, queries.csv by default")
        private File queriesCsvFile = new File("queries.csv");

        @Option(name = "-q-json", aliases = {
                        "--queries-json-file" }, metaVar = "QUERIES_JSON_FILE", usage = "Output JSON file with queries, queries.json by default")
        private File queriesJsonFile = new File("queries.json");

        public boolean isHelp() {
            return help;
        }

        public int getCount() {
            return count;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public LocalDate getEndDate() {
            return endDate != null ? endDate : startDate.with(TemporalAdjusters.lastDayOfMonth());
        }

        public File getStopLocationMappingsCsvFile() {
            return stopLocationMappingsCsvFile;
        }

        public File getQueriesCsvFile() {
            return queriesCsvFile;
        }

        public File getQueriesJsonFile() {
            return queriesJsonFile;
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
                execute(configuration);
            }
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            printUsage(parser);
        }
    }

    private static void execute(final Configuration configuration) throws IOException {

        final File csvFile = configuration.getStopLocationMappingsCsvFile();

        final List<StopLocationMapping> stopLocationMappings;
        final StopLocationMappingCsvReader reader = new StopLocationMappingCsvReader();
        stopLocationMappings = reader.read(csvFile);

        final QueryGenerator queryGenerator = new QueryGenerator();
        final List<QueryDescription> queryDescriptions = queryGenerator.generateQueries(
                        stopLocationMappings, configuration.getCount(),
                        configuration.getStartDate(), configuration.getEndDate());

        if (configuration.getQueriesJsonFile() != null) {
            new QueryDescriptionJsonWriter().write(queryDescriptions,
                            configuration.getQueriesJsonFile());
        }
        if (configuration.getQueriesCsvFile() != null) {
            new QueryDescriptionCsvWriter().write(queryDescriptions,
                            configuration.getQueriesCsvFile());
        }
    }

    private static void printUsage(CmdLineParser parser) {
        System.out.println("Usage: java -cp pt-batch-pte.jar " + GenerateQueries.class.getName()
                        + "[options...]");
        parser.printUsage(System.out);
        System.out.println();
    }

}
