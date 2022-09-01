package org.javaopen.keydriver.data;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

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
    private Map<String, String> dic;
    private int number;
    private Keyword keyword;
    private Param target;
    private Param argument;
    private String comment;
    private Param object;
    private Param option;

    public Record(Map<String, String> record) {
        dic = getDic();
        setNumber(record.get(dic.get(NUMBER_KEY)));
        setKeyword(record.get(dic.get(KEYWORD_KEY)));
        setTarget(record.get(dic.get(TARGET_KEY)));
        setArgument(record.get(dic.get(ARGUMENT_KEY)));
        setComment(record.get(dic.get(COMMENT_KEY)));
        setObject(record.get(dic.get(OBJECT_KEY)));
        setOption(record.get(dic.get(OPTION_KEY)));
    }

    private Map<String, String> getDic() {
        Map<String, String> dic = new HashMap<>();
        ResourceBundle bundle = ResourceBundle.getBundle(Param.CONFIG);
        for (String k: KEYS) {
            String s = bundle.getString(k);
            if (StringUtils.isEmpty(s)) {
                s = k;
            }
            dic.put(k, s);
        }
        return dic;
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
        if (keyword == null) {
            return;
        }
        this.keyword = Keyword.getKeyword(keyword);
    }

    private void setTarget(String target) {
        if (target == null) {
            return;
        }
        this.target = new Param(target);
    }

    private void setArgument(String argument) {
        if (argument == null) {
            return;
        }
        this.argument = new Param(argument);
    }

    private void setComment(String comment) {
        this.comment = comment;
    }

    private void setObject(String object) {
        if (object == null) {
            return;
        }
        this.object = new Param(object);
    }

    private void setOption(String option) {
        if (option == null) {
            return;
        }
        this.option = new Param(option);
    }
}
