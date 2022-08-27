package org.javaopen.keydriver;

public enum Keyword {
    OPEN("open"),
    CLICK("click"),
    SELECT("select"),
    INPUT("input"),
    ACCEPT("accept"),
    DISMISS("dismiss"),
    ASSERT("assert"),
    EXECUTE("execute")
    ;

    private final String text;

    private Keyword(final String text) {
        this.text = text;
    }

    public String getString() {
        return text;
    }

}
