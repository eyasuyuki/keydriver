package org.javaopen.keydriver.browser;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverLogLevel;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.safari.SafariDriver;

import static org.javaopen.keydriver.browser.Browser.CHROME;
import static org.javaopen.keydriver.browser.Browser.EDGE;
import static org.javaopen.keydriver.browser.Browser.FIREFOX;
import static org.javaopen.keydriver.browser.Browser.IE;
import static org.javaopen.keydriver.browser.Browser.SAFARI;

public class WebDriverFactory {
    public static final String BROWSER_KEY = "browser";
    private static WebDriver webdriver;

    public static WebDriver getInstance(String browser) {
        if (webdriver != null) {
            return webdriver;
        }
        if (CHROME.getName().equals(browser)) {
            ChromeOptions options = new ChromeOptions();
            options.setLogLevel(ChromeDriverLogLevel.SEVERE);
            webdriver = new ChromeDriver(options);
        } else if (FIREFOX.getName().equals(browser)) {
            webdriver = new FirefoxDriver();
        } else if (EDGE.getName().equals(browser)) {
            webdriver = new EdgeDriver();
        } else if (SAFARI.getName().equals(browser)) {
            webdriver = new SafariDriver();
        } else if (IE.getName().equals(browser)) {
            InternetExplorerOptions options = new InternetExplorerOptions();
            options.attachToEdgeChrome();
            webdriver = new InternetExplorerDriver(options);
        } else {
            throw new IllegalArgumentException(browser);
        }
        return webdriver;
    }
}
