package org.hisrc.ptbatch.app;

import java.io.IOException;

import org.junit.Test;

public class GenerateQueriesTest {
    
    @Test
    public void generateQueriesForRnv() throws IOException
    {
        final String[] args = new String[]{
                        "-c", "10",
                        "-g", "files/rnv.zip",
                        "-s", "2016-10-01",
                        "-e", "2016-10-31",
                        "-csv", "files/rnv-queries.csv",
                        "-json", "files/rnv-queries.json"
        };
        GenerateQueries.main(args);
    }

}
