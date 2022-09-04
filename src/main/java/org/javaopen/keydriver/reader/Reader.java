package org.javaopen.keydriver.reader;

import org.javaopen.keydriver.data.Section;
import org.javaopen.keydriver.driver.Context;

import java.io.IOException;
import java.util.List;

public interface Reader {
    List<Section> read(Context context, String path) throws IOException;
}
