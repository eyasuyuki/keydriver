package org.javaopen.keydriver.data;

import org.javaopen.keydriver.driver.Context;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

public class TestTestCase {
    @org.junit.Test
    public void testTest() {
        Locale.setDefault(Locale.US);
        Context context = Context.createContext(null, null, null);
        Map<String, String> map = Stream.of(new String[][] {
                { "No", "1" },
                { "Keyword", "open" },
                { "Target", "url[https://www.example.com]" },
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        TestCase testCase = new TestCase(context, map);

        assertThat(testCase.getNumber(), is(1));
        assertThat(testCase.getKeyword(), is(Keyword.OPEN));
        assertThat(testCase.getTarget().getTag(), is(DataType.URL));
        assertThat(testCase.getTarget().getValue(), is("https://www.example.com"));
        assertThat(testCase.getArgument(), nullValue());
        assertThat(testCase.getComment(), nullValue());
        assertThat(testCase.getObject(), nullValue());
        assertThat(testCase.getOption(), nullValue());

        map = Stream.of(new String[][] {
                { "No", "2" },
                { "Keyword", "input" },
                { "Target", "Textbox" },
                { "Argument", "Metal Gear Solid" },
                { "Object", "name[q]" },
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        testCase = new TestCase(context, map);

        assertThat(testCase.getNumber(), is(2));
        assertThat(testCase.getKeyword(), is(Keyword.INPUT));
        assertThat(testCase.getTarget().getTag(), is(DataType.TEXT));
        assertThat(testCase.getTarget().getValue(), is("Textbox"));
        assertThat(testCase.getArgument().getTag(), is(DataType.TEXT));
        assertThat(testCase.getArgument().getValue(), is("Metal Gear Solid"));
        assertThat(testCase.getComment(), nullValue());
        assertThat(testCase.getObject().getTag(), is(DataType.NAME));
        assertThat(testCase.getObject().getValue(), is("q"));
        assertThat(testCase.getOption(), nullValue());

        map = Stream.of(new String[][]{
                {"No", "3"},
                {"Keyword", "upload"},
                {"Target", "/Users/yasuyuki/Documents/playlist.txt"},
                {"Object", "id[file_upload_1]"}
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        testCase = new TestCase(context, map);

        assertThat(testCase.getNumber(), is(3));
        assertThat(testCase.getKeyword(), is(Keyword.UPLOAD));
        assertThat(testCase.getTarget().getTag(), is(DataType.TEXT));
        assertThat(testCase.getTarget().getValue(), is("/Users/yasuyuki/Documents/playlist.txt"));
        assertThat(testCase.getObject().getTag(), is(DataType.ID));
        assertThat(testCase.getObject().getValue(), is("file_upload_1"));

        map = Stream.of(new String[][]{
                {"No", "7"},
                {"Keyword", "assert"},
                {"Argument", "fail[]"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        testCase = new TestCase(context, map);

        assertThat(testCase.getNumber(), is(7));
        assertThat(testCase.getKeyword(), is(Keyword.ASSERT));
        assertThat(testCase.getArgument().getTag(), is(Matches.FAIL));
    }
}
