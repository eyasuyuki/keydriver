package org.javaopen.keydriver;

import org.apache.commons.lang.RandomStringUtils;
import org.javaopen.keydriver.data.DummyKeyword;
import org.javaopen.keydriver.data.DummyTest;
import org.javaopen.keydriver.data.Keyword;
import org.javaopen.keydriver.data.Section;
import org.javaopen.keydriver.driver.Context;
import org.javaopen.keydriver.reader.Reader;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestApp {
    private static final Random random = new Random();

    @Before
    public void setUp() {
        Locale.setDefault(Locale.US);
    }

    @Test
    public void testRun() throws Exception {
        Context context = Context.getContext();
        List<Section> sections = generateSections();
        Reader reader = mock(Reader.class);
        when(reader.read(anyObject(), anyObject())).thenReturn(sections);
        App.run(context, reader);
    }

    private List<Section> generateSections() {
        List<Section> sections = new ArrayList<>();

        // top section
        Section top = new Section(RandomStringUtils.randomAlphanumeric(random.nextInt(20)+1));
        org.javaopen.keydriver.data.Test dir1 = DummyTest.getDummy(Keyword._DIRECTIVE);
        org.javaopen.keydriver.data.Test dir2  = DummyTest.getDummy(Keyword._DIRECTIVE);
        org.javaopen.keydriver.data.Test dir3 = DummyTest.getDummy(Keyword._DIRECTIVE);
        top.getTests().addAll(Arrays.asList(dir1, dir2, dir3));
        sections.add(top);

        // subroutine sections
        sections.add(generateSection(dir1.getObject().getValue()));
        sections.add(generateSection(dir2.getObject().getValue()));
        sections.add(generateSection(dir3.getObject().getValue()));

        // standalone section
        sections.add(generateSection(RandomStringUtils.randomAlphanumeric(random.nextInt(20)+1)));

        return sections;
    }

    private Section generateSection(String name) {
        Section section = new Section(name);
        int size = random.nextInt(40)+1;
        for (int i=0; i<size; i++) {
            Keyword keyword = DummyKeyword.getRandom();
            if (keyword.equals(Keyword._DIRECTIVE)) {
                continue;
            }
            org.javaopen.keydriver.data.Test test = DummyTest.getDummy(keyword);
            section.getTests().add(test);
        }

        return section;
    }
}
