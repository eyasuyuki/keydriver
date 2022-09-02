package org.javaopen.keydriver.web;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import static org.javaopen.keydriver.web.Browser.CHROME;
import static org.javaopen.keydriver.web.Browser.EDGE;
import static org.javaopen.keydriver.web.Browser.FIREFOX;
import static org.javaopen.keydriver.web.Browser.SAFARI;

public class WebDriverFactory {
    private static WebDriver webdriver;

    public static WebDriver getInstance(String browser) {
        if (webdriver != null) {
            return webdriver;
        }
        if (CHROME.getName().equals(browser)) {
            webdriver = new ChromeDriver();
        } else if (FIREFOX.getName().equals(browser)) {
            webdriver = new FirefoxDriver();
        } else if (EDGE.getName().equals(browser)) {
            webdriver = new EdgeDriver();
        } else if (SAFARI.getName().equals(browser)) {
            webdriver = new SafariDriver();
        } else {
            throw new IllegalArgumentException(browser);
        }
        return webdriver;
    }
}
