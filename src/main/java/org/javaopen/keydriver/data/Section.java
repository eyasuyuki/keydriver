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
        for (Test t : getTests()) {
            context.setCurrentDriver(DriverFactory.getDriver(t));
            context.getCurrentDriver().perform(context, this, t);
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
}

