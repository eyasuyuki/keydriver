package org.javaopen.keydriver.data;

import org.javaopen.keydriver.driver.Context;

import java.sql.Timestamp;
import java.util.Map;

import static org.javaopen.keydriver.driver.Context.ARGUMENT_KEY;
import static org.javaopen.keydriver.driver.Context.COMMENT_KEY;
import static org.javaopen.keydriver.driver.Context.KEYWORD_KEY;
import static org.javaopen.keydriver.driver.Context.NUMBER_KEY;
import static org.javaopen.keydriver.driver.Context.OBJECT_KEY;
import static org.javaopen.keydriver.driver.Context.OPTION_KEY;
import static org.javaopen.keydriver.driver.Context.TARGET_KEY;

public class Test {
    private int number;
    private Keyword keyword;
    private Param target;
    private Param argument;
    private String comment;
    private Param object;
    private Param option;

    private Timestamp start;
    private Timestamp end;
    private boolean executed;
    private boolean success;
    private boolean expectingFailure;
    private String expected;
    private String actual;
    private String matchFailed;
    private String stackTrace;

    public Test(Context context, Map<String, String> record) {
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
        if (number == null) {
            return;
        }
        int n = Integer.parseInt(number);
        this.number = n;
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

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    public boolean isExecuted() {
        return executed;
    }

    public void setExecuted(boolean executed) {
        this.executed = executed;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }

    public String getActual() {
        return actual;
    }

    public void setActual(String actual) {
        this.actual = actual;
    }

    public String getMatchFailed() {
        return matchFailed;
    }

    public void setMatchFailed(String matchFailed) {
        this.matchFailed = matchFailed;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }


    public boolean isExpectingFailure() {
        return expectingFailure;
    }

    public void setExpectingFailure(boolean expectingFailure) {
        this.expectingFailure = expectingFailure;
    }
}
