package org.javaopen.keydriver.reader;

import org.apache.commons.io.FilenameUtils;

import java.util.HashMap;
import java.util.Map;

public class ReaderFactory {
    private static Map<String, Reader> readerMap = new HashMap<String, Reader>();
    static {
        readerMap.put("xlsx", new ExcelReader());
    }

    public static Reader getReader(String path) {
        String ext = FilenameUtils.getExtension(path);
        Reader result = readerMap.get(ext);
        if (result == null) throw new IllegalArgumentException(path);
        return result;
    }
}
