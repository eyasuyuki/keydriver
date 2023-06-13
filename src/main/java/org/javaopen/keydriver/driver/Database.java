package org.javaopen.keydriver.driver;

import org.apache.commons.lang.StringUtils;
import org.javaopen.keydriver.data.Keyword;
import org.javaopen.keydriver.data.TestCase;
import org.javaopen.keydriver.data.Section;
import org.javaopen.keydriver.data.TestException;

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

    private java.sql.Driver driver;

    private Connection getConnection(Context context, String jdbcUrl) {
        String path = context.getConfig().getString(Context.JDBC_DRIVER_PATH);
        String classname = context.getConfig().getString(Context.JDBC_CLASS_NAME);
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

    private void checkAssert(Statement st, String sql, Section section, TestCase testCase) throws SQLException {
        ResultSet res = st.executeQuery(sql);
        if (res.next()) {
            String value = res.getString(1);
            if (!match(value, testCase.getArgument())) {
                testCase.setSuccess(false);
                testCase.setMatchFailed("Section: "+section.getName()+", Test: "+ testCase.getNumber() + " failed: expected: "+ testCase.getArgument().toString()+", but got: "+value);
                logger.severe(testCase.getMatchFailed());
                throw new AssertionError(testCase.getMatchFailed());
            } else {
                testCase.setSuccess(true);
            }
        } else {
            testCase.setSuccess(false);
            final String msg = "Section: "+section.getName()+", Test: "+ testCase.getNumber() + " fetch failed: result empty.";
            testCase.setMatchFailed(msg);
            logger.severe(msg);
            throw new IllegalArgumentException(msg);
        }
    }
    @Override
    public void perform(Context context, Section section, TestCase testCase) {
        testCase.setStart(new Timestamp(System.currentTimeMillis()));
        try (Connection conn = getConnection(context, testCase.getObject().getValue())) {
            Statement st = conn.createStatement();
            String sql = testCase.getTarget().getValue();// target as sql
            if (StringUtils.isEmpty(sql)) {
                sql = testCase.getArgument().getValue();
            }
            if (testCase.getKeyword().equals(Keyword.ASSERT)) {
                checkAssert(st, sql, section, testCase);
            } else if (testCase.getKeyword().equals(Keyword.EXECUTE)) {
                st.execute(sql);
                testCase.setSuccess(true);
            }
            testCase.setEnd(new Timestamp(System.currentTimeMillis()));
        } catch (Throwable t) {
            testCase.setSuccess(false);

            StringWriter writer = new StringWriter();
            t.printStackTrace(new PrintWriter(writer));
            testCase.setStackTrace(writer.toString());

            testCase.setEnd(new Timestamp(System.currentTimeMillis()));

            throw new TestException(t, testCase);
        }
    }

    @Override
    public void quit(Context context) {
        // nothing to do it
    }
}
