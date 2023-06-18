package org.javaopen.keydriver.reader;

import org.javaopen.keydriver.data.Keyword;
import org.javaopen.keydriver.data.Param;
import org.javaopen.keydriver.data.Section;
import org.javaopen.keydriver.data.TestCase;
import org.javaopen.keydriver.driver.Context;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TestExcelReader {

    private Context context;

    @Before
    public void setUp() {
        Locale.setDefault(Locale.JAPAN);//important

        // context
        context = Context.createContext(null, null, null);
    }

    @Test
    public void testRead() throws IOException {
        // Skip settings
        context.getConfig().setProperty(Context.SKIP_SHEETS_KEY, 2);
        context.getConfig().setProperty(Context.SKIP_HEADERS_KEY, 3);

        Reader reader = new ExcelReader();
        final String path = "./asset/Google検索.xlsx";
        List<Section> sections = reader.read(context, path);

        assertThat(sections).isNotNull();
        assertThat(sections).asList().size().isEqualTo(1);

        Section section = sections.get(0);
        assertThat(section.getName()).isEqualTo("Google検索");

        TestCase testCase = section.getTestCaseList().get(0);
        assertThat(testCase)
            .extracting("keyword", "target")
            .doesNotContainNull()
            .containsExactly(Keyword.OPEN, new Param("url[https://www.google.com]"));

        testCase = section.getTestCaseList().get(1);
        assertThat(testCase)
            .extracting("keyword", "target", "argument", "object")
            .doesNotContainNull()
            .containsExactly(Keyword.INPUT, new Param("テキストボックス"), new Param("サルゲッチュ"), new Param("name[q]"));

        testCase = section.getTestCaseList().get(2);
        assertThat(testCase)
            .extracting("keyword", "target", "argument", "object")
            .doesNotContainNull()
            .containsExactly(Keyword.ASSERT, new Param("ボタン[Google 検索]表示"), new Param("is[true]"), new Param("name[btnK#displayed]"));

        testCase = section.getTestCaseList().get(3);
        assertThat(testCase)
            .extracting("keyword", "target", "argument", "object")
            .doesNotContainNull()
            .containsExactly(Keyword.ASSERT, new Param("ボタン[Google 検索]活性"), new Param("is[true]"), new Param("name[btnK#enabled]"));

        testCase = section.getTestCaseList().get(4);
        assertThat(testCase)
            .extracting("keyword", "target", "object")
            .doesNotContainNull()
            .containsExactly(Keyword.CLICK, new Param("ボタン[Google 検索]"), new Param("name[btnK]"));

        testCase = section.getTestCaseList().get(5);
        assertThat(testCase)
            .extracting("keyword", "target", "argument", "object")
            .doesNotContainNull()
            .containsExactly(Keyword.ASSERT, new Param("タイトル"), new Param("is[サルゲッチュ - Google 検索]"), new Param("xpath[/html/head/title]"));

        testCase = section.getTestCaseList().get(6);
        assertThat(testCase)
                .extracting("keyword", "comment")
                .doesNotContainNull()
                .containsExactly(Keyword.CAPTURE, "スクリーンショット");

    };

    @After
    public void tearDown() {}
}
