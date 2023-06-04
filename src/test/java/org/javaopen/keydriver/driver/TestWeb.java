package org.javaopen.keydriver.driver;

import org.javaopen.keydriver.data.DummyTestList;
import org.javaopen.keydriver.data.Keyword;
import org.javaopen.keydriver.data.Matches;
import org.javaopen.keydriver.data.Section;
import org.javaopen.keydriver.data.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.util.Arrays;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

public class TestWeb {
    private DummyTestList dummyTests;
    private Context context;
    private WebDriver driver;
    private WebElement element;

    @Before
    public void setUp() {
        Locale.setDefault(Locale.US);//important

        context = Context.getContext(null, null, null);
        dummyTests = new DummyTestList(context);
        driver = mock(WebDriver.class, withSettings().extraInterfaces(TakesScreenshot.class));
        element = mock(WebElement.class);
        context.setWebDriver(driver);
        // mock screenshot
        when(((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE))
            .thenReturn(new File("./src/test/resources/screenshot.png"));
        // mock findElement
        when(driver.findElement(any(By.class))).thenReturn(element);
        // mock alert
        WebDriver.TargetLocator locator = mock(WebDriver.TargetLocator.class);
        Alert alert = mock(Alert.class);
        when(locator.alert()).thenReturn(alert);
        when(driver.switchTo()).thenReturn(locator);
    }

    @Test
    public void testOpen() {
        TestCase testCase = dummyTests.getTest(Keyword.OPEN);

        Driver d = context.getDriver(testCase);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        d.perform(context, section, testCase);

        assertSuccess(testCase);
    }

    private void assertSuccess(TestCase testCase) {
        assertThat(testCase.isExecuted(), is(true));
        assertThat(testCase.getStart(), is(not(nullValue())));
        assertThat(testCase.getEnd(), is(not(nullValue())));
        assertThat(testCase.getEnd(), is(greaterThanOrEqualTo(testCase.getStart())));
        assertThat(testCase.isSuccess(), is(true));
        assertThat(testCase.isExpectingFailure(), is(false));
        assertThat(testCase.getStackTrace(), is(nullValue()));
    }

    @Test
    public void testOpenException() {
        TestCase testCase = dummyTests.getTest(Keyword.OPEN);

        Driver d = context.getDriver(testCase);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        doThrow(new RuntimeException()).when(driver).get(anyString());
        try {
            d.perform(context, section, testCase);
            assertThat(null, false);//fail if no-exception
        } catch (Exception e) {
            assertException(testCase);
        }
    }

    private void assertException(TestCase testCase) {
        assertThat(testCase.isExecuted(), is(true));
        assertThat(testCase.getStart(), is(not(nullValue())));
        assertThat(testCase.getEnd(), is(not(nullValue())));
        assertThat(testCase.getEnd(), is(greaterThanOrEqualTo(testCase.getStart())));
        assertThat(testCase.isSuccess(), is(false));
        assertThat(testCase.isExpectingFailure(), is(false));
        assertThat(testCase.getStackTrace(), is(not(nullValue())));
    }

    @Test
    public void testClick() {
        TestCase testCase = dummyTests.getTest(Keyword.CLICK);

        Driver d = context.getDriver(testCase);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        d.perform(context, section, testCase);

        assertSuccess(testCase);
    }

    @Test
    public void testClickException() {
        TestCase testCase = dummyTests.getTest(Keyword.CLICK);

        Driver d = context.getDriver(testCase);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        doThrow(new RuntimeException()).when(element).click();
        try {
            d.perform(context, section, testCase);
            assertThat(null, false);
        } catch (Exception e) {
            assertException(testCase);
        }
    }

    @Test
    public void testInput() {
        TestCase testCase = dummyTests.getTest(Keyword.INPUT);

        Driver d = context.getDriver(testCase);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        d.perform(context, section, testCase);

        assertSuccess(testCase);
    }

    @Test
    public void testInputException() {
        TestCase testCase = dummyTests.getTest(Keyword.INPUT);

        Driver d = context.getDriver(testCase);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        doThrow(new RuntimeException()).when(element).sendKeys(any(CharSequence.class));
        try {
            d.perform(context, section, testCase);
            assertThat(null, false);
        } catch(Exception e) {
            assertException(testCase);
        }
    }

    @Test
    public void testClear() {
        TestCase testCase = dummyTests.getTest(Keyword.CLEAR);

        Driver d = context.getDriver(testCase);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        d.perform(context, section, testCase);

        assertSuccess(testCase);

    }

    @Test
    public void testClearException() {
        TestCase testCase = dummyTests.getTest(Keyword.CLEAR);

        Driver d = context.getDriver(testCase);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        doThrow(new RuntimeException()).when(element).clear();
        try {
            d.perform(context, section, testCase);
            assertThat(null, false);
        } catch (Exception e) {
            assertException(testCase);
        }
    }

    @Test
    public void testSelect() {
        TestCase testCase = dummyTests.getTest(Keyword.SELECT);

        Driver d = context.getDriver(testCase);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        // prepare select element
        when(element.getTagName()).thenReturn("select");
        WebElement option = mock(WebElement.class);
        when(option.getAttribute(anyString())).thenReturn("1");
        when(element.findElements(any(By.class))).thenReturn(Arrays.asList(option));
        when(element.isEnabled()).thenReturn(true); // selenium-java 4.5.3
        when(option.isEnabled()).thenReturn(true); // selenium-java 4.5.3

        d.perform(context, section, testCase);

        assertSuccess(testCase);
    }

    @Test
    public void testSelectException() {
        TestCase testCase = dummyTests.getTest(Keyword.SELECT);

        Driver d = context.getDriver(testCase);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        doThrow(new RuntimeException()).when(element).findElements(any(By.class));
        try {
            d.perform(context, section, testCase);
            assertThat(null, false);
        } catch (Exception e) {
            assertException(testCase);
        }
    }

    @Test
    public void testCapture() {
        TestCase testCase = dummyTests.getTest(Keyword.CAPTURE);

        Driver d = context.getDriver(testCase);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        d.perform(context, section, testCase);

        assertSuccess(testCase);
    }

    @Test
    public void testAccept() {
        TestCase testCase = dummyTests.getTest(Keyword.ACCEPT);

        Driver d = context.getDriver(testCase);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        d.perform(context, section, testCase);

        assertSuccess(testCase);
    }

    @Test
    public void testAcceptException() {
        TestCase testCase = dummyTests.getTest(Keyword.ACCEPT);

        Driver d = context.getDriver(testCase);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        doThrow(new RuntimeException()).when(driver).switchTo();
        try {
            d.perform(context, section, testCase);
            assertThat(null, false);
        } catch (Exception e) {
            assertException(testCase);
        }
    }

    @Test
    public void testDismiss() {
        TestCase testCase = dummyTests.getTest(Keyword.DISMISS);

        Driver d = context.getDriver(testCase);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        d.perform(context, section, testCase);

        assertSuccess(testCase);
    }

    @Test
    public void testDismissException() {
        TestCase testCase = dummyTests.getTest(Keyword.DISMISS);

        Driver d = context.getDriver(testCase);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        doThrow(new RuntimeException()).when(driver).switchTo();
        try {
            d.perform(context, section, testCase);
            assertThat(null, false);
        } catch (Exception e) {
            assertException(testCase);
        }
    }

    @Test
    public void testUpload() {
        TestCase testCase = dummyTests.getTest(Keyword.UPLOAD);

        Driver d = context.getDriver(testCase);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        d.perform(context, section, testCase);

        assertSuccess(testCase);
    }

    @Test
    public void testUploadException() {
        TestCase testCase = dummyTests.getTest(Keyword.UPLOAD);

        Driver d = context.getDriver(testCase);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        doThrow(new RuntimeException()).when(element).sendKeys(anyString());
        try {
            d.perform(context, section, testCase);
            assertThat(null, false);
        } catch (Exception e) {
            assertException(testCase);
        }
    }

    @Test
    public void testAssert() {
        TestCase testCase = dummyTests.getTest(Keyword.ASSERT);

        Driver d = context.getDriver(testCase);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        when(element.isEnabled()).thenReturn(true);

        d.perform(context, section, testCase);

        assertSuccess(testCase);
    }

    @Test
    public void testAssertFailed() {
        TestCase testCase = dummyTests.getTest(Keyword.ASSERT);

        Driver d = context.getDriver(testCase);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        when(element.isEnabled()).thenReturn(false);
        try {
            d.perform(context, section, testCase);
            assertThat(null, false);
        } catch (Exception e) {
            assertException(testCase);
        }
    }

    @Test
    public void testAssertExpectingFailure() {
        TestCase testCase = dummyTests.getTests().stream()
            .filter(x -> x.getKeyword().equals(Keyword.ASSERT) && x.getArgument().getTag().equals(Matches.FAIL))
            .findFirst()
            .orElse(null);

        Driver d = context.getDriver(testCase);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        when(element.isEnabled()).thenReturn(false);
        try {
            d.perform(context, section, testCase);
            assertThat(null, false);
        } catch (Exception e) {
            assertThat(testCase.isExecuted(), is(true));
            assertThat(testCase.getStart(), is(not(nullValue())));
            assertThat(testCase.getEnd(), is(not(nullValue())));
            assertThat(testCase.getEnd(), is(greaterThanOrEqualTo(testCase.getStart())));
            assertThat(testCase.isSuccess(), is(false));
            assertThat(testCase.isExpectingFailure(), is(true));
            assertThat(testCase.getStackTrace(), is(not(nullValue())));
        }
    }

    @Test
    public void testAssertException() {
        testAssertFailed();
    }
}
