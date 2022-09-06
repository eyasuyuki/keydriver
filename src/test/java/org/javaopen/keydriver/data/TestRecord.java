package org.javaopen.keydriver.data;

import org.javaopen.keydriver.driver.Context;
import org.junit.Test;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

public class TestRecord {
    @Test
    public void testRecord() {
        Locale.setDefault(Locale.US);
        Context context = new Context();
        Map<String, String> map = Stream.of(new String[][] {
                { "No", "1" },
                { "Keyword", "open" },
                { "Target", "url[https://www.example.com]" },
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        Record record = new Record(context, map);

        assertThat(record.getNumber(), is(1));
        assertThat(record.getKeyword(), is(Keyword.OPEN));
        assertThat(record.getTarget().getTag(), is(DataType.URL));
        assertThat(record.getTarget().getValue(), is("https://www.example.com"));
        assertThat(record.getArgument(), nullValue());
        assertThat(record.getComment(), nullValue());
        assertThat(record.getObject(), nullValue());
        assertThat(record.getOption(), nullValue());

        map = Stream.of(new String[][] {
                { "No", "2" },
                { "Keyword", "input" },
                { "Target", "Textbox" },
                { "Argument", "Metal Gear Solid" },
                { "Object", "name[q]" },
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        record = new Record(context, map);

        assertThat(record.getNumber(), is(2));
        assertThat(record.getKeyword(), is(Keyword.INPUT));
        assertThat(record.getTarget().getTag(), is(DataType.TEXT));
        assertThat(record.getTarget().getValue(), is("Textbox"));
        assertThat(record.getArgument().getTag(), is(DataType.TEXT));
        assertThat(record.getArgument().getValue(), is("Metal Gear Solid"));
        assertThat(record.getComment(), nullValue());
        assertThat(record.getObject().getTag(), is(DataType.NAME));
        assertThat(record.getObject().getValue(), is("q"));
        assertThat(record.getOption(), nullValue());
    }
}
