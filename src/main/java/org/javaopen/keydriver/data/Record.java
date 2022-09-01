package org.javaopen.keydriver.data;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Record {
    public static final String NUMBER_KEY = "number";
    public static final String KEYWORD_KEY = "keyword";
    public static final String TARGET_KEY = "target";
    public static final String ARGUMENT_KEY = "argument";
    public static final String COMMENT_KEY = "comment";
    public static final String OBJECT_KEY = "object";
    public static final String OPTION_KEY = "option";
    public static final List<String> KEYS = Arrays.asList(new String[]{
        NUMBER_KEY,
        KEYWORD_KEY,
        TARGET_KEY,
        ARGUMENT_KEY,
        COMMENT_KEY,
        OBJECT_KEY,
        OPTION_KEY});
    private int number;
    private Keyword keyword;
    private Param target;
    private Param argument;
    private String comment;
    private Param object;
    private Param option;

    public Record(Map<String, String> record) {
        setNumber(record.get(NUMBER_KEY));
        setKeyword(record.get(KEYWORD_KEY));
        setTarget(record.get(TARGET_KEY));
        setArgument(record.get(ARGUMENT_KEY));
        setComment(record.get(COMMENT_KEY));
        setObject(record.get(OBJECT_KEY));
        setOption(record.get(OPTION_KEY));

    }

    public int getNumber() {
        return number;
    }

    public Keyword getKeyword() {
        return keyword;
    }

    public Param getTarget() {
        return target;
    }

    public Param getArgument() {
        return argument;
    }

    public String getComment() {
        return comment;
    }

    public Param getObject() {
        return object;
    }

    public Param getOption() {
        return option;
    }

    private void setNumber(String number) {
        int n = Integer.parseInt(number);
    }

    private void setKeyword(String keyword) {
        this.keyword = Keyword.getKeyword(keyword);
    }

    private void setTarget(String target) {
        this.target = new Param(target);
    }

    private void setArgument(String argument) {
        this.argument = new Param(argument);
    }

    private void setComment(String comment) {
        this.comment = comment;
    }

    private void setObject(String object) {
        this.object = new Param(object);
    }

    private void setOption(String option) {
        this.option = new Param(option);
    }
}
