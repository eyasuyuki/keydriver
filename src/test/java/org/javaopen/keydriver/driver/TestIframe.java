package org.javaopen.keydriver.driver;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.javaopen.keydriver.browser.Browser;
import org.javaopen.keydriver.browser.WebDriverFactory;
import org.javaopen.keydriver.data.Section;
import org.javaopen.keydriver.data.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TestIframe {

    @Rule
    public WireMockRule rule = new WireMockRule(8888);

    private Context context;

    @Before
    public void setUp() {
        Locale.setDefault(Locale.US);//important
        // context
        context = Context.createContext(null, null, null);
        // use force non-mock WebDriver
        WebDriver webDriver = WebDriverFactory.getInstance(context, Browser.CHROME.getName());
        context.setWebDriver(webDriver);
        // browser quit
        context.getConfig().setProperty(Context.BROWSER_QUIT_KEY, true);

        assertThat(rule.getOptions().portNumber(), is(8888));
        // start mock server
        rule.stubFor(get(urlEqualTo("/index.html")).willReturn(
                aResponse().withBodyFile("index.html")
        ));
        rule.stubFor(get(urlEqualTo("/frame1.html")).willReturn(
                aResponse().withBodyFile("frame1.html")
        ));
        rule.stubFor(get(urlEqualTo("/frame2.html")).willReturn(
                aResponse().withBodyFile("frame2.html")
        ));
        rule.stubFor(get(urlEqualTo("/favicon.ico")).willReturn(
            aResponse().withStatus(404)
        ));
        rule.start();
    }

    @Test
    public void testServe() {
        final String url = "http://localhost:8888/index.html";
        final String frame1Url = "http://localhost:8888/frame1.html";
        final String frame2Url = "http://localhost:8888/frame2.html";

        final String indexPath = "src/test/resources/__files/index.html";
        final String frame1Path = "src/test/resources/__files/frame1.html";
        final String frame2Path = "src/test/resources/__files/frame2.html";
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpGet get = new HttpGet(url);
            HttpEntity entity = client.execute(get).getEntity();
            assertThat(EntityUtils.toString(entity, "UTF-8"), is(new String(Files.readAllBytes(Path.of(indexPath)))));
            get = new HttpGet(frame1Url);
            entity = client.execute(get).getEntity();
            assertThat(EntityUtils.toString(entity, "UTF-8"), is(new String(Files.readAllBytes(Path.of(frame1Path)))));
            get = new HttpGet(frame2Url);
            entity = client.execute(get).getEntity();
            assertThat(EntityUtils.toString(entity, "UTF-8"), is(new String(Files.readAllBytes(Path.of(frame2Path)))));
        } catch (Exception e) {
            throw new RuntimeException("url: "+url, e);
        }
    }



    @Test
    public void testFindElementFromIframe() {
        Section section = new Section("Sheet1");
        section.getTestCaseList().add(new TestCase(context, Stream.of(new String[][] {
                { "No", "1" },
                { "Keyword", "open" },
                { "Target", "url[http://localhost:8888/index.html]" },
        }).collect(Collectors.toMap(data -> ((String[])data)[0], data -> ((String[])data)[1]))));
        section.getTestCaseList().add(new TestCase(context, Stream.of(new String[][] {
                {"No", "2"},
                {"Keyword", "input"},
                {"Target", "textbox"},
                {"Argument", "This is it"},
                {"Object", "id[name]"}
        }).collect(Collectors.toMap(data -> ((String[])data)[0], data-> ((String[])data)[1]))));
        section.getTestCaseList().add(new TestCase(context, Stream.of(new String[][] {
                {"No", "3"},
                {"Keyword", "click"},
                {"Target", "button"},
                {"Object", "id[send]"}
        }).collect(Collectors.toMap(data -> ((String[])data)[0], data-> ((String[])data)[1]))));
        section.getTestCaseList().add(new TestCase(context, Stream.of(new String[][] {
                {"No", "4"},
                {"Keyword", "assert"},
                {"Target", "button"},
                {"Argument", "is[That's it]"},
                {"Object", "id[hidden_0]"}
        }).collect(Collectors.toMap(data -> ((String[])data)[0], data-> ((String[])data)[1]))));

        // open
        Driver driver = context.getDriver(section.getTestCaseList().get(0));
        driver.perform(context, section, section.getTestCaseList().get(0));
        // input
        driver = context.getDriver(section.getTestCaseList().get(1));
        driver.perform(context, section, section.getTestCaseList().get(1));
        // click
        driver = context.getDriver(section.getTestCaseList().get(2));
        driver.perform(context, section, section.getTestCaseList().get(2));

        // quit
        //driver.quit(context); // crash with mvn test
    }

    @After
    public void tearDown() {}
}
