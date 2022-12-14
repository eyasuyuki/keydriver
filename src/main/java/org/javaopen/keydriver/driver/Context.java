package org.javaopen.keydriver.driver;

import org.apache.commons.lang.StringUtils;
import org.javaopen.keydriver.data.Section;
import org.javaopen.keydriver.data.Test;
import org.openqa.selenium.WebDriver;

import java.sql.Connection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class Context {
    public static final String NUMBER_KEY = "number";
    public static final String KEYWORD_KEY = "keyword";
    public static final String TARGET_KEY = "target";
    public static final String ARGUMENT_KEY = "argument";
    public static final String COMMENT_KEY = "comment";
    public static final String OBJECT_KEY = "object";
    public static final String OPTION_KEY = "option";
    public static final List<String> KEYS = Arrays.asList(new String[]{
            NUMBER_KEY,
            KEYWORD_KEY,
            TARGET_KEY,
            ARGUMENT_KEY,
            COMMENT_KEY,
            OBJECT_KEY,
            OPTION_KEY});
    public static final String CONFIG = "config";
    private static final Context context = new Context();
    public static Context getContext() {
        return context;
    }
    private WebDriver webDriver;
    private Connection connection;
    private ResourceBundle bundle = ResourceBundle.getBundle(CONFIG);
    private Map<String, String> dic = new HashMap<>();
    private String inputFileName;
    private Map<String, Section> sectionMap = new HashMap<>();

    public Context() {
        this.getBundle();
        for (String k: KEYS) {
            String s = bundle.getString(k);
            if (StringUtils.isEmpty(s)) {
                s = k;
            }
            dic.put(k, s);
        }
    }

    public Driver getDriver(Test test) {
        return DriverFactory.getDriver(test);
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

    public ResourceBundle getBundle() {
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
