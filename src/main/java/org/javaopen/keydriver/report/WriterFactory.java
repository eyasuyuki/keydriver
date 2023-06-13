package org.javaopen.keydriver.report;

import org.javaopen.keydriver.driver.Context;

import java.util.HashMap;
import java.util.Map;

public class WriterFactory {
    private static Map<String, Writer> writerMap = new HashMap<String, Writer>();
    static {
        writerMap.put("xlsx", new ExcelWriter());
    }
    public static Writer getWriter(Context context) {
        String ext = context.getConfig().getString(Context.OUTPUT_EXTENSION_KEY);
        Writer result = writerMap.get(ext);
        if (result == null) throw new IllegalArgumentException(ext);
        return result;
    }
}
