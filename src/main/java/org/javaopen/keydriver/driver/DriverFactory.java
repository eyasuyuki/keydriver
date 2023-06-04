package org.javaopen.keydriver.driver;

import org.javaopen.keydriver.data.DataType;
import org.javaopen.keydriver.data.Keyword;
import org.javaopen.keydriver.data.Param;
import org.javaopen.keydriver.data.TestCase;

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

    static Driver getDriver(TestCase testCase) {
        if (WEB_KEYWORDS.contains(testCase.getKeyword())) {
            return web;
        } else if (testCase.getKeyword().equals(Keyword.EXECUTE)) {
            return database;
        } else if (testCase.getKeyword().equals(Keyword.ASSERT)) {
            Param param = testCase.getObject();
            if (param == null) {
                param = testCase.getArgument();
            }
            if (param.getTag().equals(DataType.SQL)) {
                return database;
            } else {
                return web;
            }
        } else {
            throw new IllegalArgumentException(testCase.toString());
        }
    }
}
