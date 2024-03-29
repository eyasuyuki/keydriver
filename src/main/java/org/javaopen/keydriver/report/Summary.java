package org.javaopen.keydriver.report;

import org.javaopen.keydriver.data.Section;
import org.javaopen.keydriver.data.TestCase;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Summary implements Report, Usage {
    private final SystemInformation information = new SystemInformation();
    private int expectingTestCount;
    private int executedTestCount;
    private int successTestCount;
    private int failedTestCount;
    private int expectingFailureCount;
    private int notExecutedTestCount;
    private Timestamp startTime;
    private Duration duration;

    public Summary(List<Section> sections) {
        if (sections == null || sections.size() < 1) {
            return;
        }
        expectingTestCount = (int)sections.stream().mapToLong(x -> x.getTestCaseList().size()).sum();
        executedTestCount = (int)sections.stream().flatMap(x -> x.getTestCaseList().stream()).filter(TestCase::isExecuted).count();
        successTestCount = (int)sections.stream().flatMap(x -> x.getTestCaseList().stream()).filter(TestCase::isSuccess).count();
        failedTestCount = (int)sections.stream().flatMap(x -> x.getTestCaseList().stream()).filter(x -> x.isExecuted() && !x.isSuccess()).count();
        expectingFailureCount = (int)sections.stream().flatMap(x -> x.getTestCaseList().stream()).filter(TestCase::isExpectingFailure).count();
        notExecutedTestCount = (int)sections.stream().flatMap(x -> x.getTestCaseList().stream()).filter(x -> !x.isExecuted()).count();
        Optional<TestCase> min = sections.stream().flatMap(x -> x.getTestCaseList().stream().filter(y -> y.getStart() != null)).min(Comparator.comparingLong(x -> x.getStart().getTime()));
        Optional<TestCase> max = sections.stream().flatMap(x -> x.getTestCaseList().stream().filter(y -> y.getEnd() != null)).max(Comparator.comparingLong(x -> x.getEnd().getTime()));
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
    public int getNotExecutedTestCount() {
        return notExecutedTestCount;
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
