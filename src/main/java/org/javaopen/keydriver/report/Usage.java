package org.javaopen.keydriver.report;

public interface Usage {
    String getArch();
    int getProcessorCount();
    double getLoadAverage();
    long getMaxMemory();
    long getFreeMemory();
    long getTotalMemory();
    long getUsableDisk();
    long getFreeDisk();
    long getTotalDisk();
}
