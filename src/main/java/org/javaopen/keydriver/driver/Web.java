package org.javaopen.keydriver.driver;


import org.apache.commons.configuration2.convert.PropertyConverter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.javaopen.keydriver.data.DataType;
import org.javaopen.keydriver.data.Keyword;
import org.javaopen.keydriver.data.Matches;
import org.javaopen.keydriver.data.Param;
import org.javaopen.keydriver.data.Tag;
import org.javaopen.keydriver.data.Test;
import org.javaopen.keydriver.data.Section;
import org.javaopen.keydriver.browser.WebDriverFactory;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.List;
import java.util.logging.Logger;

public class Web implements Driver {
    public static final String BROWSER_WAIT_KEY = "browser_wait";
    public static final String BROWSER_QUIT_KEY = "browser_quit";
    public static final String AUTO_CAPTURE_KEY = "auto_capture";
    public static final String ERROR_SUFFIX_KEY = "error_suffix";
    public static final String ERROR_SUFFIX_DEFAULT = "__ERROR__";
    private static final String DEFAULT_ATTRIBUTE = "innerText";
    private Logger logger = Logger.getLogger(Web.class.getName());

    private String errorSuffix;

    @Override
    public void perform(Context context, Section section, Test test) {
        // set test properties
        test.setStart(new Timestamp(System.currentTimeMillis()));
        // set wait
        int wait = context.getConfig().getInt(BROWSER_WAIT_KEY);

        // auto capture mode
        boolean autoCapture = context.getConfig().getBoolean(AUTO_CAPTURE_KEY);
        // error suffix
        errorSuffix = context.getConfig().getString(ERROR_SUFFIX_KEY);
        if (StringUtils.isEmpty(errorSuffix)) {
            errorSuffix = ERROR_SUFFIX_DEFAULT;
        }

        // getDriver
        WebDriver driver = getDriver(context, wait);

        // get test fields
        Keyword key = test.getKeyword();

        try {
            test.setExecuted(true);
            // perform
            dispatch(context, driver, wait, section, test);
            // manual capture or auto
            if (key.equals(Keyword.CAPTURE) || autoCapture) {
                capture(driver, context, section, test);
            }
            test.setSuccess(true);
            // set end
            test.setEnd(new Timestamp(System.currentTimeMillis()));
        } catch (Throwable t) {
            setFailure(context, driver, t, section, test);
            throw new RuntimeException(t);
        }
    }

    private void dispatch(Context context, WebDriver driver, int wait, Section section, Test test) {
        Keyword key = test.getKeyword();
        Param target = test.getTarget();
        Param argument = test.getArgument();
        Param object = test.getObject();
        Param option = test.getOption();

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
        } else if (key.equals(Keyword.ACCEPT)) {
            Alert alert = waitAlert(driver, wait);
            alert.accept();
        } else if (key.equals(Keyword.DISMISS)) {
            Alert alert = waitAlert(driver, wait);
            alert.dismiss();
        } else if (key.equals(Keyword.UPLOAD)) {
            doUpload(driver, argument, object, wait);
        } else if (key.equals(Keyword.ASSERT)) {
            doAssert(section, test, driver, object, argument, wait);
        } else if (key.equals(Keyword._DIRECTIVE)) {
            doDirective(context, argument, object);
        }
    }

    @Override
    public void quit(Context context) {
        WebDriver driver = context.getWebDriver();
        boolean quit = context.getConfig().getBoolean(BROWSER_QUIT_KEY);
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

    private void doAssert(Section section, Test test, WebDriver driver, Param object, Param argument, int wait) {
        String attribute = DEFAULT_ATTRIBUTE;
        if (object != null && StringUtils.isNotEmpty(object.getAttribute())) {
            attribute = object.getAttribute();
        }
        WebElement element = findElement(driver, object, wait);
        test.setExpected(argument.getValue());

        String value = getValue(element, attribute);
        test.setActual(value);

        test.setExpectingFailure(argument != null && argument.getTag() == Matches.FAIL);

        if (!match(value, argument)) {
            test.setSuccess(false);
            if (test.isExpectingFailure()) {
                test.setMatchFailed("Section: "+section.getName()+", Test: "+ test.getNumber() + " failed: expected: "+argument.toString());
            } else {
                test.setMatchFailed("Section: "+section.getName()+", Test: "+ test.getNumber() + " failed: expected: "+argument.toString()+", but got: "+value);
            }
            logger.severe(test.getMatchFailed());
            throw new AssertionError(test.getMatchFailed());
        } else {
            test.setSuccess(true);
        }
    }

    void setFailure(Context context, WebDriver driver, Throwable t, Section section, Test test) {
        test.setSuccess(false);

        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        t.printStackTrace(printWriter);
        test.setStackTrace(writer.toString());

        try {
            capture(driver, context, section, test, true);
        } catch (Exception e) {
            // do nothing
        }

        test.setEnd(new Timestamp(System.currentTimeMillis()));
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
            String browser = context.getConfig().getString(WebDriverFactory.BROWSER_KEY);
            driver = WebDriverFactory.getInstance(context, browser);
            // set browser wait
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(wait));
            // set driver
            context.setWebDriver(driver);
        }
        return driver;
    }
    private WebElement findElement(WebDriver driver, Param object, int wait) {
        By by = null;
        if (driver == null || object == null) {
            return null;
        } else if (object.getTag() == DataType.ID) {
            by = By.id(object.getValue());
        } else if (object.getTag() == DataType.NAME) {
            by = By.name(object.getValue());
        } else if (object.getTag() == DataType.XPATH) {
            by = By.xpath(object.getValue());
        } else if (object.getTag() ==  DataType.CSS) {
            by = By.cssSelector(object.getValue());
        } else {
            throw new IllegalArgumentException(object.toString());
        }
        // find from IFRAMEs
        List<WebElement> frames = driver.findElements(By.tagName("iframe"));
        Duration duration = Duration.ofSeconds(wait);
        WebDriverWait w = new WebDriverWait(driver, duration);
        try {
            return w.until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (Exception e) {
            for (WebElement f: frames) {
                try {
                    return w.until(ExpectedConditions.presenceOfElementLocated(by));
                } catch (Exception ex) {}
            }
        }
        throw new RuntimeException("Element not found. "+object.toString());
    }

    private Alert waitAlert(WebDriver driver, int wait) {
        WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(wait));
        return w.until(ExpectedConditions.alertIsPresent());
    }
    private File getCaptureFile(Context context, Section section, Test test, boolean isError) {
        String suffix = isError ? errorSuffix : "";
        String num = String.format("%03d", test.getNumber());
        return new File(section.getName()+"_"+num+suffix+".png");
    }

    private void capture(WebDriver driver, Context context, Section section, Test test) throws IOException {
        capture(driver, context, section, test, false);
    }

    private void capture(WebDriver driver, Context context, Section section, Test test, boolean isError) throws IOException {
        File f = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(f, getCaptureFile(context, section, test, isError));
    }
}
