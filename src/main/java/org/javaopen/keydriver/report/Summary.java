package org.javaopen.keydriver.report;

import org.apache.commons.configuration2.convert.PropertyConverter;
import org.apache.commons.math3.stat.descriptive.summary.Sum;
import org.javaopen.keydriver.data.Section;
import org.javaopen.keydriver.data.Test;
import org.javaopen.keydriver.driver.Context;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Summary implements Report, Usage {
    public static final String EXPECTING_TIME_KEY = "expecting_time";
    private SystemInformation information = new SystemInformation();
    private int expectingTestCount;
    private int executedTestCount;
    private int successTestCount;
    private int failedTestCount;
    private int expectingFailureCount;
    private int uncompletedTestCount;
    private Timestamp startTime;
    private Duration expectingTime;
    private Duration duration;

    public Summary(Context context, List<Section> sections) {
        expectingTestCount = (int)sections.stream().flatMap(x -> x.getTests().stream()).count();
        executedTestCount = (int)sections.stream().flatMap(x -> x.getTests().stream()).filter(x -> x.isExecuted()).count();
        successTestCount = (int)sections.stream().flatMap(x -> x.getTests().stream()).filter(x -> x.isSuccess()).count();
        failedTestCount = (int)sections.stream().flatMap(x -> x.getTests().stream()).filter(x -> !x.isSuccess()).count();
        expectingFailureCount = (int)sections.stream().flatMap(x -> x.getTests().stream()).filter(x -> x.isExpectingFailure()).count();
        uncompletedTestCount = (int)sections.stream().flatMap(x -> x.getTests().stream()).filter(x -> !x.isCompleted()).count();
        Optional<Test> min = sections.stream().flatMap(x -> x.getTests().stream()).min(Comparator.comparingLong(x -> x.getStart().getTime()));
        Optional<Test> max = sections.stream().flatMap(x -> x.getTests().stream()).max(Comparator.comparingLong(x -> x.getEnd().getTime()));
        if (min.isPresent()) {
            startTime = min.get().getStart();
        }
        double ex = PropertyConverter.toDouble(context.getBundle().getObject(EXPECTING_TIME_KEY));
        double exTime = (double)expectingTestCount * ex;
        expectingTime = Duration.ofSeconds((long)exTime);

        if (min.isPresent() && max.isPresent()) {
            duration = Duration.between(startTime.toInstant(), max.get().getEnd().toInstant());
        }

    }

    @Override
    public int getExpectingTestCount() {
        return expectingTestCount;
    }

    @Override
    public int getExecutedTestCount() {
        return executedTestCount;
    }

    @Override
    public int getSucceedTestCount() {
        return successTestCount;
    }

    @Override
    public int getFailedTestCount() {
        return failedTestCount;
    }

    @Override
    public int getExpectingFailureCount() {
        return expectingFailureCount;
    }

    @Override
    public int getUncompletedTestCount() {
        return uncompletedTestCount;
    }

    @Override
    public Timestamp getStartTime() {
        return startTime;
    }

    @Override
    public Duration getExpectingTime() {
        return expectingTime;
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public String getArch() {
        return information.getArch();
    }

    @Override
    public int getProcessorCount() {
        return information.getProcessorCount();
    }

    @Override
    public double getLoadAverage() {
        return information.getLoadAverage();
    }

    @Override
    public long getMaxMemory() {
        return information.getMaxMemory();
    }

    @Override
    public long getFreeMemory() {
        return information.getFreeMemory();
    }

    @Override
    public long getTotalMemory() {
        return information.getTotalMemory();
    }

    @Override
    public long getUsableDisk() {
        return information.getUsableDisk();
    }

    @Override
    public long getFreeDisk() {
        return information.getFreeDisk();
    }

    @Override
    public long getTotalDisk() {
        return information.getTotalDisk();
    }
}
