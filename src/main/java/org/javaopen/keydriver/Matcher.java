package org.javaopen.keydriver;

import java.util.Arrays;

public enum Matcher implements Tag {
    IS("is"),
    IS_NOT("isNot"),
    IS_NULL("isNull"),
    IS_NOT_NULL("isNotNull"),
    GREATER_THAN("greaterThan"),
    GREATER_THAN_EQUAL("greaterThanEqual"),
    LESS_THAN("lessThan"),
    LESS_THAN_EQUAL("lessThanEqual"),
    ;
    public static Tag getTag(String name) {
        return Arrays.stream(Matcher.values()).filter(x -> x.getString().equals(name)).findFirst().orElseThrow(() -> new IllegalArgumentException(name));
    }

    private String text;
    private Matcher(final String text) {
        this.text = text;
    }

    @Override
    public String getString() {
        return text;
    }

}
