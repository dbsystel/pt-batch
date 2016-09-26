package org.hisrc.ptbatch.pte.app;

import java.io.IOException;

public class ExecuteQueriesRun {

    public static void main(String[] args) throws IOException {
        
        final String[] arguments = new String[]{
                        "-slm-csv", "files/rnv-stop-location-mappings.csv",
                        "-q-csv", "files/rnv-queries.csv",
                        "-t-csv", "files/rnv-trips.csv",
                        "-t-json", "files/trips"
        };
        ExecuteQueries.main(arguments);
    }
}
