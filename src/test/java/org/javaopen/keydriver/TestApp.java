package org.javaopen.keydriver;

import org.apache.commons.lang.RandomStringUtils;
import org.javaopen.keydriver.data.DummyKeyword;
import org.javaopen.keydriver.data.DummyTest;
import org.javaopen.keydriver.data.Keyword;
import org.javaopen.keydriver.data.Section;
import org.javaopen.keydriver.driver.Context;
import org.javaopen.keydriver.driver.Database;
import org.javaopen.keydriver.driver.Driver;
import org.javaopen.keydriver.driver.DriverFactory;
import org.javaopen.keydriver.driver.Web;
import org.javaopen.keydriver.reader.ExcelReader;
import org.javaopen.keydriver.reader.Reader;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.openqa.selenium.Alert;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

public class TestApp {
    private static final Random random = new Random();
    private Context context;
    private ResourceBundle bundle = ResourceBundle.getBundle(Context.CONFIG);
    private Driver web;
    private Driver database;
    private WebDriver driver;
    private WebElement element;

    @Before
    public void setUp() {
        Locale.setDefault(Locale.US);//important

        context = mock(Context.class);
        web = new Web();
        database = mock(Database.class);
        driver = mock(WebDriver.class, withSettings().extraInterfaces(TakesScreenshot.class));
        element = mock(WebElement.class);
        // mock getBundle
        when(context.getBundle()).thenReturn(bundle);
        // mock getDriver
        ArgumentMatcher<org.javaopen.keydriver.data.Test> matcher = new ArgumentMatcher<org.javaopen.keydriver.data.Test>() {
            @Override
            public boolean matches(Object o) {
                if (o == null) {
                    return false;
                }
                org.javaopen.keydriver.data.Test t = (org.javaopen.keydriver.data.Test)o;
                Keyword k = t.getKeyword();
                return DriverFactory.WEB_KEYWORDS.contains(k);
            }
        };
        when(context.getDriver(argThat(matcher))).thenReturn(web);
        when(context.getDriver(argThat(not(matcher)))).thenReturn(database);
        // mock getWebDriver
        when(context.getWebDriver()).thenReturn(driver);
        // mock screenshot
        when(((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE))
                .thenReturn(new File("./src/test/resources/screenshot.png"));
        // mock findElement
        when(driver.findElement(anyObject())).thenReturn(element);
        // mock select
        when(element.getTagName()).thenReturn("select");
        WebElement option = mock(WebElement.class);
        when(element.findElements(anyObject())).thenReturn(Arrays.asList(option));
        when(element.isEnabled()).thenReturn(true);// selenium-java 4.5.3
        when(option.isEnabled()).thenReturn(true);// selenium-java 5.5.3
        // mock alert
        WebDriver.TargetLocator locator = mock(WebDriver.TargetLocator.class);
        Alert alert = mock(Alert.class);
        when(locator.alert()).thenReturn(alert);
        when(driver.switchTo()).thenReturn(locator);
    }

    @Test
    public void testExecute() throws Exception {
        List<Section> sections = generateSections();
        Reader reader = mock(ExcelReader.class);
        when(reader.read(anyObject(), anyObject())).thenReturn(sections);
        Map<String, Section> sectionMap = new HashMap<>();
        for (Section s: sections) {
            sectionMap.put(s.getName(), s);
        }
        when(context.getSectionMap()).thenReturn(sectionMap);

        // execute
        App.execute(context, reader);

        Section top = sections.get(0);
        assertThat(top.isExecuted(), is(true));
        assertThat(top.isRan(), is(false));

        Section s1 = sections.get(1);
        assertThat(s1.isExecuted(), is(false));
        assertThat(s1.isRan(), is(true));

        Section s2 = sections.get(2);
        assertThat(s2.isExecuted(), is(false));
        assertThat(s2.isRan(), is(true));

        Section s3 = sections.get(3);
        assertThat(s3.isExecuted(), is(false));
        assertThat(s3.isRan(), is(true));

        Section s4 = sections.get(4);
        assertThat(s4.isExecuted(), is(true));
        assertThat(s4.isRan(), is(false));
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
