package org.javaopen.keydriver.driver;

import org.javaopen.keydriver.data.DummyTests;
import org.javaopen.keydriver.data.Keyword;
import org.javaopen.keydriver.data.Matches;
import org.javaopen.keydriver.data.Section;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
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
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;
import static org.powermock.api.mockito.PowerMockito.when;

public class TestWeb {
    private DummyTests dummyTests;
    private Context context;
    private WebDriver driver;
    private WebElement element;

    @Before
    public void setUp() {
        Locale.setDefault(Locale.US);//important

        dummyTests = new DummyTests();
        context = Context.getContext();
        driver = mock(WebDriver.class, withSettings().extraInterfaces(TakesScreenshot.class));
        element = mock(WebElement.class);
        context.setDriver(driver);
        // mock screenshot
        when(((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE))
            .thenReturn(new File("./src/test/resources/screenshot.png"));
        // mock findElement
        when(driver.findElement(anyObject())).thenReturn(element);
        // mock alert
        WebDriver.TargetLocator locator = mock(WebDriver.TargetLocator.class);
        Alert alert = mock(Alert.class);
        when(locator.alert()).thenReturn(alert);
        when(driver.switchTo()).thenReturn(locator);
    }

    @Test
    public void testOpen() {
        org.javaopen.keydriver.data.Test test = dummyTests.getTest(Keyword.OPEN);

        Driver d = DriverFactory.getDriver(test);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        d.perform(context, section, test);

        assertSuccess(test);
    }

    private void assertSuccess(org.javaopen.keydriver.data.Test test) {
        assertThat(test.isExecuted(), is(true));
        assertThat(test.getStart(), is(not(nullValue())));
        assertThat(test.getEnd(), is(not(nullValue())));
        assertThat(test.getEnd(), is(greaterThanOrEqualTo(test.getStart())));
        assertThat(test.isSuccess(), is(true));
        assertThat(test.isExpectingFailure(), is(false));
        assertThat(test.getStackTrace(), is(nullValue()));
    }

    @Test
    public void testOpenException() {
        org.javaopen.keydriver.data.Test test = dummyTests.getTest(Keyword.OPEN);

        Driver d = DriverFactory.getDriver(test);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        doThrow(new RuntimeException()).when(driver).get(anyObject());
        try {
            d.perform(context, section, test);
            assertThat(null, false);//fail if no-exception
        } catch (Exception e) {
            assertException(test);
        }
    }

    private void assertException(org.javaopen.keydriver.data.Test test) {
        assertThat(test.isExecuted(), is(true));
        assertThat(test.getStart(), is(not(nullValue())));
        assertThat(test.getEnd(), is(not(nullValue())));
        assertThat(test.getEnd(), is(greaterThanOrEqualTo(test.getStart())));
        assertThat(test.isSuccess(), is(false));
        assertThat(test.isExpectingFailure(), is(false));
        assertThat(test.getStackTrace(), is(not(nullValue())));
    }

    @Test
    public void testClick() {
        org.javaopen.keydriver.data.Test test = dummyTests.getTest(Keyword.CLICK);

        Driver d = DriverFactory.getDriver(test);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        d.perform(context, section, test);

        assertSuccess(test);
    }

    @Test
    public void testClickException() {
        org.javaopen.keydriver.data.Test test = dummyTests.getTest(Keyword.CLICK);

        Driver d = DriverFactory.getDriver(test);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        doThrow(new RuntimeException()).when(element).click();
        try {
            d.perform(context, section, test);
            assertThat(null, false);
        } catch (Exception e) {
            assertException(test);
        }
    }

    @Test
    public void testInput() {
        org.javaopen.keydriver.data.Test test = dummyTests.getTest(Keyword.INPUT);

        Driver d = DriverFactory.getDriver(test);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        d.perform(context, section, test);

        assertSuccess(test);
    }

    @Test
    public void testInputException() {
        org.javaopen.keydriver.data.Test test = dummyTests.getTest(Keyword.INPUT);

        Driver d = DriverFactory.getDriver(test);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        doThrow(new RuntimeException()).when(element).sendKeys(anyObject());
        try {
            d.perform(context, section, test);
            assertThat(null, false);
        } catch(Exception e) {
            assertException(test);
        }
    }

    @Test
    public void testClear() {
        org.javaopen.keydriver.data.Test test = dummyTests.getTest(Keyword.CLEAR);

        Driver d = DriverFactory.getDriver(test);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        d.perform(context, section, test);

        assertSuccess(test);

    }

    @Test
    public void testClearException() {
        org.javaopen.keydriver.data.Test test = dummyTests.getTest(Keyword.CLEAR);

        Driver d = DriverFactory.getDriver(test);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        doThrow(new RuntimeException()).when(element).clear();
        try {
            d.perform(context, section, test);
            assertThat(null, false);
        } catch (Exception e) {
            assertException(test);
        }
    }

    @Test
    public void testSelect() {
        org.javaopen.keydriver.data.Test test = dummyTests.getTest(Keyword.SELECT);

        Driver d = DriverFactory.getDriver(test);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        // prepare select element
        when(element.getTagName()).thenReturn("select");
        WebElement option = mock(WebElement.class);
        when(option.getAttribute(anyObject())).thenReturn("1");
        when(element.findElements(anyObject())).thenReturn(Arrays.asList(option));

        d.perform(context, section, test);

        assertSuccess(test);
    }

    @Test
    public void testSelectException() {
        org.javaopen.keydriver.data.Test test = dummyTests.getTest(Keyword.SELECT);

        Driver d = DriverFactory.getDriver(test);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        doThrow(new RuntimeException()).when(element).findElements(anyObject());
        try {
            d.perform(context, section, test);
            assertThat(null, false);
        } catch (Exception e) {
            assertException(test);
        }
    }

    @Test
    public void testCapture() {
        org.javaopen.keydriver.data.Test test = dummyTests.getTest(Keyword.CAPTURE);

        Driver d = DriverFactory.getDriver(test);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        d.perform(context, section, test);

        assertSuccess(test);
    }

    @Test
    public void testCaptureException() {
        org.javaopen.keydriver.data.Test test = dummyTests.getTest(Keyword.CAPTURE);

        Driver d = DriverFactory.getDriver(test);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        doThrow(new RuntimeException()).when((TakesScreenshot)driver).getScreenshotAs(anyObject());
        try {
            d.perform(context, section, test);
            assertThat(null, false);
        } catch (Exception e) {
            assertException(test);
        }
    }

    @Test
    public void testAccept() {
        org.javaopen.keydriver.data.Test test = dummyTests.getTest(Keyword.ACCEPT);

        Driver d = DriverFactory.getDriver(test);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        d.perform(context, section, test);

        assertSuccess(test);
    }

    @Test
    public void testAcceptException() {
        org.javaopen.keydriver.data.Test test = dummyTests.getTest(Keyword.ACCEPT);

        Driver d = DriverFactory.getDriver(test);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        doThrow(new RuntimeException()).when(driver).switchTo();
        try {
            d.perform(context, section, test);
            assertThat(null, false);
        } catch (Exception e) {
            assertException(test);
        }
    }

    @Test
    public void testDismiss() {
        org.javaopen.keydriver.data.Test test = dummyTests.getTest(Keyword.DISMISS);

        Driver d = DriverFactory.getDriver(test);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        d.perform(context, section, test);

        assertSuccess(test);
    }

    @Test
    public void testDismissException() {
        org.javaopen.keydriver.data.Test test = dummyTests.getTest(Keyword.DISMISS);

        Driver d = DriverFactory.getDriver(test);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        doThrow(new RuntimeException()).when(driver).switchTo();
        try {
            d.perform(context, section, test);
            assertThat(null, false);
        } catch (Exception e) {
            assertException(test);
        }
    }

    @Test
    public void testUpload() {
        org.javaopen.keydriver.data.Test test = dummyTests.getTest(Keyword.UPLOAD);

        Driver d = DriverFactory.getDriver(test);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        d.perform(context, section, test);

        assertSuccess(test);
    }

    @Test
    public void testUploadException() {
        org.javaopen.keydriver.data.Test test = dummyTests.getTest(Keyword.UPLOAD);

        Driver d = DriverFactory.getDriver(test);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        doThrow(new RuntimeException()).when(element).sendKeys(anyObject());
        try {
            d.perform(context, section, test);
            assertThat(null, false);
        } catch (Exception e) {
            assertException(test);
        }
    }

    @Test
    public void testAssert() {
        org.javaopen.keydriver.data.Test test = dummyTests.getTest(Keyword.ASSERT);

        Driver d = DriverFactory.getDriver(test);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        when(element.isEnabled()).thenReturn(true);

        d.perform(context, section, test);

        assertSuccess(test);
    }

    @Test
    public void testAssertFailed() {
        org.javaopen.keydriver.data.Test test = dummyTests.getTest(Keyword.ASSERT);

        Driver d = DriverFactory.getDriver(test);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        when(element.isEnabled()).thenReturn(false);
        try {
            d.perform(context, section, test);
            assertThat(null, false);
        } catch (Exception e) {
            assertException(test);
        }
    }

    @Test
    public void testAssertExpectingFailure() {
        org.javaopen.keydriver.data.Test test = dummyTests.getTests().stream()
            .filter(x -> x.getKeyword().equals(Keyword.ASSERT) && x.getArgument().getTag().equals(Matches.FAIL))
            .findFirst()
            .orElse(null);

        Driver d = DriverFactory.getDriver(test);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        when(element.isEnabled()).thenReturn(false);
        try {
            d.perform(context, section, test);
            assertThat(null, false);
        } catch (Exception e) {
            assertThat(test.isExecuted(), is(true));
            assertThat(test.getStart(), is(not(nullValue())));
            assertThat(test.getEnd(), is(not(nullValue())));
            assertThat(test.getEnd(), is(greaterThanOrEqualTo(test.getStart())));
            assertThat(test.isSuccess(), is(false));
            assertThat(test.isExpectingFailure(), is(true));
            assertThat(test.getStackTrace(), is(not(nullValue())));
        }
    }

    @Test
    public void testAssertException() {
        testAssertFailed();
    }
}
