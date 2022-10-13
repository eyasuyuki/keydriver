package org.javaopen.keydriver.report;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class SystemInformation implements Usage {
    private OperatingSystemMXBean bean = ManagementFactory.getOperatingSystemMXBean();
    private Runtime runtime = Runtime.getRuntime();
    private File file = new File(".");
    @Override
    public String getArch() {
        return bean.getName()+" "+bean.getArch();
    }

    @Override
    public int getProcessorCount() {
        return bean.getAvailableProcessors();
    }

    @Override
    public double getLoadAverage() {
        double value = bean.getSystemLoadAverage();
        if (value < 0.0) {
            value = ((com.sun.management.OperatingSystemMXBean)bean).getSystemCpuLoad();
        }
        return value;
    }

    @Override
    public long getMaxMemory() {
        return runtime.maxMemory();
    }

    @Override
    public long getFreeMemory() {
        return runtime.freeMemory();
    }

    @Override
    public long getTotalMemory() {
        return runtime.totalMemory();
    }

    @Override
    public long getUsableDisk() {
        return file.getUsableSpace();
    }

    @Override
    public long getFreeDisk() {
        return file.getFreeSpace();
    }

    @Override
    public long getTotalDisk() {
        return file.getTotalSpace();
    }
}
