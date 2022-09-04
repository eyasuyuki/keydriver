package org.javaopen.keydriver.driver;

import org.apache.commons.lang.StringUtils;
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
    private WebDriver driver;
    private Connection connection;
    private ResourceBundle bundle = ResourceBundle.getBundle(CONFIG);
    private Map<String, String> dic = new HashMap<>();

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

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
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
}
