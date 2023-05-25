package org.javaopen.keydriver.data;

import org.javaopen.keydriver.driver.Context;
import org.javaopen.keydriver.driver.Driver;

import java.util.ArrayList;
import java.util.List;

public class Section {
    public Section(String name) {
        this.name = name;
    }

    private String name;
    private List<Test> tests = new ArrayList<>();
    private boolean executed;
    private boolean ran;

    public void run(Context context) {
        loop(context);
        setRan(true);
    }

    public void execute(Context context) {
        if (isExecuted() || isRan()) {
            return;
        }
        loop(context);
        setExecuted(true);
    }

    private void loop(Context context) {
        for (Test t : getTests()) {
            Driver driver = context.getDriver(t);
            driver.perform(context, this, t);
        }
    }

    public String getName() {
        return name;
    }

    public List<Test> getTests() {
        return tests;
    }

    public boolean isExecuted() {
        return executed;
    }

    public void setExecuted(boolean executed) {
        this.executed = executed;
    }

    public boolean isRan() {
        return ran;
    }

    public void setRan(boolean ran) {
        this.ran = ran;
    }
}

