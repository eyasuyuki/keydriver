package org.javaopen.keydriver.data;

import org.javaopen.keydriver.driver.Context;

import java.util.Map;

import static org.javaopen.keydriver.driver.Context.ARGUMENT_KEY;
import static org.javaopen.keydriver.driver.Context.COMMENT_KEY;
import static org.javaopen.keydriver.driver.Context.KEYWORD_KEY;
import static org.javaopen.keydriver.driver.Context.NUMBER_KEY;
import static org.javaopen.keydriver.driver.Context.OBJECT_KEY;
import static org.javaopen.keydriver.driver.Context.OPTION_KEY;
import static org.javaopen.keydriver.driver.Context.TARGET_KEY;

public class Record {
    private int number;
    private Keyword keyword;
    private Param target;
    private Param argument;
    private String comment;
    private Param object;
    private Param option;

    public Record(Context context, Map<String, String> record) {
        final Map<String, String> dic = context.getDic();
        setNumber(record.get(dic.get(NUMBER_KEY)));
        setKeyword(record.get(dic.get(KEYWORD_KEY)));
        setTarget(record.get(dic.get(TARGET_KEY)));
        setArgument(record.get(dic.get(ARGUMENT_KEY)));
        setComment(record.get(dic.get(COMMENT_KEY)));
        setObject(record.get(dic.get(OBJECT_KEY)));
        setOption(record.get(dic.get(OPTION_KEY)));
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
