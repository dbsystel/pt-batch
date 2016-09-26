package org.hisrc.ptbatch.pte.app;

import java.io.IOException;

public class GenerateStopLocationMappingsRun {

    public static void main(String[] args) throws IOException {
        
        final String[] arguments = new String[]{
                        "-g", "files/rnv.zip",
                        "-csv", "files/rnv-stop-location-mappings.csv"
        };
        GenerateStopLocationMappings.main(arguments);
    }
}
