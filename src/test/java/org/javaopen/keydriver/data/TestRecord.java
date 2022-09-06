package org.javaopen.keydriver.data;

import org.javaopen.keydriver.driver.Context;
import org.junit.Test;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

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
    }
}
