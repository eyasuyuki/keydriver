package org.javaopen.keydriver.driver;

import org.javaopen.keydriver.data.Section;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
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
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
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
        driver = mock(WebDriver.class, withSettings().name("elementName").extraInterfaces(TakesScreenshot.class));//TODO
        element = mock(WebElement.class);
        context.setDriver(driver);
        // TODO when
        when(((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE))
            .thenReturn(new File("./src/test/resources/screenshot.png"));
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
    }

    @Test
    public void testOpenException() {
    }

    @Test
    public void testClick() {
    }

    @Test
    public void testClickException() {
    }

    @Test
    public void testInput() {
    }

    @Test
    public void testInputException() {
    }

    @Test
    public void testClear() {
    }

    @Test
    public void testClearException() {
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
    public void testAssertException() {
    }
}
