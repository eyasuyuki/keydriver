package org.javaopen.keydriver.data;

import java.util.ArrayList;
import java.util.List;

public class Section {
    public Section(String name) {
        this.name = name;
    }

    private String name;
    private List<Test> tests = new ArrayList<>();

    public String getName() {
        return name;
    }

    public List<Test> getTests() {
        return tests;
    }
}
