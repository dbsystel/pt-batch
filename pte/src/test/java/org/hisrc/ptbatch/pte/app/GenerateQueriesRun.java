package org.hisrc.ptbatch.pte.app;

import java.io.IOException;

public class GenerateQueriesRun {

    public static void main(String[] args) throws IOException {
        
        final String[] arguments = new String[]{
                        "-c", "1000",
                        "-s", "2016-10-01",
                        "-e", "2016-10-31",
                        "-slm-csv", "files/rnv-stop-location-mappings.csv",
                        "-q-csv", "files/rnv-queries.csv",
                        "-q-json", "files/rnv-queries.json"
        };
        GenerateQueries.main(arguments);
    }
}
