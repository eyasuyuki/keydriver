package org.javaopen.keydriver.data;

import org.javaopen.keydriver.driver.Context;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

public class TestTest {
    @org.junit.Test
    public void testTest() {
        Locale.setDefault(Locale.US);
        Context context = Context.getContext();
        Map<String, String> map = Stream.of(new String[][] {
                { "No", "1" },
                { "Keyword", "open" },
                { "Target", "url[https://www.example.com]" },
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        Test test = new Test(context, map);

        assertThat(test.getNumber(), is(1));
        assertThat(test.getKeyword(), is(Keyword.OPEN));
        assertThat(test.getTarget().getTag(), is(DataType.URL));
        assertThat(test.getTarget().getValue(), is("https://www.example.com"));
        assertThat(test.getArgument(), nullValue());
        assertThat(test.getComment(), nullValue());
        assertThat(test.getObject(), nullValue());
        assertThat(test.getOption(), nullValue());

        map = Stream.of(new String[][] {
                { "No", "2" },
                { "Keyword", "input" },
                { "Target", "Textbox" },
                { "Argument", "Metal Gear Solid" },
                { "Object", "name[q]" },
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        test = new Test(context, map);

        assertThat(test.getNumber(), is(2));
        assertThat(test.getKeyword(), is(Keyword.INPUT));
        assertThat(test.getTarget().getTag(), is(DataType.TEXT));
        assertThat(test.getTarget().getValue(), is("Textbox"));
        assertThat(test.getArgument().getTag(), is(DataType.TEXT));
        assertThat(test.getArgument().getValue(), is("Metal Gear Solid"));
        assertThat(test.getComment(), nullValue());
        assertThat(test.getObject().getTag(), is(DataType.NAME));
        assertThat(test.getObject().getValue(), is("q"));
        assertThat(test.getOption(), nullValue());

        map = Stream.of(new String[][]{
                {"No", "3"},
                {"Keyword", "upload"},
                {"Target", "/Users/yasuyuki/Documents/playlist.txt"},
                {"Object", "id[file_upload_1]"}
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        test = new Test(context, map);

        assertThat(test.getNumber(), is(3));
        assertThat(test.getKeyword(), is(Keyword.UPLOAD));
        assertThat(test.getTarget().getTag(), is(DataType.TEXT));
        assertThat(test.getTarget().getValue(), is("/Users/yasuyuki/Documents/playlist.txt"));
        assertThat(test.getObject().getTag(), is(DataType.ID));
        assertThat(test.getObject().getValue(), is("file_upload_1"));

        map = Stream.of(new String[][]{
                {"No", "7"},
                {"Keyword", "assert"},
                {"Argument", "fail[]"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        test = new Test(context, map);

        assertThat(test.getNumber(), is(7));
        assertThat(test.getKeyword(), is(Keyword.ASSERT));
        assertThat(test.getArgument().getTag(), is(Matches.FAIL));
    }
}
