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
        assertThat(param.getTag(), is(Matches.IS));
        assertThat(param.getValue(), is("true"));

        param = new Param("url[http://www.javaopen.org/");
        assertThat(param.getTag(), is(DataType.URL));
        assertThat(param.getValue(), is("http://www.javaopen.org/"));

        param = new Param("name[user_label#displayed]");
        assertThat(param.getTag(), is(DataType.NAME));
        assertThat(param.getValue(), is("user_label"));
        assertThat(param.getAttribute(), is("displayed"));

        param = new Param("xpath[//*[@id=\"main\"]/div/div[1]/form/div/div[2]/div[4]/input#displayed]");
        assertThat(param.getTag(), is(DataType.XPATH));
        assertThat(param.getValue(), is("//*[@id=\"main\"]/div/div[1]/form/div/div[2]/div[4]/input"));
        assertThat(param.getAttribute(), is("displayed"));

        param = new Param("sheet[Sheet1]");
        assertThat(param.getTag(), is(DataType.SHEET));
        assertThat(param.getValue(), is("Sheet1"));

        param = new Param("window[1]");
        assertThat(param.getTag(), is(DataType.WINDOW));
        assertThat(param.getValue(), is("1"));

        param = new Param("rect[50:20:200:120]");
        assertThat(param.getTag(), is(DataType.RECT));
        assertThat(param.getValue(), is("50:20:200:120"));
    }
}
