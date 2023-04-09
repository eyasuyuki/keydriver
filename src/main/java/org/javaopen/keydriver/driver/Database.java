package org.javaopen.keydriver.driver;

import org.apache.commons.lang.StringUtils;
import org.javaopen.keydriver.data.Keyword;
import org.javaopen.keydriver.data.Test;
import org.javaopen.keydriver.data.Section;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.logging.Logger;

public class Database implements Driver {
    private static Logger logger = Logger.getLogger(Database.class.getName());
    public static final String JDBC_DRIVER_PATH = "jdbc_driver_path";
    public static final String JDBC_CLASS_NAME = "jdbc_class_name";

    private java.sql.Driver driver;

    private Connection getConnection(Context context, String jdbcUrl) {
        String path = context.getConfig().getString(JDBC_DRIVER_PATH);
        String classname = context.getConfig().getString(JDBC_CLASS_NAME);
        logger.info("path="+path+", classname="+classname);
        try {
            URL u = new File(path).toURI().toURL();
            URLClassLoader loader = new URLClassLoader(new URL[]{ u });
            Class<Driver> clazz = (Class<Driver>) loader.loadClass(classname);
            driver = (java.sql.Driver) clazz.newInstance();
            Connection conn = driver.connect(jdbcUrl, null);
            return conn;
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

    private void checkAssert(Statement st, String sql, Section section, Test test) throws SQLException {
        ResultSet res = st.executeQuery(sql);
        if (res.next()) {
            String value = res.getString(1);
            if (!match(value, test.getArgument())) {
                test.setSuccess(false);
                test.setMatchFailed("Section: "+section.getName()+", Test: "+ test.getNumber() + " failed: expected: "+test.getArgument().toString()+", but got: "+value);
                logger.severe(test.getMatchFailed());
                throw new AssertionError(test.getMatchFailed());
            } else {
                test.setSuccess(true);
            }
        } else {
            test.setSuccess(false);
            final String msg = "Section: "+section.getName()+", Test: "+ test.getNumber() + " fetch failed: result empty.";
            test.setMatchFailed(msg);
            logger.severe(msg);
            throw new IllegalArgumentException(msg);
        }
    }
    @Override
    public void perform(Context context, Section section, Test test) {
        test.setStart(new Timestamp(System.currentTimeMillis()));
        try (Connection conn = getConnection(context, test.getObject().getValue())) {
            Statement st = conn.createStatement();
            String sql = test.getTarget().getValue();// target as sql
            if (StringUtils.isEmpty(sql)) {
                sql = test.getArgument().getValue();
            }
            if (test.getKeyword().equals(Keyword.ASSERT)) {
                checkAssert(st, sql, section, test);
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
