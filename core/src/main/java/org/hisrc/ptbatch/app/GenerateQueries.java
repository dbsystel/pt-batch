package org.hisrc.ptbatch.app;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.hisrc.ptbatch.args4j.spi.LocalDateOptionHandler;
import org.hisrc.ptbatch.model.QueryDescription;
import org.hisrc.ptbatch.service.GtfsService;
import org.hisrc.ptbatch.service.QueryDescriptionCsvWriter;
import org.hisrc.ptbatch.service.QueryDescriptionJsonWriter;
import org.hisrc.ptbatch.service.QueryGeneratorService;
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

        @Option(name = "-g", aliases = {
                        "--gtfs-file" }, metaVar = "GTFS_FILE", usage = "GTFS file containing stops, gtfs.zip by default")
        private File gtfsFile = new File("gtfs.zip");

        @Option(name = "-s", aliases = {
                        "--start-date" }, metaVar = "START_DATE", usage = "Start date (yyyy-MM-dd) for query generation, inclusive, first day of the next month by default", handler = LocalDateOptionHandler.class)
        private LocalDate startDate = LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth());

        @Option(name = "-e", aliases = {
                        "--end-date" }, metaVar = "END_DATE", usage = "End date (yyyy-MM-dd) for query generation, inclusive, last day of the START_DATE month by default", handler = LocalDateOptionHandler.class)
        private LocalDate endDate = null;

        @Option(name = "-csv", aliases = {
                        "--csv-file" }, metaVar = "CSV_FILE", usage = "Output CSV file, queries.csv by default")
        private File csvFile = new File("queries.csv");

        @Option(name = "-json", aliases = {
                        "--json-file" }, metaVar = "JSON_FILE", usage = "Output JSON file, queries.json by default")
        private File jsonFile = new File("queries.json");

        public boolean isHelp() {
            return help;
        }

        public int getCount() {
            return count;
        }

        public File getGtfsFile() {
            return gtfsFile;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public LocalDate getEndDate() {
            return endDate != null ? endDate : startDate.with(TemporalAdjusters.lastDayOfMonth());
        }

        public File getCsvFile() {
            return csvFile;
        }

        public File getJsonFile() {
            return jsonFile;
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
                final GtfsService gtfsReader = new GtfsService(configuration.getGtfsFile());
                final QueryGeneratorService queryGenerator = new QueryGeneratorService(gtfsReader);
                final List<QueryDescription> queryDescriptions = queryGenerator.generateQueries(
                                configuration.getCount(), configuration.getStartDate(),
                                configuration.getEndDate());

                if (configuration.getJsonFile() != null) {
                    new QueryDescriptionJsonWriter().write(queryDescriptions,
                                    configuration.getJsonFile());
                }
                if (configuration.getCsvFile() != null) {
                    new QueryDescriptionCsvWriter().write(queryDescriptions,
                                    configuration.getCsvFile());
                }
            }
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            printUsage(parser);
        }
    }

    private static void printUsage(CmdLineParser parser) {
        System.out.println("Usage: java -cp pt-batch.jar " + GenerateQueries.class.getName()
                        + "[options...]");
        parser.printUsage(System.out);
        System.out.println();
    }

}
