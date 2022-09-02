package org.javaopen.keydriver.web;

public enum Browser {
    CHROME("chrome"),
    FIREFOX("firefox"),
    EDGE("edge"),
    SAFARI("safari"),
    ;

    private final String name;
    public String getName() {
        return name;
    }
    private Browser(final String name) {
        this.name = name;
    }
    static Browser getEnum(String name) {
        if (CHROME.name.equals(name)) {
            return CHROME;
        } else if (FIREFOX.name.equals(name)) {
            return FIREFOX;
        } else if (EDGE.name.equals(name)) {
            return EDGE;
        } else if (SAFARI.name.equals(name)) {
            return SAFARI;
        } else {
            throw new IllegalArgumentException(name);
        }
    }
}
