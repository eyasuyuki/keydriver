package org.javaopen.keydriver;

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
    }
}
