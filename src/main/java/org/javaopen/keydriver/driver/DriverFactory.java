package org.javaopen.keydriver.driver;

import org.javaopen.keydriver.data.DataType;
import org.javaopen.keydriver.data.Keyword;
import org.javaopen.keydriver.data.Param;
import org.javaopen.keydriver.data.Record;

import java.util.Arrays;
import java.util.List;

public class DriverFactory {
    private static List<Keyword> WEB_KEYWORDS = Arrays.asList(new Keyword[]{
        Keyword.OPEN,
        Keyword.CLICK,
        Keyword.INPUT,
        Keyword.CLEAR,
        Keyword.SELECT,
        Keyword.CAPTURE,
        Keyword.ACCEPT,
        Keyword.DISMISS,
        Keyword.UPLOAD,
    });

    private static final Driver web = new Web();
    private static final Driver database = new Database();

    public static Driver getDriver(Record record) {
        if (WEB_KEYWORDS.contains(record.getKeyword())) {
            return web;
        } else if (record.getKeyword().equals(Keyword.EXECUTE)) {
            return database;
        } else if (record.getKeyword().equals(Keyword.ASSERT)) {
            Param param = record.getObject();
            if (param == null) {
                param = record.getArgument();
            }
            if (param.getTag().equals(DataType.SQL)) {
                return database;
            } else {
                return web;
            }
        } else {
            throw new IllegalArgumentException(record.toString());
        }
    }
}
