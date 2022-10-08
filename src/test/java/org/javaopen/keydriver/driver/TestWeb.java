package org.javaopen.keydriver.driver;

import org.javaopen.keydriver.data.Section;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
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
    private Context context;
    private WebDriver driver;
    private WebElement element;

    @Before
    public void setUp() {
        Locale.setDefault(Locale.US);

        context = new Context();
        driver = mock(WebDriver.class, withSettings().extraInterfaces(TakesScreenshot.class));//TODO
        element = mock(WebElement.class);
        context.setDriver(driver);
        // TODO when
        when(((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE))
            .thenReturn(new File("./src/test/resources/screenshot.png"));
        when(driver.findElement(anyObject())).thenReturn(element);
    }

    @Test
    public void testOpen() {
        Map<String, String> map = Stream.of(new String[][] {
                { "No", "1" },
                { "Keyword", "open" },
                { "Target", "url[https://www.example.com]" },
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        org.javaopen.keydriver.data.Test test = new org.javaopen.keydriver.data.Test(context, map);

        Driver d = DriverFactory.getDriver(test);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        d.perform(context, section, test);

        assertThat(test.getStart(), is(not(nullValue())));
        assertThat(test.getEnd(), is(not(nullValue())));
        assertThat(test.getEnd(), is(greaterThan(test.getStart())));
        assertThat(test.isCompleted(), is(true));
        assertThat(test.isSuccess(), is(true));
        assertThat(test.isExpectingFailure(), is(false));
        assertThat(test.getStackTrace(), is(nullValue()));
    }

    @Test
    public void testOpenException() {
        Map<String, String> map = Stream.of(new String[][] {
                { "No", "1" },
                { "Keyword", "open" },
                { "Target", "url[abcxyz]" },
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        org.javaopen.keydriver.data.Test test = new org.javaopen.keydriver.data.Test(context, map);

        Driver d = DriverFactory.getDriver(test);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        doThrow(new RuntimeException()).when(driver).get("abcxyz");
        try {
            d.perform(context, section, test);
            assertThat(null, false);//fail if no-exception
        } catch (Exception e) {
            assertThat(test.getStart(), is(not(nullValue())));
            assertThat(test.getEnd(), is(not(nullValue())));
            assertThat(test.getEnd(), is(greaterThan(test.getStart())));
            assertThat(test.isCompleted(), is(false));
            assertThat(test.isSuccess(), is(false));
            assertThat(test.isExpectingFailure(), is(false));
            assertThat(test.getStackTrace(), is(not(nullValue())));
        }
    }

    @Test
    public void testClick() {
        Map<String, String> map = Stream.of(new String[][] {
                { "No", "2" },
                { "Keyword", "click" },
                { "Target", "Button" },
                { "Argument", "" },
                { "Object", "id[send_button]" },
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        org.javaopen.keydriver.data.Test test = new org.javaopen.keydriver.data.Test(context, map);

        Driver d = DriverFactory.getDriver(test);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        d.perform(context, section, test);

        assertThat(test.getStart(), is(not(nullValue())));
        assertThat(test.getEnd(), is(not(nullValue())));
        assertThat(test.getEnd(), is(greaterThan(test.getStart())));
        assertThat(test.isCompleted(), is(true));
        assertThat(test.isSuccess(), is(true));
        assertThat(test.isExpectingFailure(), is(false));
        assertThat(test.getStackTrace(), is(nullValue()));
    }

    @Test
    public void testClickException() {
        Map<String, String> map = Stream.of(new String[][] {
                { "No", "2" },
                { "Keyword", "click" },
                { "Target", "Button" },
                { "Argument", "" },
                { "Object", "id[send_button]" },
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        org.javaopen.keydriver.data.Test test = new org.javaopen.keydriver.data.Test(context, map);

        Driver d = DriverFactory.getDriver(test);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        doThrow(new RuntimeException()).when(element).click();
        try {
            d.perform(context, section, test);
            assertThat(null, false);
        } catch (Exception e) {
            assertThat(test.getStart(), is(not(nullValue())));
            assertThat(test.getEnd(), is(not(nullValue())));
            assertThat(test.getEnd(), is(greaterThan(test.getStart())));
            assertThat(test.isCompleted(), is(false));
            assertThat(test.isSuccess(), is(false));
            assertThat(test.isExpectingFailure(), is(false));
            assertThat(test.getStackTrace(), is(not(nullValue())));
        }
    }

    @Test
    public void testInput() {
        Map<String, String> map = Stream.of(new String[][] {
                { "No", "2" },
                { "Keyword", "input" },
                { "Target", "Textbox" },
                { "Argument", "Metal Gear Solid" },
                { "Object", "name[q]" },
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        org.javaopen.keydriver.data.Test test = new org.javaopen.keydriver.data.Test(context, map);

        Driver d = DriverFactory.getDriver(test);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        d.perform(context, section, test);

        assertThat(test.getStart(), is(not(nullValue())));
        assertThat(test.getEnd(), is(not(nullValue())));
        assertThat(test.getEnd(), is(greaterThan(test.getStart())));
        assertThat(test.isCompleted(), is(true));
        assertThat(test.isSuccess(), is(true));
        assertThat(test.isExpectingFailure(), is(false));
        assertThat(test.getStackTrace(), is(nullValue()));
    }

    @Test
    public void testInputException() {
        Map<String, String> map = Stream.of(new String[][] {
                { "No", "2" },
                { "Keyword", "input" },
                { "Target", "Textbox" },
                { "Argument", "Metal Gear Solid" },
                { "Object", "name[q]" },
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        org.javaopen.keydriver.data.Test test = new org.javaopen.keydriver.data.Test(context, map);

        Driver d = DriverFactory.getDriver(test);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        doThrow(new RuntimeException()).when(element).sendKeys(anyObject());
        try {
            d.perform(context, section, test);
            assertThat(null, false);
        } catch(Exception e) {
            assertThat(test.getStart(), is(not(nullValue())));
            assertThat(test.getEnd(), is(not(nullValue())));
            assertThat(test.getEnd(), is(greaterThan(test.getStart())));
            assertThat(test.isCompleted(), is(false));
            assertThat(test.isSuccess(), is(false));
            assertThat(test.isExpectingFailure(), is(false));
            assertThat(test.getStackTrace(), is(not(nullValue())));
        }
    }

    @Test
    public void testClear() {
        Map<String, String> map = Stream.of(new String[][] {
                { "No", "2" },
                { "Keyword", "clear" },
                { "Target", "Input" },
                { "Object", "id[name_text]" },
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        org.javaopen.keydriver.data.Test test = new org.javaopen.keydriver.data.Test(context, map);

        Driver d = DriverFactory.getDriver(test);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        d.perform(context, section, test);

        assertThat(test.getStart(), is(not(nullValue())));
        assertThat(test.getEnd(), is(not(nullValue())));
        assertThat(test.getEnd(), is(greaterThan(test.getStart())));
        assertThat(test.isCompleted(), is(true));
        assertThat(test.isSuccess(), is(true));
        assertThat(test.isExpectingFailure(), is(false));
        assertThat(test.getStackTrace(), is(nullValue()));
    }

    @Test
    public void testClearException() {
        Map<String, String> map = Stream.of(new String[][] {
                { "No", "2" },
                { "Keyword", "clear" },
                { "Target", "Input" },
                { "Object", "id[name_text]" },
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        org.javaopen.keydriver.data.Test test = new org.javaopen.keydriver.data.Test(context, map);

        Driver d = DriverFactory.getDriver(test);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        doThrow(new RuntimeException()).when(element).clear();
        try {
            d.perform(context, section, test);
            assertThat(null, false);
        } catch (Exception e) {
            assertThat(test.getStart(), is(not(nullValue())));
            assertThat(test.getEnd(), is(not(nullValue())));
            assertThat(test.getEnd(), is(greaterThan(test.getStart())));
            assertThat(test.isCompleted(), is(false));
            assertThat(test.isSuccess(), is(false));
            assertThat(test.isExpectingFailure(), is(false));
            assertThat(test.getStackTrace(), is(not(nullValue())));
        }
    }

    @Test
    public void testSelect() {
    }

    @Test
    public void testSelectException() {
    }

    @Test
    public void testCapture() {
    }

    @Test
    public void testCaptureException() {
    }

    @Test
    public void testAccept() {
    }

    @Test
    public void testAcceptException() {
    }

    @Test
    public void testDismiss() {
    }

    @Test
    public void testDismissException() {
    }

    @Test
    public void testUpload() {
    }

    @Test
    public void testUploadException() {
    }

    @Test
    public void testAssert() {
    }

    @Test
    public void testAssertFailed() {
    }

    @Test
    public void testAssertExpectingFailure() {
    }

    @Test
    public void testAssertException() {
    }
}
