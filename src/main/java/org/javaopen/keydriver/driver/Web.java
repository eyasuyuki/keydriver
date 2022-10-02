package org.javaopen.keydriver.driver;


import org.apache.commons.configuration2.convert.PropertyConverter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.javaopen.keydriver.data.DataType;
import org.javaopen.keydriver.data.Keyword;
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
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Duration;
import java.util.logging.Logger;

public class Web implements Driver {
    public static final String BROWSER_WAIT_KEY = "browser_wait";
    public static final String BROWSER_QUIT_KEY = "browser_quit";
    public static final String AUTO_CAPTURE_KEY = "auto_capture";
    private static final String DEFAULT_ATTRIBUTE = "innerText";
    private Logger logger = Logger.getLogger(Web.class.getName());

    @Override
    public void perform(Context context, Section section, Test test) {
        // set wait
        int wait = PropertyConverter.toInteger(context.getBundle().getObject(BROWSER_WAIT_KEY));

        // auto capture mode
        boolean autoCapture = PropertyConverter.toBoolean(context.getBundle().getObject(AUTO_CAPTURE_KEY));

        // getDriver
        WebDriver driver = getDriver(context, wait);

        // get record fields
        int number = test.getNumber();
        Keyword key = test.getKeyword();
        Param target = test.getTarget();
        Param argument = test.getArgument();
        String comment = test.getComment();
        Param object = test.getObject();
        Param option = test.getOption();

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
        String attribute = object.getAttribute();
        if (StringUtils.isEmpty(attribute)) {
            attribute = DEFAULT_ATTRIBUTE;
        }
        WebElement element = findElement(driver, object);
        String value;
        if (Param.ATTRIBUTE_DISPLAYED.equals(object.getAttribute())) {
            value = Boolean.toString(element.isDisplayed());
        } else if (Param.ATTRIBUTE_ENABLED.equals(object.getAttribute())) {
            value = Boolean.toString(element.isEnabled());
        } else if (Param.ATTRIBUTE_SELECTED.equals(object.getAttribute())) {
            value = Boolean.toString(element.isSelected());
        } else {
            value  = element.getAttribute(attribute);
        }
        if (!match(value, argument)) {
            logger.severe("Section: "+section.getName()+", Test: "+ test.getNumber()+" failed: expected: "+argument.getValue()+", but got: "+value);
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
        if (object.getTag() == DataType.ID) {
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
    private File getCaptureFile(Context context, Section section, Test test) {
        String num = String.format("%03d", test.getNumber());
        return new File(section.getName()+"_"+num+".png");
    }

    private void capture(WebDriver driver, Context context, Section section, Test test) {
        File f = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(f, getCaptureFile(context, section, test));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
