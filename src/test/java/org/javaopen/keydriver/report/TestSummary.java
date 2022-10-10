package org.javaopen.keydriver.report;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

public class TestSummary {
    @Test
    public void testResourceInformation() {
        Summary summary = new Summary(null);
        //String resourceInformation = summary.getResourceInformation();
        //assertThat(resourceInformation, is(not(nullValue())));
    }
}
