package org.javaopen.keydriver.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Section {
    public Section(String name) {
        this.name = name;
    }

    private String name;
    private List<Record> records = new ArrayList<>();

    public String getName() {
        return name;
    }

    public List<Record> getRecords() {
        return records;
    }
}
