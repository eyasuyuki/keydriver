package org.javaopen.keydriver.report;

import org.javaopen.keydriver.data.DummyTests;
import org.javaopen.keydriver.data.Keyword;
import org.javaopen.keydriver.data.Matches;
import org.javaopen.keydriver.data.Section;
import org.javaopen.keydriver.driver.Context;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TestSummary {
    private Context context;
    private DummyTests dummyTests;
    private Summary summary;
    private int testCount;
    @Before
    public void setUp() {
        Locale.setDefault(Locale.US);

        context = Context.getContext(null, null, null);
        dummyTests = new DummyTests(context);
        for (org.javaopen.keydriver.data.Test t: dummyTests.getTests()) {
            if (t.getKeyword().equals(Keyword.ASSERT) && t.getArgument().getTag().equals(Matches.FAIL)) {
                expectingFailure(t);
            } else {
                success(t);
            }
        }
        Section section = new Section("Section1");
        section.getTests().addAll(dummyTests.getTests());
        summary = new Summary(Arrays.asList(section));
        testCount = dummyTests.getTests().size();
    }

    @Test
    public void testGetExpectingTestCount() {
        assertThat(summary.getExpectingTestCount(), is(testCount));
    }
    @Test
    public void testGetExecutedTestCount() {
        assertThat(summary.getExecutedTestCount(), is(testCount));
    }
    @Test
    public void testGetSucceedTestCount() {
        assertThat(summary.getExecutedTestCount(), is(testCount));
    }
    @Test
    public void testGetFailedTestCount() {
        assertThat(summary.getFailedTestCount(), is(1));
    }
    @Test
    public void testGetExpectingFailureCount() {
        assertThat(summary.getExpectingFailureCount(), is(1));
    }
    @Test
    public void testGetNotExecutedTestCount() {
        assertThat(summary.getNotExecutedTestCount(), is(0));
    }
    @Test
    public void testGetStartTime() {
        Optional<org.javaopen.keydriver.data.Test> min = dummyTests.getTests().stream()
            .min(Comparator.comparingLong(x -> x.getStart().getTime()));
        assertThat(min.isPresent(), is(true));
        Timestamp start = min.get().getStart();
        assertThat(summary.getStartTime(), is(start));
    }

    @Test
    public void testGetDuration() {
        Optional<org.javaopen.keydriver.data.Test> min = dummyTests.getTests().stream()
            .min(Comparator.comparingLong(x -> x.getStart().getTime()));
        Optional<org.javaopen.keydriver.data.Test> max = dummyTests.getTests().stream()
            .max(Comparator.comparingLong(x -> x.getEnd().getTime()));
        assertThat(min.isPresent(), is(true));
        assertThat(max.isPresent(), is(true));
        Duration duration = Duration.between(min.get().getStart().toInstant(), max.get().getEnd().toInstant());
        assertThat(summary.getDuration(), is(duration));
    }

    private void setStartEnd(org.javaopen.keydriver.data.Test test) {
        test.setStart(new Timestamp(System.currentTimeMillis()));
        Duration duration = Duration.ofMillis((long)(Math.random() * 5000.0));
        test.setEnd(Timestamp.from(Instant.from(duration.addTo(test.getStart().toInstant()))));
    }

    private void success(org.javaopen.keydriver.data.Test test) {
        setStartEnd(test);
        test.setExecuted(true);
        test.setSuccess(true);
    }

    private void expectingFailure(org.javaopen.keydriver.data.Test test) {
        setStartEnd(test);
        test.setExecuted(true);
        test.setSuccess(false);
        test.setExpectingFailure(true);
        test.setMatchFailed("dummy");
        test.setExpected("dummy");
        test.setActual("wrong");
        test.setStackTrace("dummy");
    }
}
