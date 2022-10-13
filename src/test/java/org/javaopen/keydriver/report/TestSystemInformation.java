package org.javaopen.keydriver.report;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

public class TestSystemInformation {
    private Usage usage;
    
    @Before
    public void setUp() {
        usage = new SystemInformation();
    }

    @Test
    public void testGetArch() {
        assertThat(usage.getArch(), is(not(emptyString())));
    }

    @Test
    public void testGetProcessorCount() {
        assertThat(usage.getProcessorCount(), is(greaterThanOrEqualTo(1)));
    }

    @Test
    public void testGetLoadAverage() {
        assertThat(usage.getLoadAverage(), is(greaterThanOrEqualTo(0.0)));
    }

    @Test
    public void testGetMaxMemory() {
        assertThat(usage.getMaxMemory(), is(greaterThan(0L)));
    }

    @Test
    public void testGetFreeMemory() {
        assertThat(usage.getFreeMemory(), is(greaterThan(0L)));
    }

    @Test
    public void testGetTotalMemory() {
        assertThat(usage.getTotalMemory(), is(greaterThan(0L)));
    }

    @Test
    public void testGetUsableDisk() {
        assertThat(usage.getUsableDisk(), is(greaterThan(0L)));
    }

    @Test
    public void testGetFreeDisk() {
        assertThat(usage.getFreeDisk(), is(greaterThan(0L)));
    }

    @Test
    public void testGetTotalDisk() {
        assertThat(usage.getTotalDisk(), is(greaterThan(0L)));
    }
    
}
