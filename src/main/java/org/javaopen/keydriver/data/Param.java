package org.javaopen.keydriver.data;

import org.apache.commons.lang.StringUtils;
import org.javaopen.keydriver.driver.Context;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Param {
    private static final String VALUE_HEAD_KEY = "value_head";
    private static final String VALUE_TAIL_KEY = "value_tail";
    private static final String ATTRIBUTE_SEPARATOR_KEY = "attribute_separator";
    private static final String VALUE_HEAD_DEFAULT = "\\[";
    private static final String VALUE_TAIL_DEFAULT = "]";
    private static final String ATTRIBUTE_SEPARATOR_DEFAULT = "#";
    private static final List<String> MATCHERS = Arrays.asList(Matches.values())
        .stream()
        .map(x -> x.getString())
        .collect(Collectors.toList());
    private static final List<String> DATA_TYPES = Arrays.asList(DataType.values())
        .stream()
        .map(x -> x.getString())
        .collect(Collectors.toList());
    public Param(String param) {
        String[] str = param.split(getValueHead());
        if (MATCHERS.contains(str[0])) {
            tag = Matches.getTag(str[0]);
            setValue(str[1]);
        } else if (DATA_TYPES.contains(str[0])) {
            tag = DataType.getTag(str[0]);
            setValue(str[1]);
        } else {
            tag = DataType.TEXT;
            setValue(param);
        }
    }

    private String getValueHead() {
        return getConfigValue(VALUE_HEAD_KEY, VALUE_HEAD_DEFAULT);
    }
    private String getValueTail() {
        return getConfigValue(VALUE_TAIL_KEY, VALUE_TAIL_DEFAULT);
    }
    private String getAttributeSeparator() {
        return getConfigValue(ATTRIBUTE_SEPARATOR_KEY, ATTRIBUTE_SEPARATOR_DEFAULT);
    }
    private String getConfigValue(String key, String defValue) {
        ResourceBundle bundle = ResourceBundle.getBundle(Context.CONFIG);
        String value = bundle.getString(key);
        if (StringUtils.isEmpty(value)) {
            return defValue;
        } else {
            return value;
        }
    }
    private Tag tag;
    private String value;
    private String attribute;

    public Tag getTag() {
        return tag;
    }

    public String getValue() {
        return value;
    }
    public String getAttribute() {
        return attribute;
    }
    private void setValue(String rawValue) {
        final String valueTail = getValueTail();
        if (rawValue.endsWith(valueTail)) {
            value = rawValue.substring(0, rawValue.length() - valueTail.length());
        } else {
            value = rawValue;
        }
        String[] values = value.split(getAttributeSeparator());
        if (values.length > 1 && StringUtils.isNotEmpty(values[1])) {
            value = values[0];
            attribute = values[1];
        }
    }

    @Override
    public String toString() {
        String attr = "";
        if (StringUtils.isNotEmpty(attribute)) {
            attr = getAttributeSeparator()+attribute;
        }
        return tag.toString()+"["+value+attr+"]";
    }
}
