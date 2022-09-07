package org.javaopen.keydriver.driver;


import org.apache.commons.configuration2.convert.PropertyConverter;
import org.apache.commons.io.FileUtils;
import org.javaopen.keydriver.data.DataType;
import org.javaopen.keydriver.data.Keyword;
import org.javaopen.keydriver.data.Param;
import org.javaopen.keydriver.data.Record;
import org.javaopen.keydriver.data.Section;
import org.javaopen.keydriver.web.WebDriverFactory;
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
import java.time.Duration;
import java.util.logging.Logger;

public class Web implements Driver {
    public static final String BROWSER_WAIT_KEY = "browser_wait";
    public static final String AUTO_CAPTURE_KEY = "auto_capture";
    private Logger logger = Logger.getLogger(Web.class.getName());

    @Override
    public void perform(Context context, Section section, Record record) {
        // set wait
        int wait = PropertyConverter.toInteger(context.getBundle().getObject(BROWSER_WAIT_KEY));

        // auto capture mode
        boolean autoCapture = PropertyConverter.toBoolean(context.getBundle().getObject(AUTO_CAPTURE_KEY));

        // getDriver
        WebDriver driver = getDriver(context, wait);

        // get record fields
        Keyword key = record.getKeyword();
        Param target = record.getTarget();
        Param argument = record.getArgument();
        String comment = record.getComment();
        Param object = record.getObject();
        Param option = record.getOption();

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
        } else if (key.equals(Keyword.ASSERT)) {
                String value = findElement(driver, object).getAttribute("innerText");
                if (!match(value, argument)) {
                    logger.severe("Section: "+section.getName()+", Test: "+comment+" failed: expected: "+argument.getValue()+", but got: "+value);
                }
        }
        // manual capture or auto
        if (key.equals(Keyword.CAPTURE) || autoCapture) {
            capture(driver, context, section, record);
        }
    }

    private WebDriver getDriver(Context context, int wait) {
        WebDriver driver = context.getDriver();
        if (driver == null) {
            String browser = context.getBundle().getString(WebDriverFactory.BROWSER_KEY);
            driver = WebDriverFactory.getInstance(browser);
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
    private File getCaptureFile(Context context, Section section, Record record) {
        String num = String.format("%03d", record.getNumber());
        return new File(section.getName()+"_"+num+".png");
    }

    private void capture(WebDriver driver, Context context, Section section, Record record) {
        File f = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(f, getCaptureFile(context, section, record));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
