package org.javaopen.keydriver.driver;

import org.apache.commons.lang.StringUtils;
import org.javaopen.keydriver.data.Keyword;
import org.javaopen.keydriver.data.Test;
import org.javaopen.keydriver.data.Section;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.logging.Logger;

public class Database implements Driver {
    public static final String JDBC_DRIVER_PATH = "jdbc_driver_path";
    public static final String JDBC_CLASS_NAME = "jdbc_class_name";
    private static final Logger logger = Logger.getLogger(Database.class.getName());

    private java.sql.Driver driver;

    private void initDriver(Context context) {
        String path = context.getConfig().getString(JDBC_DRIVER_PATH);
        String classname = context.getConfig().getString(JDBC_CLASS_NAME);
        if (driver != null || StringUtils.isEmpty(path) || StringUtils.isEmpty(classname)) {
            return;
        }
        try {
            URL u = new URL("jar:file:"+path+"!/");
            URLClassLoader loader = new URLClassLoader(new URL[]{ u });
            driver = (java.sql.Driver)Class.forName(classname, true, loader).newInstance();
            DriverManager.registerDriver(driver);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void perform(Context context, Section section, Test test) {
        test.setStart(new Timestamp(System.currentTimeMillis()));
        initDriver(context);
        try (Connection conn = DriverManager.getConnection(test.getOption().getValue())) {
            Statement st = conn.createStatement();
            String sql = test.getObject().getValue();
            if (StringUtils.isEmpty(sql)) {
                sql = test.getArgument().getValue();
            }
            if (test.getKeyword().equals(Keyword.ASSERT)) {
                ResultSet res = st.executeQuery(sql);
                String value = res.getString(0);
                if (!match(value, test.getArgument())) {
                    test.setSuccess(false);
                    test.setMatchFailed("Section: "+section.getName()+", Test: "+ test.getNumber() + " failed: expected: "+test.getArgument().toString()+", but got: "+value);
                    logger.severe(test.getMatchFailed());
                    throw new AssertionError(test.getMatchFailed());
                } else {
                    test.setSuccess(true);
                }
            } else if (test.getKeyword().equals(Keyword.EXECUTE)) {
                st.execute(sql);
                test.setSuccess(true);
            }
            test.setEnd(new Timestamp(System.currentTimeMillis()));
        } catch (Throwable t) {
            test.setSuccess(false);

            StringWriter writer = new StringWriter();
            t.printStackTrace(new PrintWriter(writer));
            test.setStackTrace(writer.toString());

            test.setEnd(new Timestamp(System.currentTimeMillis()));

            throw new RuntimeException(t);
        }
    }

    @Override
    public void quit(Context context) {
        // nothing to do it
    }
}
