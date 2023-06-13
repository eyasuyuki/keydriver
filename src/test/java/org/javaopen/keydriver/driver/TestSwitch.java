package org.javaopen.keydriver.driver;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.javaopen.keydriver.browser.Browser;
import org.javaopen.keydriver.browser.WebDriverFactory;
import org.javaopen.keydriver.data.Section;
import org.javaopen.keydriver.data.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TestSwitch {

    @Rule
    public WireMockRule rule = new WireMockRule(8888);
    private Context context;
    @Before
    public void setUp() {
        Locale.setDefault(Locale.US);//important

        // context
        context = Context.getContext(null, null, null);
        // use force non-mock WebDriver
        WebDriver webDriver = WebDriverFactory.getInstance(context, Browser.CHROME.getName());
        context.setWebDriver(webDriver);
        // browser quit
        context.getConfig().setProperty(Context.BROWSER_QUIT_KEY, true);

        assertThat(rule.getOptions().portNumber(), is(8888));
        // start mock server
        rule.stubFor(get(urlEqualTo("/main.html")).willReturn(
                aResponse().withBodyFile("main.html")
        ));
        rule.stubFor(get(urlEqualTo("/child.html")).willReturn(
                aResponse().withBodyFile("child.html")
        ));
        rule.stubFor(get(urlEqualTo("/favicon.ico")).willReturn(
                aResponse().withStatus(404)
        ));
        rule.start();
    }

    @Test
    public void testSwitch() {
        Section section = new Section("TestSwitch1");
        section.getTestCaseList().add(new TestCase(context, Stream.of(new String[][] {
                { "No", "1" },
                { "Keyword", "open" },
                { "Target", "url[http://localhost:8888/main.html]" },
        }).collect(Collectors.toMap(data -> ((String[])data)[0], data -> ((String[])data)[1]))));
        section.getTestCaseList().add(new TestCase(context, Stream.of(new String[][] {
                {"No", "2"},
                {"Keyword", "click"},
                {"Target", "Click me"},
                {"Object", "id[child_link]"}
        }).collect(Collectors.toMap(data -> ((String[])data)[0], data-> ((String[])data)[1]))));
        section.getTestCaseList().add(new TestCase(context, Stream.of(new String[][] {
                {"No", "3"},
                {"Keyword", "switch"},
                {"Target", "Child window"},
        }).collect(Collectors.toMap(data -> ((String[])data)[0], data-> ((String[])data)[1]))));
        section.getTestCaseList().add(new TestCase(context, Stream.of(new String[][] {
                {"No", "4"},
                {"Keyword", "input"},
                {"Target", "Text input"},
                {"Argument", "abcdefg"},
                {"Object", "id[input_test]"},
        }).collect(Collectors.toMap(data -> ((String[])data)[0], data-> ((String[])data)[1]))));
        section.getTestCaseList().add(new TestCase(context, Stream.of(new String[][] {
                {"No", "5"},
                {"Keyword", "click"},
                {"Target", "Close button"},
                {"Object", "id[close_button]"},
        }).collect(Collectors.toMap(data -> ((String[])data)[0], data-> ((String[])data)[1]))));
        section.getTestCaseList().add(new TestCase(context, Stream.of(new String[][] {
                {"No", "6"},
                {"Keyword", "switch"},
                {"Target", "Main window"},
        }).collect(Collectors.toMap(data -> ((String[])data)[0], data-> ((String[])data)[1]))));
        section.getTestCaseList().add(new TestCase(context, Stream.of(new String[][] {
                {"No", "7"},
                {"Keyword", "assert"},
                {"Target", "Hidden input"},
                {"Argument", "is[Test me]"},
                {"Object", "id[hidden_1#value]"},
        }).collect(Collectors.toMap(data -> ((String[])data)[0], data-> ((String[])data)[1]))));

        // open
        Driver driver = context.getDriver(section.getTestCaseList().get(0));
        driver.perform(context, section, section.getTestCaseList().get(0));
        // click
        driver = context.getDriver(section.getTestCaseList().get(1));
        driver.perform(context, section, section.getTestCaseList().get(1));
        // switch
        driver = context.getDriver(section.getTestCaseList().get(2));
        driver.perform(context, section, section.getTestCaseList().get(2));
        // input
        driver = context.getDriver(section.getTestCaseList().get(3));
        driver.perform(context, section, section.getTestCaseList().get(3));
        // click
        driver = context.getDriver(section.getTestCaseList().get(4));
        driver.perform(context, section, section.getTestCaseList().get(4));
        // switch
        driver = context.getDriver(section.getTestCaseList().get(5));
        driver.perform(context, section, section.getTestCaseList().get(5));
        // assert
        driver = context.getDriver(section.getTestCaseList().get(6));
        driver.perform(context, section, section.getTestCaseList().get(6));

        // quit
        //driver.quit(context); // crash with mvn test
    }

    @After
    public void tearDown() {}
}
