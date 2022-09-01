package org.javaopen.keydriver.data;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TestParam {
    @Test
    public void testParam() {
        Param param = new Param("id[save_button]");
        assertThat(param.getTag(), is(DataType.ID));
        assertThat(param.getValue(), is("save_button"));

        param = new Param("is[true]");
        assertThat(param.getTag(), is(Matcher.IS));
        assertThat(param.getValue(), is("true"));

        param = new Param("url[http://www.javaopen.org/");
        assertThat(param.getTag(), is(DataType.URL));
        assertThat(param.getValue(), is("http://www.javaopen.org/"));
    }
}
