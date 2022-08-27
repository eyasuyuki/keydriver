package org.javaopen.keydriver;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Param {
    private static final String VALUE_HEAD = "\\[";
    private static final String VALUE_TAIL = "]";
    private static final List<String> MATCHERS = Arrays.asList(Matcher.values()).stream().map(x -> x.getString()).collect(Collectors.toList());
    private static final List<String> DATA_TYPES = Arrays.asList(DataType.values()).stream().map(x -> x.getString()).collect(Collectors.toList());
    public Param(String param) {
        String[] str = param.split("\\[");
        if (str.length < 2) throw new IllegalArgumentException(param);
        if (MATCHERS.contains(str[0])) {
            tag = Matcher.getTag(str[0]);
            setValue(str[1]);
        } else if (DATA_TYPES.contains(str[0])) {
            tag = DataType.getTag(str[0]);
            setValue(str[1]);
        } else {
            throw new IllegalArgumentException(param);
        }
    }
    private Tag tag;
    private String value;

    public Tag getTag() {
        return tag;
    }

    public String getValue() {
        return value;
    }
    private void setValue(String rawValue) {
        if (rawValue.endsWith("]")) {
            value = rawValue.substring(0, rawValue.length() - 1);
        } else {
            value = rawValue;
        }
    }
}
