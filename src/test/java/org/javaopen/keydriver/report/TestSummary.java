package org.javaopen.keydriver.report;

import org.javaopen.keydriver.data.DummyTestList;
import org.javaopen.keydriver.data.Keyword;
import org.javaopen.keydriver.data.Matches;
import org.javaopen.keydriver.data.Section;
import org.javaopen.keydriver.data.TestCase;
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
    private DummyTestList dummyTests;
    private Summary summary;
    private int testCount;
    @Before
    public void setUp() {
        Locale.setDefault(Locale.US);

        context = Context.getContext(null, null, null);
        dummyTests = new DummyTestList(context);
        for (TestCase t: dummyTests.getTests()) {
            if (t.getKeyword().equals(Keyword.ASSERT) && t.getArgument().getTag().equals(Matches.FAIL)) {
                expectingFailure(t);
            } else {
                success(t);
            }
        }
        Section section = new Section("Section1");
        section.getTestCaseList().addAll(dummyTests.getTests());
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
        Optional<TestCase> min = dummyTests.getTests().stream()
            .min(Comparator.comparingLong(x -> x.getStart().getTime()));
        assertThat(min.isPresent(), is(true));
        Timestamp start = min.get().getStart();
        assertThat(summary.getStartTime(), is(start));
    }

    @Test
    public void testGetDuration() {
        Optional<TestCase> min = dummyTests.getTests().stream()
            .min(Comparator.comparingLong(x -> x.getStart().getTime()));
        Optional<TestCase> max = dummyTests.getTests().stream()
            .max(Comparator.comparingLong(x -> x.getEnd().getTime()));
        assertThat(min.isPresent(), is(true));
        assertThat(max.isPresent(), is(true));
        Duration duration = Duration.between(min.get().getStart().toInstant(), max.get().getEnd().toInstant());
        assertThat(summary.getDuration(), is(duration));
    }

    private void setStartEnd(TestCase testCase) {
        testCase.setStart(new Timestamp(System.currentTimeMillis()));
        Duration duration = Duration.ofMillis((long)(Math.random() * 5000.0));
        testCase.setEnd(Timestamp.from(Instant.from(duration.addTo(testCase.getStart().toInstant()))));
    }

    private void success(TestCase testCase) {
        setStartEnd(testCase);
        testCase.setExecuted(true);
        testCase.setSuccess(true);
    }

    private void expectingFailure(TestCase testCase) {
        setStartEnd(testCase);
        testCase.setExecuted(true);
        testCase.setSuccess(false);
        testCase.setExpectingFailure(true);
        testCase.setMatchFailed("dummy");
        testCase.setExpected("dummy");
        testCase.setActual("wrong");
        testCase.setStackTrace("dummy");
    }
}
