package org.javaopen.keydriver.data;

import org.javaopen.keydriver.driver.Context;
import org.javaopen.keydriver.driver.Driver;
import org.javaopen.keydriver.driver.DriverFactory;

import java.util.ArrayList;
import java.util.List;

public class Section {
    public Section(String name) {
        this.name = name;
    }

    private String name;
    private List<Test> tests = new ArrayList<>();
    private boolean executed;

    public void run(Context context) {
        if (isExecuted()) {
            return;
        }
        for (Test t : getTests()) {
            Driver driver = DriverFactory.getDriver(t);
            driver.perform(context, this, t);
        }
        setExecuted(true);
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
}

