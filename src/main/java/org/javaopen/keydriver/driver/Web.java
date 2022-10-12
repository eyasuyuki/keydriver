package org.javaopen.keydriver.driver;


import org.apache.commons.configuration2.convert.PropertyConverter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.javaopen.keydriver.data.DataType;
import org.javaopen.keydriver.data.Keyword;
import org.javaopen.keydriver.data.Matches;
import org.javaopen.keydriver.data.Param;
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
        int wait = PropertyConverter.toInteger(context.getBundle().getObject(BROWSER_WAIT_KEY));

        // auto capture mode
        boolean autoCapture = PropertyConverter.toBoolean(context.getBundle().getObject(AUTO_CAPTURE_KEY));
        // error suffix
        errorSuffix = context.getBundle().getString(ERROR_SUFFIX_KEY);
        if (StringUtils.isEmpty(errorSuffix)) {
            errorSuffix = ERROR_SUFFIX_DEFAULT;
        }

        // getDriver
        WebDriver driver = getDriver(context, wait);

        // get test fields
        Keyword key = test.getKeyword();
        Param target = test.getTarget();
        Param argument = test.getArgument();
        Param object = test.getObject();
        Param option = test.getOption();

        try {
            test.setExecuted(true);
            // perform
            if (key.equals(Keyword.OPEN)) {
                driver.get(target.getValue());
            } else if (key.equals(Keyword.CLICK)) {
                findElement(driver, object).click();
            } else if (key.equals(Keyword.INPUT)) {
                findElement(driver, object).sendKeys(argument.getValue());
            } else if (key.equals(Keyword.CLEAR)) {
                findElement(driver, object).clear();
            } else if (key.equals(Keyword.SELECT)) {
                Select select = new Select(findElement(driver, object));
                select.selectByValue(option.getValue());
            } else if (key.equals(Keyword.ACCEPT)) {
                Alert alert = waitAlert(driver, wait);
                alert.accept();
            } else if (key.equals(Keyword.DISMISS)) {
                Alert alert = waitAlert(driver, wait);
                alert.dismiss();
            } else if (key.equals(Keyword.UPLOAD)) {
                doUpload(driver, argument, object);
            } else if (key.equals(Keyword.ASSERT)) {
                doAssert(section, test, driver, object, argument);
            }
            // manual capture or auto
            if (key.equals(Keyword.CAPTURE) || autoCapture) {
                capture(driver, context, section, test);
            }
            test.setSuccess(true);
            // set end
            test.setEnd(new Timestamp(System.currentTimeMillis()));
        } catch (Throwable t) {
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
            throw new RuntimeException(t);
        }
    }

    @Override
    public void quit(Context context) {
        WebDriver driver = context.getDriver();
        boolean quit = PropertyConverter.toBoolean(context.getBundle().getObject(BROWSER_QUIT_KEY));
        if (driver != null && quit) {
            driver.quit();
        }
    }

    private void doUpload(WebDriver driver, Param argument, Param object) {
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
        findElement(driver, object).sendKeys(filename);
    }

    private void doAssert(Section section, Test test, WebDriver driver, Param object, Param argument) {
        String attribute = DEFAULT_ATTRIBUTE;
        if (object != null && StringUtils.isNotEmpty(object.getAttribute())) {
            attribute = object.getAttribute();
        }
        WebElement element = findElement(driver, object);
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
        WebDriver driver = context.getDriver();
        if (driver == null) {
            String browser = context.getBundle().getString(WebDriverFactory.BROWSER_KEY);
            driver = WebDriverFactory.getInstance(context, browser);
            // set browser wait
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(wait));
            // set driver
            context.setDriver(driver);
        }
        return driver;
    }
    private WebElement findElement(WebDriver driver, Param object) {
        if (driver == null || object == null) {
            return null;
        } else if (object.getTag() == DataType.ID) {
            return driver.findElement(By.id(object.getValue()));
        } else if (object.getTag() == DataType.NAME) {
            return driver.findElement(By.name(object.getValue()));
        } else if (object.getTag() == DataType.XPATH) {
            return driver.findElement(By.xpath(object.getValue()));
        } else {
            throw new IllegalArgumentException(object.toString());
        }
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
