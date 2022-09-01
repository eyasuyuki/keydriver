package org.javaopen.keydriver.data;

import java.util.Arrays;

public enum DataType implements Tag {
    ID("id"),
    NAME("name"),
    XPATH("xpath"),
    URL("url"),
    SQL("sql"),
    TEXT("text"),
    ;

    public static Tag getTag(String name) {
        return Arrays.stream(DataType.values())
            .filter(x -> x.getString().equals(name))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(name));
    }

    private String text;
    private DataType(final String text) {
        this.text = text;
    }


    @Override
    public String getString() {
        return text;
    }
}
