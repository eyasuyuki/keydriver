package org.javaopen.keydriver.browser;

import org.javaopen.keydriver.driver.Context;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverLogLevel;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import static org.javaopen.keydriver.browser.Browser.CHROME;
import static org.javaopen.keydriver.browser.Browser.EDGE;
import static org.javaopen.keydriver.browser.Browser.FIREFOX;
import static org.javaopen.keydriver.browser.Browser.IE;
import static org.javaopen.keydriver.browser.Browser.SAFARI;

public class WebDriverFactory {
    public static final String BROWSER_KEY = "browser";
    public static final String EDGE_EXECUTABLE_KEY = "edge_executable";
    public static final String IE_DRIVER_PATH_KEY = "ie_driver_path";
    public static final String WEBDRIVER_IE_DRIVER = "webdriver.ie.driver";
    private static WebDriver webdriver;

    public static WebDriver getInstance(Context context, String browser) {
        if (webdriver != null) {
            return webdriver;
        }
        if (CHROME.getName().equals(browser)) {
            ChromeOptions options = new ChromeOptions();
            options.setLogLevel(ChromeDriverLogLevel.SEVERE);
            options.addArguments("--remote-allow-origins=*");
            webdriver = new ChromeDriver(options);
        } else if (FIREFOX.getName().equals(browser)) {
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--remote-allow-origins=*");
            webdriver = new FirefoxDriver(options);
        } else if (EDGE.getName().equals(browser)) {
            EdgeOptions options = new EdgeOptions();
            options.addArguments("--remote-allow-origins=*");
            webdriver = new EdgeDriver(options);
        } else if (SAFARI.getName().equals(browser)) {
            webdriver = new SafariDriver();
        } else if (IE.getName().equals(browser)) {
            InternetExplorerOptions options = new InternetExplorerOptions();
            options.setCapability("nativeEvents", false);
            options.setCapability("enablePersistentHover", true);
            options.setCapability("ignoreZoomSetting", true);
            options.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
            options.attachToEdgeChrome();
            String edge = context.getConfig().getString(EDGE_EXECUTABLE_KEY);
            options.withEdgeExecutablePath(edge);
            String path = context.getConfig().getString(IE_DRIVER_PATH_KEY);
            System.setProperty(WEBDRIVER_IE_DRIVER, path);
            webdriver = new InternetExplorerDriver(options);
        } else {
            throw new IllegalArgumentException(browser);
        }
        return webdriver;
    }
}
