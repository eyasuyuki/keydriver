package org.javaopen.keydriver.reader;

import org.javaopen.keydriver.data.Record;

import java.io.IOException;
import java.util.List;

public interface Reader {
    List<Record> read(String path) throws IOException;
}
