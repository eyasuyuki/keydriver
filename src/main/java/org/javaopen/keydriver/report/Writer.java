package org.javaopen.keydriver.report;

import org.javaopen.keydriver.driver.Context;

public interface Writer {
    void write(Context context, Report report, Usage usage);
}
