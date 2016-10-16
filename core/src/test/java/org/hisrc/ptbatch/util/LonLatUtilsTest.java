package org.hisrc.ptbatch.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LonLatUtilsTest {

    @Test
    public void calculatesDistance() {

        assertEquals(6679169, LonLatUtils.distance(0, 0, 45, 45), 1);

    }

}
