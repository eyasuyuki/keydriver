package org.javaopen.keydriver.report;

import org.apache.commons.configuration2.convert.PropertyConverter;
import org.javaopen.keydriver.data.Section;
import org.javaopen.keydriver.data.Test;
import org.javaopen.keydriver.driver.Context;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Summary implements Report, Usage {
    private final SystemInformation information = new SystemInformation();
    private final int expectingTestCount;
    private final int executedTestCount;
    private final int successTestCount;
    private final int failedTestCount;
    private final int expectingFailureCount;
    private final int uncompletedTestCount;
    private Timestamp startTime;
    private Duration duration;

    public Summary(Context context, List<Section> sections) {
        expectingTestCount = (int)sections.stream().mapToLong(x -> x.getTests().size()).sum();
        executedTestCount = (int)sections.stream().flatMap(x -> x.getTests().stream()).filter(Test::isExecuted).count();
        successTestCount = (int)sections.stream().flatMap(x -> x.getTests().stream()).filter(Test::isSuccess).count();
        failedTestCount = (int)sections.stream().flatMap(x -> x.getTests().stream()).filter(x -> !x.isSuccess()).count();
        expectingFailureCount = (int)sections.stream().flatMap(x -> x.getTests().stream()).filter(Test::isExpectingFailure).count();
        uncompletedTestCount = (int)sections.stream().flatMap(x -> x.getTests().stream()).filter(x -> !x.isCompleted()).count();
        Optional<Test> min = sections.stream().flatMap(x -> x.getTests().stream()).min(Comparator.comparingLong(x -> x.getStart().getTime()));
        Optional<Test> max = sections.stream().flatMap(x -> x.getTests().stream()).max(Comparator.comparingLong(x -> x.getEnd().getTime()));
        min.ifPresent(test -> startTime = test.getStart());

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
