package org.javaopen.keydriver.driver;

import org.javaopen.keydriver.data.DataType;
import org.javaopen.keydriver.data.Keyword;
import org.javaopen.keydriver.data.Param;
import org.javaopen.keydriver.data.Test;

import java.util.Arrays;
import java.util.List;

public class DriverFactory {
    public static List<Keyword> WEB_KEYWORDS = Arrays.asList(new Keyword[]{
        Keyword.OPEN,
        Keyword.CLICK,
        Keyword.INPUT,
        Keyword.CLEAR,
        Keyword.SELECT,
        Keyword.CAPTURE,
        Keyword.ACCEPT,
        Keyword.DISMISS,
        Keyword.UPLOAD,
        Keyword.SWITCH,
        Keyword._DIRECTIVE,
    });

    private static final Driver web = new Web();
    private static final Driver database = new Database();

    static Driver getDriver(Test test) {
        if (WEB_KEYWORDS.contains(test.getKeyword())) {
            return web;
        } else if (test.getKeyword().equals(Keyword.EXECUTE)) {
            return database;
        } else if (test.getKeyword().equals(Keyword.ASSERT)) {
            Param param = test.getObject();
            if (param == null) {
                param = test.getArgument();
            }
            if (param.getTag().equals(DataType.SQL)) {
                return database;
            } else {
                return web;
            }
        } else {
            throw new IllegalArgumentException(test.toString());
        }
    }
}
