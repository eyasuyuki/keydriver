package org.javaopen.keydriver.driver;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
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
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

public class Context {
    public static final String SECTION_KEY = "section";
    public static final String NUMBER_KEY = "number";
    public static final String KEYWORD_KEY = "keyword";
    public static final String TARGET_KEY = "target";
    public static final String ARGUMENT_KEY = "argument";
    public static final String COMMENT_KEY = "comment";
    public static final String OBJECT_KEY = "object";
    public static final String OPTION_KEY = "option";
    public static final List<String> KEYS = Arrays.asList(new String[]{
            SECTION_KEY,
            NUMBER_KEY,
            KEYWORD_KEY,
            TARGET_KEY,
            ARGUMENT_KEY,
            COMMENT_KEY,
            OBJECT_KEY,
            OPTION_KEY});
    public static final String CONFIG = "config";
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
            config.setProperty(Database.JDBC_DRIVER_PATH, driverPath);
        }
        if (StringUtils.isNotEmpty(jdbcClassName)) {
            config.setProperty(Database.JDBC_CLASS_NAME, jdbcClassName);
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
