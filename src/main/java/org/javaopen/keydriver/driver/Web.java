package org.javaopen.keydriver.driver;


import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.javaopen.keydriver.browser.WebDriverFactory;
import org.javaopen.keydriver.data.DataType;
import org.javaopen.keydriver.data.Keyword;
import org.javaopen.keydriver.data.Matches;
import org.javaopen.keydriver.data.Param;
import org.javaopen.keydriver.data.Section;
import org.javaopen.keydriver.data.Tag;
import org.javaopen.keydriver.data.TestCase;
import org.javaopen.keydriver.data.TestException;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.openqa.selenium.support.ui.ExpectedConditions.numberOfWindowsToBe;

public class Web implements Driver {
    public static final String ERROR_SUFFIX_DEFAULT = "__ERROR__";
    private static final String DEFAULT_ATTRIBUTE = "innerText";
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final List<Tag> ELEMENT_TAGS = Arrays.asList(new Tag[]{DataType.ID, DataType.NAME, DataType.XPATH, DataType.CSS});
    private static final String FULLSCREEN = "fullscreen";

    private String errorSuffix;
    private String rectSepataror;

    @Override
    public void perform(Context context, Section section, TestCase testCase) {
        // set test properties
        testCase.setStart(new Timestamp(System.currentTimeMillis()));
        // set wait
        int wait = context.getConfig().getInt(Context.BROWSER_WAIT_KEY);

        // auto capture mode
        boolean autoCapture = context.getConfig().getBoolean(Context.AUTO_CAPTURE_KEY);
        // error suffix
        errorSuffix = context.getConfig().getString(Context.ERROR_SUFFIX_KEY);
        if (StringUtils.isEmpty(errorSuffix)) {
            errorSuffix = ERROR_SUFFIX_DEFAULT;
        }
        rectSepataror = context.getConfig().getString(Context.RECT_SEPARATOR_KEY, Param.RECT_SEPARATOR_DEFAULT);

        // getDriver
        WebDriver driver = getDriver(context, wait);

        // get test fields
        Keyword key = testCase.getKeyword();

        try {
            testCase.setExecuted(true);
            // perform
            dispatch(context, driver, wait, section, testCase);
            // manual capture or auto
            if (autoCapture) {
                captureWindow(driver, context, section, testCase, false);
            } else if (key.equals(Keyword.CAPTURE)) {
                capture(driver, context, section, testCase, wait);
            }
            testCase.setSuccess(true);
            // set end
            testCase.setEnd(new Timestamp(System.currentTimeMillis()));
        } catch (TestException t) {
            throw t; // to fix origin problem #32
        } catch (Throwable t) {
            setFailure(context, driver, t, section, testCase, wait);
            throw new TestException(t, testCase);
        }
    }

    private void dispatch(Context context, WebDriver driver, int wait, Section section, TestCase testCase) {
        Keyword key = testCase.getKeyword();
        Param target = testCase.getTarget();
        Param argument = testCase.getArgument();
        Param object = testCase.getObject();
        Param option = testCase.getOption();

        if (key.equals(Keyword.OPEN)) {
            driver.get(target.getValue());
        } else if (key.equals(Keyword.CLICK)) {
            findElement(driver, object, wait).click();
        } else if (key.equals(Keyword.INPUT)) {
            findElement(driver, object, wait).sendKeys(argument.getValue());
        } else if (key.equals(Keyword.CLEAR)) {
            findElement(driver, object, wait).clear();
        } else if (key.equals(Keyword.SELECT)) {
            Select select = new Select(findElement(driver, object, wait));
            select.selectByValue(option.getValue());
        } else if (key.equals(Keyword.SWITCH)) {
            doSwitch(driver, argument, wait);
        } else if (key.equals(Keyword.ACCEPT)) {
            Alert alert = waitAlert(driver, wait);
            alert.accept();
        } else if (key.equals(Keyword.DISMISS)) {
            Alert alert = waitAlert(driver, wait);
            alert.dismiss();
        } else if (key.equals(Keyword.UPLOAD)) {
            doUpload(driver, argument, object, wait);
        } else if (key.equals(Keyword.ASSERT)) {
            doAssert(section, testCase, driver, object, argument, wait);
        } else if (key.equals(Keyword._DIRECTIVE)) {
            doDirective(context, argument, object);
        }
    }

    @Override
    public void quit(Context context) {
        WebDriver driver = context.getWebDriver();
        boolean quit = context.getConfig().getBoolean(Context.BROWSER_QUIT_KEY);
        if (driver != null && quit) {
            driver.quit();
        }
    }

    private void doDirective(Context context, Param argument, Param object) {
        Tag tag = null;
        String sheetName = null;
        if (object != null) {
            tag = object.getTag();
            sheetName = object.getValue();
        }
        if (tag == null && argument != null) {
            tag = argument.getTag();
            sheetName = argument.getValue();
        }
        if (tag != null && tag.equals(DataType.SHEET)) {
            Section s = context.getSectionMap().get(sheetName);
            s.run(context);
        }
    }

    private void doUpload(WebDriver driver, Param argument, Param object, int wait) {
        String filename;
        if (argument.getTag().equals(DataType.URL)) {
            try {
                URL url = new URL(argument.getValue());
                File file = new File(url.toURI());
                filename = file.getAbsolutePath();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        } else {
            filename = argument.getValue();
        }
        findElement(driver, object, wait).sendKeys(filename);
    }

    private void doAssert(Section section, TestCase testCase, WebDriver driver, Param object, Param argument, int wait) {
        String attribute = DEFAULT_ATTRIBUTE;
        if (object != null && StringUtils.isNotEmpty(object.getAttribute())) {
            attribute = object.getAttribute();
        }
        WebElement element = findElement(driver, object, wait);
        testCase.setExpected(argument.getValue());

        String value = getValue(element, attribute);
        testCase.setActual(value);

        testCase.setExpectingFailure(argument != null && argument.getTag() == Matches.FAIL);

        if (!match(value, argument)) {
            testCase.setSuccess(false);
            if (testCase.isExpectingFailure()) {
                testCase.setMatchFailed("Section: "+section.getName()+", Test: "+ testCase.getNumber() + " failed: expected: "+argument.toString());
            } else {
                testCase.setMatchFailed("Section: "+section.getName()+", Test: "+ testCase.getNumber() + " failed: expected: "+argument.toString()+", but got: "+value);
            }
            logger.error(testCase.getMatchFailed());
            throw new AssertionError(testCase.getMatchFailed());
        } else {
            testCase.setSuccess(true);
        }
    }

    void setFailure(Context context, WebDriver driver, Throwable t, Section section, TestCase testCase, int wait) {
        testCase.setSuccess(false);

        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        t.printStackTrace(printWriter);
        testCase.setStackTrace(writer.toString());

        try {
            captureWindow(driver, context, section, testCase, true);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        testCase.setEnd(new Timestamp(System.currentTimeMillis()));
    }

    private String getValue(WebElement element, String attribute) {
        if (element == null) {
            return null;
        } else if (Param.ATTRIBUTE_DISPLAYED.equals(attribute)) {
            return Boolean.toString(element.isDisplayed());
        } else if (Param.ATTRIBUTE_ENABLED.equals(attribute)) {
            return Boolean.toString(element.isEnabled());
        } else if (Param.ATTRIBUTE_SELECTED.equals(attribute)) {
            return Boolean.toString(element.isSelected());
        } else {
            return element.getAttribute(attribute);
        }
    }

    private WebDriver getDriver(Context context, int wait) {
        WebDriver driver = context.getWebDriver();
        if (driver == null) {
            String browser = context.getConfig().getString(Context.BROWSER_KEY);
            driver = WebDriverFactory.getInstance(context, browser);
            // set browser wait
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(wait));
            // set driver
            context.setWebDriver(driver);
        }
        return driver;
    }

    private By getBy(Param object) {
        if (object.getTag() == DataType.ID) {
            return By.id(object.getValue());
        } else if (object.getTag() == DataType.NAME) {
            return By.name(object.getValue());
        } else if (object.getTag() == DataType.XPATH) {
            return By.xpath(object.getValue());
        } else if (object.getTag() ==  DataType.CSS) {
            return By.cssSelector(object.getValue());
        } else {
            throw new IllegalArgumentException(object.toString());
        }
    }
    private WebElement findElement(WebDriver driver, Param object, int wait) {
        if (driver == null || object == null) {
            return null;
        }
        // find from IFRAMEs
        Duration duration = Duration.ofSeconds(wait);
        WebDriverWait w = new WebDriverWait(driver, duration);
        try {
            return w.until(ExpectedConditions.presenceOfElementLocated(getBy(object)));
        } catch (Exception e) {
            driver.switchTo().defaultContent();
            List<WebElement> frames = driver.findElements(By.tagName("iframe"));
            for (WebElement f: frames) {
                driver.switchTo().defaultContent();
                driver.switchTo().frame(f);
                try {
                    return w.until(ExpectedConditions.presenceOfElementLocated(getBy(object)));
                } catch (Exception ex) {}
            }
        }
        throw new RuntimeException("Element not found. "+object.toString());
    }

    private void doSwitch(WebDriver driver, Param argument, int wait) {
        WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(wait));
        String current = "";
        // for NoSuchWindowException
        try {
            current = driver.getWindowHandle();
            w.until(numberOfWindowsToBe(2));
        } catch (Exception e) {
            logger.info(e.getLocalizedMessage());
        }
        if (argument != null && StringUtils.isNotEmpty(current) && current.equals(argument.getValue())) {
            return;
        }
        Set<String> handles = driver.getWindowHandles();
        logger.info("handles="+handles.toString());
        for (String h: handles) {
            if (argument != null && h.contentEquals(argument.getValue())) {
                driver.switchTo().window(h);
                break;
            }
            if (h.contentEquals(current)) {
                continue;
            }
            // switch to another window
            driver.switchTo().window(h);
            break;
        }
    }

    private Alert waitAlert(WebDriver driver, int wait) {
        WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(wait));
        return w.until(ExpectedConditions.alertIsPresent());
    }
    private File getCaptureFile(Context context, Section section, TestCase testCase, boolean isError) {
        String suffix = isError ? errorSuffix : "";
        String num = String.format("%03d", testCase.getNumber());
        return new File(section.getName()+"_"+num+suffix+".png");
    }

    private void capture(WebDriver driver, Context context, Section section, TestCase testCase, int wait) {
        Param param = testCase.getObject();
        if (param == null) {
            captureWindow(driver, context, section, testCase, false);
        } else if (ELEMENT_TAGS.contains(param.getTag())) {
            captureElement(driver, context, section, testCase, wait);
        } else if (DataType.TEXT.equals(param.getTag()) && FULLSCREEN.equals(param.getValue())) {
            java.awt.Dimension screenRect = Toolkit.getDefaultToolkit().getScreenSize();
            new Rectangle(screenRect);
            captureFullscreen(driver, context, section, testCase);
        } else if (DataType.RECT.equals(param.getTag())) {
            captureRect(driver, context, section, testCase);
        } else {
            captureWindow(driver, context, section, testCase, false);
        }
    }

    private void captureWindow(WebDriver driver, Context context, Section section, TestCase testCase, boolean isError) {
        Rectangle rect = getWindowRect(driver);
        capture(driver, context, section, testCase, isError, rect);
    }

    private void captureElement(WebDriver driver, Context context, Section section, TestCase testCase, int wait) {
        try {
            WebElement element = findElement(driver, testCase.getObject(), wait);
            File f = element.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(f, getCaptureFile(context, section, testCase, false));
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    private void captureFullscreen(WebDriver driver, Context context, Section section, TestCase testCase) {
        java.awt.Dimension screenRect = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle rect = new Rectangle(screenRect);
        capture(driver, context, section, testCase, false, rect);
    }

    private void captureRect(WebDriver driver, Context context, Section section, TestCase testCase) {
        Rectangle rect = getRect(testCase.getObject());
        capture(driver, context, section, testCase, false, rect);
    }

    private void capture(WebDriver driver, Context context, Section section, TestCase testCase, boolean isError, Rectangle rect)  {
        // for NoSuchWindowException
        try {
            final Robot robot = new Robot();
            BufferedImage image = robot.createScreenCapture(rect);
            File f = getCaptureFile(context, section, testCase, isError);
            ImageIO.write(image, "png", f);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    private Rectangle getWindowRect(WebDriver driver) {
        Point point = driver.manage().window().getPosition();
        Dimension dim = driver.manage().window().getSize();
        return new Rectangle(point.getX(), point.getY(), dim.getWidth(), dim.getHeight());
    }

    private Rectangle getElementRect(WebDriver driver, Param param, int wait) {
        WebElement element = findElement(driver, param, wait);
        org.openqa.selenium.Rectangle r = element.getRect();
        // TODO cenvert screen coordinate
        return new Rectangle(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    private Rectangle getRect(Param param) {
        final String[] vals = param.getValue().split(rectSepataror);
        final int x = Integer.parseInt(vals[0]);
        final int y = Integer.parseInt(vals[1]);
        final int w = Integer.parseInt(vals[2]);
        final int h = Integer.parseInt(vals[3]);
        return new Rectangle(x, y, w, h);
    }
}
