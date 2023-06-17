package org.javaopen.keydriver.data;

import org.apache.commons.lang.StringUtils;
import org.javaopen.keydriver.driver.Context;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Param {
    public static final String ATTRIBUTE_DISPLAYED = "displayed";
    public static final String ATTRIBUTE_ENABLED = "enabled";
    public static final String ATTRIBUTE_SELECTED = "selected";
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
        int pos = param.indexOf(getValueHead());
        if (pos > 0 && MATCHERS.contains(param.substring(0, pos))) {
            tag = Matches.getTag(param.substring(0, pos));
            setValue(param.substring(pos+1));
        } else if (pos > 0 && DATA_TYPES.contains(param.substring(0, pos))) {
            tag = DataType.getTag(param.substring(0, pos));
            setValue(param.substring(pos+1));
        } else {
            tag = DataType.TEXT;
            setValue(param);
        }
    }

    private String getValueHead() {
        return getConfigValue(Context.VALUE_HEAD_KEY, VALUE_HEAD_DEFAULT);
    }
    private String getValueTail() {
        return getConfigValue(Context.VALUE_TAIL_KEY, VALUE_TAIL_DEFAULT);
    }
    private String getAttributeSeparator() {
        return getConfigValue(Context.ATTRIBUTE_SEPARATOR_KEY, ATTRIBUTE_SEPARATOR_DEFAULT);
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
        return tag.getString()+"["+value+attr+"]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        return this.toString().equals(obj.toString());
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
}
