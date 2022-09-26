package org.javaopen.keydriver.driver;

import org.apache.commons.lang.StringUtils;
import org.javaopen.keydriver.data.Keyword;
import org.javaopen.keydriver.data.Record;
import org.javaopen.keydriver.data.Section;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class Database implements Driver {
    public static final String JDBC_DRIVER_PATH = "jdbc_driver_path";
    public static final String JDBC_CLASS_NAME = "jdbc_class_name";
    private static final Logger logger = Logger.getLogger(Database.class.getName());

    private java.sql.Driver driver;

    private void initDriver(Context context) {
        String path = context.getBundle().getString(JDBC_DRIVER_PATH);
        String classname = context.getBundle().getString(JDBC_CLASS_NAME);
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
    public void perform(Context context, Section section, Record record) {
        initDriver(context);
        try (Connection conn = DriverManager.getConnection(record.getOption().getValue())) {
            Statement st = conn.createStatement();
            String sql = record.getObject().getValue();
            if (StringUtils.isEmpty(sql)) {
                sql = record.getArgument().getValue();
            }
            if (record.getKeyword().equals(Keyword.ASSERT)) {
                ResultSet res = st.executeQuery(sql);
                String value = res.getString(0);
                if (!match(value, record.getArgument())) {
                    logger.severe("Section: "+section.getName()+", Test: "+record.getComment()+" failed: expected: "+record.getArgument().getValue()+", but got: "+value);
                }
            } else if (record.getKeyword().equals(Keyword.EXECUTE)) {
                st.execute(sql);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void quit(Context context) {
        // nothing to do it
    }
}
