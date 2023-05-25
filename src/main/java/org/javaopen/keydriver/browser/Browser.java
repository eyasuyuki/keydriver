package org.javaopen.keydriver.browser;

import java.util.Arrays;

public enum Browser {
    CHROME("chrome"),
    FIREFOX("firefox"),
    EDGE("edge"),
    SAFARI("safari"),
    IE("ie"),
    ;

    private final String name;
    public String getName() {
        return name;
    }
    private Browser(final String name) {
        this.name = name;
    }
    static Browser getEnum(String name) {
        return Arrays.stream(Browser.values())
                .filter(x -> x.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(name));
    }
}
