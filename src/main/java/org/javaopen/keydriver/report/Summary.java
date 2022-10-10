package org.javaopen.keydriver.report;

import org.apache.commons.math3.stat.descriptive.summary.Sum;
import org.javaopen.keydriver.data.Section;
import org.javaopen.keydriver.data.Test;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Summary implements Report, Usage {
    private SystemInformation information = new SystemInformation();
    private int expectingTestCount;
    private int executedTestCount;
    private int successTestCount;
    private int failedTestCount;
    private int expectingFailureCount;
    private int uncompletedTestCount;
    private Timestamp startTime;
    private long expectingTime;
    private long duration;

    public Summary(List<Section> sections) {
        Stream<Test> stream = sections.stream().flatMap(x -> x.getTests().stream());
        expectingTestCount = (int)stream.count();
        // TODO executedTestCount
        successTestCount = (int)stream.filter(x -> x.isSuccess()).count();
        failedTestCount = (int)stream.filter(x -> !x.isSuccess()).count();
        expectingFailureCount = (int)stream.filter(x -> x.isExpectingFailure()).count();
        uncompletedTestCount = (int)stream.filter(x -> !x.isCompleted()).count();
        Comparator<Test> minCondition = (x1, x2) -> Long.compare(x1.getStart().getTime(), x2.getStart().getTime());
        Optional<Test> min = stream.min(minCondition);
        Comparator<Test> maxCondition = (x1, x2) -> Long.compare(x2.getEnd().getTime(), x1.getEnd().getTime());
        Optional<Test> max = stream.max(maxCondition);
        if (min.isPresent()) {
            startTime = min.get().getStart();
        }
        // TODO expecting time
        if (min.isPresent() && max.isPresent()) {
            duration = max.get().getEnd().getTime() - startTime.getTime();
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
        return null;
    }

    @Override
    public long getExpectingTime() {
        return 0;
    }

    @Override
    public long getDuration() {
        return 0;
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
