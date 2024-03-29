package org.javaopen.keydriver.data;

import org.javaopen.keydriver.driver.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DummyTestList {
    private List<TestCase> testCases = new ArrayList<>();
    private Context context;

    public DummyTestList(Context context) {
        this.context = context;
        testCases.add(new TestCase(context, Stream.of(new String[][] {
                { "No", "1" },
                { "Keyword", "open" },
                { "Target", "url[https://www.example.com]" },
        }).collect(Collectors.toMap(data -> ((String[])data)[0], data -> ((String[])data)[1]))));
        testCases.add(new TestCase(context, Stream.of(new String[][] {
                { "No", "2" },
                { "Keyword", "click" },
                { "Target", "Button" },
                { "Argument", "" },
                { "Object", "id[send_button]" },
        }).collect(Collectors.toMap(data -> ((String[])data)[0], data -> ((String[])data)[1]))));
        testCases.add(new TestCase(context, Stream.of(new String[][] {
                { "No", "3" },
                { "Keyword", "input" },
                { "Target", "Textbox" },
                { "Argument", "Metal Gear Solid" },
                { "Object", "name[q]" },
        }).collect(Collectors.toMap(data -> ((String[])data)[0], data -> ((String[])data)[1]))));
        testCases.add(new TestCase(context, Stream.of(new String[][] {
                { "No", "4" },
                { "Keyword", "clear" },
                { "Target", "Input" },
                { "Object", "id[name_text]" },
        }).collect(Collectors.toMap(data -> ((String[])data)[0], data -> ((String[])data)[1]))));
        testCases.add(new TestCase(context, Stream.of(new String[][] {
                {"No", "5"},
                {"Keyword", "select"},
                {"Target", "Select"},
                {"Argument", "1"},
                {"Object", "id[name_text]"},
                {"Option", "1"},
        }).collect(Collectors.toMap(data -> ((String[])data)[0], data -> ((String[])data)[1]))));
        testCases.add(new TestCase(context, Stream.of(new String[][] {
                { "No", "6" },
                { "Keyword", "capture" },
                { "Comment", "Take screen shot"},
        }).collect(Collectors.toMap(data -> ((String[])data)[0], data -> ((String[])data)[1]))));
        testCases.add(new TestCase(context, Stream.of(new String[][] {
                { "No", "7" },
                { "Keyword", "accept" },
                { "Comment", "Accept an alert"},
        }).collect(Collectors.toMap(data -> ((String[])data)[0], data -> ((String[])data)[1]))));
        testCases.add(new TestCase(context, Stream.of(new String[][] {
                { "No", "9" },
                { "Keyword", "dismiss" },
                { "Comment", "Dismiss an alert"},
        }).collect(Collectors.toMap(data -> ((String[])data)[0], data -> ((String[])data)[1]))));
        testCases.add(new TestCase(context, Stream.of(new String[][] {
                {"No", "10"},
                {"Keyword", "upload"},
                {"Target", "input[file]"},
                {"Argument", "/Users/yasuyuki/Documents/playlist.txt"},
                {"Object", "id[file_upload_1]"}
        }).collect(Collectors.toMap(data -> ((String[])data)[0], data -> ((String[])data)[1]))));
        testCases.add(new TestCase(context, Stream.of(new String[][] {
                {"No", "11"},
                {"Keyword", "assert"},
                {"Target", "input[text]"},
                {"Argument", "is[true]"},
                {"Object", "id[save_button#enabled]"}
        }).collect(Collectors.toMap(data -> ((String[])data)[0], data -> ((String[])data)[1]))));
        testCases.add(new TestCase(context, Stream.of(new String[][] {
                {"No", "12"},
                {"Keyword", "assert"},
                {"Argument", "fail[]"},
        }).collect(Collectors.toMap(data -> ((String[])data)[0], data -> ((String[])data)[1]))));
        testCases.add(new TestCase(context, Stream.of(new String[][] {
                {"No", "14"},
                {"Keyword", "execute"},
                {"Target", "INSERT INTO test (id, name) VALUES (2, 'jazz');"},
                {"Object", "jdbc:postgresql://127.0.0.1:5435/test?user=sa&password=sa"}
        }).collect(Collectors.toMap(data -> ((String[])data)[0], data -> ((String[])data)[1]))));
        testCases.add(new TestCase(context, Stream.of(new String[][] {
                {"No", "15"},
                {"Keyword", "assert"},
                {"Target", "SELECT name FROM test WHERE id=1;"},
                {"Argument", "is[pop]"},
                {"Object", "jdbc:postgresql://127.0.0.1:5435/test?user=sa&password=sa"}
        }).collect(Collectors.toMap(data -> ((String[])data)[0], data-> ((String[])data)[1]))));
    }

    public List<TestCase> getTests() {
        return testCases;
    }

    public TestCase getTest(Keyword keyword) {
        return testCases.stream()
            .filter(x -> x.getKeyword().equals(keyword))
            .findFirst()
            .orElseThrow(null);
    }
}
