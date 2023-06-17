package org.javaopen.keydriver.driver;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.SystemConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang.StringUtils;
import org.javaopen.keydriver.data.Section;
import org.javaopen.keydriver.data.TestCase;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

public class Context {
    public static final String CONFIG = "config";

    // keyword
    public static final String SECTION_KEY = "keydriver.header.section";
    public static final String NUMBER_KEY = "keydriver.header.number";
    public static final String KEYWORD_KEY = "keydriver.header.keyword";
    public static final String TARGET_KEY = "keydriver.header.target";
    public static final String ARGUMENT_KEY = "keydriver.header.argument";
    public static final String COMMENT_KEY = "keydriver.header.comment";
    public static final String OBJECT_KEY = "keydriver.header.object";
    public static final String OPTION_KEY = "keydriver.header.option";
    public static final List<String> KEYS = Arrays.asList(new String[]{
            SECTION_KEY,
            NUMBER_KEY,
            KEYWORD_KEY,
            TARGET_KEY,
            ARGUMENT_KEY,
            COMMENT_KEY,
            OBJECT_KEY,
            OPTION_KEY});

    // browser
    public static final String BROWSER_KEY = "keydriver.browser";
    public static final String BROWSER_WAIT_KEY = "keydriver.browser.wait";
    public static final String AUTO_CAPTURE_KEY = "keydriver.auto.capture";
    public static final String BROWSER_QUIT_KEY = "keydriver.browser.quit";
    public static final String EDGE_DRIVER_PATH_KEY = "keydriver.edge.driver.path";
    public static final String IE_DRIVER_PATH_KEY = "keydriver.ie.driver.path";

    // Param
    public static final String VALUE_HEAD_KEY = "keydriver.value.head";
    public static final String VALUE_TAIL_KEY = "keydriver.value.tail";
    public static final String ATTRIBUTE_SEPARATOR_KEY = "keydriver.attribute.separator";
    public static final String ERROR_SUFFIX_KEY = "keydriver.error.suffix";

    // Reader
    public static final String SKIP_SHEETS_KEY = "keybriver.skip.sheets";
    public static final String SKIP_HEADERS_KEY = "keydriver.skip.headers";

    // Report
    public static final String OUTPUT_DIRECTORY_KEY = "keydriver.output.directory";
    public static final String OUTPUT_EXTENSION_KEY = "keydriver.output.extension";
    public static final String OUTPUT_PREFIX_KEY = "keydriver.output.prefix";
    public static final String SUMMARY_SHEET_NAME_KEY = "keydriver.summary.sheet.name";
    public static final String ERROR_SHEET_PREFIX_KEY = "keydriver.error.sheet.prefix";
    public static final String TIMESTAMP_FORMAT_KEY = "keydriver.timestamp.format";
    public static final String TEST_FILE_NAME_LABEL_KEY = "keydriver.test.file.name.label";
    public static final String EXPECTING_TEST_COUNT_LABEL_KEY = "keydriver.expecting.test.count.label";
    public static final String EXPECTING_FAILURE_COUNT_LABEL_KEY = "keydriver.expecting.failure.count.label";
    public static final String EXECUTED_TEST_COUNT_LABEL_KEY = "keydriver.executed.test.count.label";

    public static final String SUCCESS_TEST_COUNT_LABEL_KEY = "keydriver.success.test.count.label";
    public static final String FAILED_TEST_COUNT_LABEL_KEY = "keydriver.failed.test.count.label";
    public static final String NOT_EXECUTED_TEST_COUNT_KEY = "keydriver.not.executed.test.count";
    public static final String START_TIME_LABEL_KEY = "keydriver.start.time.label";
    public static final String DURATION_LABEL_KEY = "keydriver.duration.label";
    public static final String ARCH_LABEL_KEY = "keydriver.arch.label";
    public static final String PROCESSOR_COUNT_LABEL_KEY = "keydriver.processor.count.label";
    public static final String LOAD_AVERAGE_LABEL_KEY = "keydriver.load.average.label";
    public static final String MAX_MEMORY_LABEL_KEY = "keydriver.max.memory.label";
    public static final String FREE_MEMORY_LABEL_KEY = "keydriver.free.memory.label";
    public static final String TOTAL_MEMORY_LABEL_KEY = "keydriver.total.memory.label";
    public static final String USABLE_DISK_LABEL_KEY = "keydriver.usable.disk.label";
    public static final String FREE_DISK_LABEL_KEY = "keydriver.free.disk.label";
    public static final String TOTAL_DISK_LABEL_KEY = "keydriver.total.disk.label";

    public static final String JDBC_DRIVER_PATH = "keydriver.jdbc.driver.path";
    public static final String JDBC_CLASS_NAME = "keydriver.jdbc.class.name";

    private static Context context;
    public static Context getContext(String configPath, String driverPath, String jdbcClassName) {
        if (context == null) {
            context = new Context(configPath, driverPath, jdbcClassName);
        }
        return context;
    }
    private WebDriver webDriver;
    private Connection connection;
    private ResourceBundle bundle = ResourceBundle.getBundle(CONFIG);
    private PropertiesConfiguration config = new PropertiesConfiguration();
    private Map<String, String> dic = new HashMap<>();
    private String inputFileName;
    private Map<String, Section> sectionMap = new HashMap<>();

    private Context(String configPath, String driverPath, String jdbcClassName) {
        Collections.list(bundle.getKeys()).forEach(x -> config.setProperty(x, bundle.getObject(x)));
        if (StringUtils.isNotEmpty(driverPath)) {
            config.setProperty(JDBC_DRIVER_PATH, driverPath);
        }
        if (StringUtils.isNotEmpty(jdbcClassName)) {
            config.setProperty(JDBC_CLASS_NAME, jdbcClassName);
        }
        read(configPath);
        for (String k: KEYS) {
            String s = bundle.getString(k);
            if (StringUtils.isEmpty(s)) {
                s = k;
            }
            dic.put(k, s);
        }
    }

    private void read(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return;
        }
        Configurations configurations = new Configurations();
        try {
            Configuration cfg = configurations.properties(new File(filePath));
            StreamSupport.stream(Spliterators.spliteratorUnknownSize(cfg.getKeys(), Spliterator.ORDERED), false).forEach(x -> config.setProperty(x, cfg.getProperty(x)));
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
        // override by system configuration
        SystemConfiguration systemConfiguration = new SystemConfiguration();
        Iterator<String> keys = systemConfiguration.getKeys("keydriver");
        keys.forEachRemaining(k -> config.setProperty(k, systemConfiguration.getString(k)));
    }

    public Driver getDriver(TestCase testCase) {
        return DriverFactory.getDriver(testCase);
    }

    public PropertiesConfiguration getConfig() {
        return config;
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public void setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    private ResourceBundle getBundle() {
        return bundle;
    }

    public Map<String, String> getDic() {
        return dic;
    }

    public String getInputFileName() {
        return inputFileName;
    }

    public void setInputFileName(String inputFileName) {
        this.inputFileName = inputFileName;
    }

    public void setSectionMap(List<Section> sections) {
        for (Section s: sections) {
            sectionMap.put(s.getName(), s);
        }
    }

    public Map<String, Section> getSectionMap() {
        return sectionMap;
    }
}
