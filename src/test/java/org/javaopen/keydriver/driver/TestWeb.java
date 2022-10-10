package org.javaopen.keydriver.driver;

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
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private Context context;
    private WebDriver driver;
    private WebElement element;

    @Before
    public void setUp() {
        Locale.setDefault(Locale.US);//important

        context = new Context();
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

        assertSuccess(test);
    }

    private void assertSuccess(org.javaopen.keydriver.data.Test test) {
        assertThat(test.getStart(), is(not(nullValue())));
        assertThat(test.getEnd(), is(not(nullValue())));
        assertThat(test.getEnd(), is(greaterThanOrEqualTo(test.getStart())));
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
            assertException(test);
        }
    }

    private void assertException(org.javaopen.keydriver.data.Test test) {
        assertThat(test.getStart(), is(not(nullValue())));
        assertThat(test.getEnd(), is(not(nullValue())));
        assertThat(test.getEnd(), is(greaterThanOrEqualTo(test.getStart())));
        assertThat(test.isCompleted(), is(false));
        assertThat(test.isSuccess(), is(false));
        assertThat(test.isExpectingFailure(), is(false));
        assertThat(test.getStackTrace(), is(not(nullValue())));
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

        assertSuccess(test);
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
            assertException(test);
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

        assertSuccess(test);
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
            assertException(test);
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

        assertSuccess(test);

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
            assertException(test);
        }
    }

    @Test
    public void testSelect() {
        Map<String, String> map = Stream.of(new String[][]{
                {"No", "5"},
                {"Keyword", "select"},
                {"Target", "Select"},
                {"Argument", "1"},
                {"Object", "id[name_text]"},
                {"Option", "1"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        org.javaopen.keydriver.data.Test test = new org.javaopen.keydriver.data.Test(context, map);

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
        Map<String, String> map = Stream.of(new String[][]{
                {"No", "5"},
                {"Keyword", "select"},
                {"Target", "Select"},
                {"Argument", "1"},
                {"Object", "id[name_text]"},
                {"Option", "1"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        org.javaopen.keydriver.data.Test test = new org.javaopen.keydriver.data.Test(context, map);

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
        Map<String, String> map = Stream.of(new String[][] {
                { "No", "4" },
                { "Keyword", "capture" },
                { "Comment", "Take screen shot"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        org.javaopen.keydriver.data.Test test = new org.javaopen.keydriver.data.Test(context, map);

        Driver d = DriverFactory.getDriver(test);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        d.perform(context, section, test);

        assertSuccess(test);
    }

    @Test
    public void testCaptureException() {
        Map<String, String> map = Stream.of(new String[][] {
                { "No", "4" },
                { "Keyword", "capture" },
                { "Comment", "Take screen shot"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        org.javaopen.keydriver.data.Test test = new org.javaopen.keydriver.data.Test(context, map);

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
        Map<String, String> map = Stream.of(new String[][] {
                { "No", "3" },
                { "Keyword", "accept" },
                { "Comment", "Accept an alert"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        org.javaopen.keydriver.data.Test test = new org.javaopen.keydriver.data.Test(context, map);

        Driver d = DriverFactory.getDriver(test);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        d.perform(context, section, test);

        assertSuccess(test);
    }

    @Test
    public void testAcceptException() {
        Map<String, String> map = Stream.of(new String[][] {
                { "No", "3" },
                { "Keyword", "accept" },
                { "Comment", "Accept an alert"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        org.javaopen.keydriver.data.Test test = new org.javaopen.keydriver.data.Test(context, map);

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
        Map<String, String> map = Stream.of(new String[][] {
                { "No", "3" },
                { "Keyword", "dismiss" },
                { "Comment", "Dismiss an alert"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        org.javaopen.keydriver.data.Test test = new org.javaopen.keydriver.data.Test(context, map);

        Driver d = DriverFactory.getDriver(test);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        d.perform(context, section, test);

        assertSuccess(test);
    }

    @Test
    public void testDismissException() {
        Map<String, String> map = Stream.of(new String[][] {
                { "No", "3" },
                { "Keyword", "dismiss" },
                { "Comment", "Dismiss an alert"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        org.javaopen.keydriver.data.Test test = new org.javaopen.keydriver.data.Test(context, map);

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
        Map<String, String> map = Stream.of(new String[][]{
                {"No", "3"},
                {"Keyword", "upload"},
                {"Target", "input[file]"},
                {"Argument", "/Users/yasuyuki/Documents/playlist.txt"},
                {"Object", "id[file_upload_1]"}
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        org.javaopen.keydriver.data.Test test = new org.javaopen.keydriver.data.Test(context, map);


        Driver d = DriverFactory.getDriver(test);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        d.perform(context, section, test);

        assertSuccess(test);
    }

    @Test
    public void testUploadException() {
        Map<String, String> map = Stream.of(new String[][]{
                {"No", "3"},
                {"Keyword", "upload"},
                {"Target", "input[file]"},
                {"Argument", "/Users/yasuyuki/Documents/playlist.txt"},
                {"Object", "id[file_upload_1]"}
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        org.javaopen.keydriver.data.Test test = new org.javaopen.keydriver.data.Test(context, map);


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
        Map<String, String> map = Stream.of(new String[][]{
                {"No", "7"},
                {"Keyword", "assert"},
                {"Target", "input[text]"},
                {"Argument", "is[true]"},
                {"Object", "id[save_button#enabled]"}
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        org.javaopen.keydriver.data.Test test = new org.javaopen.keydriver.data.Test(context, map);


        Driver d = DriverFactory.getDriver(test);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        when(element.isEnabled()).thenReturn(true);

        d.perform(context, section, test);

        assertSuccess(test);
    }

    @Test
    public void testAssertFailed() {
        Map<String, String> map = Stream.of(new String[][]{
                {"No", "7"},
                {"Keyword", "assert"},
                {"Target", "input[text]"},
                {"Argument", "is[true]"},
                {"Object", "id[save_button#displayed]"}
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        org.javaopen.keydriver.data.Test test = new org.javaopen.keydriver.data.Test(context, map);


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
        Map<String, String> map = Stream.of(new String[][]{
                {"No", "7"},
                {"Keyword", "assert"},
                {"Argument", "fail[]"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        org.javaopen.keydriver.data.Test test = new org.javaopen.keydriver.data.Test(context, map);


        Driver d = DriverFactory.getDriver(test);
        assertThat(d, is(instanceOf(Web.class)));
        Section section = new Section("Section1");

        when(element.isEnabled()).thenReturn(false);
        try {
            d.perform(context, section, test);
            assertThat(null, false);
        } catch (Exception e) {
            assertThat(test.getStart(), is(not(nullValue())));
            assertThat(test.getEnd(), is(not(nullValue())));
            assertThat(test.getEnd(), is(greaterThanOrEqualTo(test.getStart())));
            assertThat(test.isCompleted(), is(false));
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
