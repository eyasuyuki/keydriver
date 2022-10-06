package org.javaopen.keydriver.driver;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

public class TestWeb {
    private Context context;
    private WebDriver driver;
    private WebElement element;

    @Before
    public void setUp() {
        context = new Context();
        driver = mock(WebDriver.class, withSettings().name("elementName"));//TODO
        element = mock(WebElement.class);
        context.setDriver(driver);
        // TODO when
    }

    @Test
    public void testOpen() {
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
