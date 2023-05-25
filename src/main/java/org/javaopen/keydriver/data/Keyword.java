package org.javaopen.keydriver.data;

import java.util.Arrays;

public enum Keyword {
    _DIRECTIVE("_directive"),
    OPEN("open"),
    CLICK("click"),
    SELECT("select"),
    INPUT("input"),
    CLEAR("clear"),
    ACCEPT("accept"),
    DISMISS("dismiss"),
    CAPTURE("capture"),
    ASSERT("assert"),
    EXECUTE("execute"),
    UPLOAD("upload"),
    SWITCH("switch"),
    ;

    private final String text;

    private Keyword(final String text) {
        this.text = text;
    }

    public static Keyword getKeyword(String keyword) {
        return Arrays.stream(Keyword.values())
            .filter(x -> x.getString().equals(keyword))
            .findFirst()
            .orElse(_DIRECTIVE);
    }

    public String getString() {
        return text;
    }

}
