package org.javaopen.keydriver.driver;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.h2.tools.Server;
import org.javaopen.keydriver.data.Keyword;
import org.javaopen.keydriver.data.Section;
import org.javaopen.keydriver.data.DummyTestList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Logger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

public class TestDatabase {
    private static final String TEST_JDBC_DRIVER_PATH = "asset/postgresql-42.5.4.jar";
    private static final String TEST_JDBC_CLASS_NAME = "org.postgresql.Driver";
    private static final String TEST_JDBC_URL = "jdbc:postgresql://127.0.0.1:5435/test?user=sa&password=sa";
    private static final Logger logger = Logger.getLogger(TestDatabase.class.getName());

    private static PropertiesConfiguration mockConfig = mock(PropertiesConfiguration.class);
    private static Context mockContext = mock(Context.class);
    private static DummyTestList tests;

    private static Server h2server;
    @BeforeClass
    public static void initClass() throws SQLException, MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Locale.setDefault(Locale.US);//important

        final Context con = Context.getContext(null, null, null);

        when(mockConfig.getString(Database.JDBC_DRIVER_PATH)).thenReturn(TEST_JDBC_DRIVER_PATH);
        when(mockConfig.getString(Database.JDBC_CLASS_NAME)).thenReturn(TEST_JDBC_CLASS_NAME);
        when(mockContext.getConfig()).thenReturn(mockConfig);
        when(mockContext.getDic()).thenReturn(con.getDic());

        tests = new DummyTestList(mockContext);

        when(mockContext.getDriver(any())).thenReturn(DriverFactory.getDriver(tests.getTest(Keyword.EXECUTE)));

        // run h2 postgresql mode
        h2server = Server.createPgServer("-baseDir", "./asset");
        h2server.start();

        // prepare table and data
        final Connection conn = getConnection(TEST_JDBC_DRIVER_PATH, TEST_JDBC_CLASS_NAME, TEST_JDBC_URL);
        final Statement st = conn.createStatement();

        // create table
        final String createTable = "CREATE TABLE IF NOT EXISTS test (id int not null primary key, name varchar(50) not null);";
        boolean result = st.execute(createTable);
        logger.info("create table: "+result);

        final String deleteAll = "DELETE FROM test;";
        result = st.execute(deleteAll);
        logger.info("delete all: "+result);

        // insert into
        final String insertInto = "INSERT INTO test (id, name) VALUES (1, 'pop');";
        result = st.execute(insertInto);
        logger.info("insert into: "+result);

        // close connection
        conn.close();
    }
    @Test
    public void testAssert() {
        org.javaopen.keydriver.data.Test test = tests.getTests()
                .stream()
                .filter(x -> x.getKeyword().equals(Keyword.ASSERT)
                    && x.getTarget() != null
                    && StringUtils.isNotEmpty(x.getTarget().getValue())
                    && x.getTarget().getValue().startsWith("SELECT"))
                .findFirst()
                .orElse(null);
        Driver d = mockContext.getDriver(test);
        Section section = new Section("Sheet1");
        d.perform(mockContext, section, test);
    }

    @Test
    public void testAssertFailed() {

    }

    @Test
    public void testAssertException() {

    }

    @Test
    public void testExecute() {
        org.javaopen.keydriver.data.Test test = tests.getTest(Keyword.EXECUTE);
        Driver d = mockContext.getDriver(test);
        Section section = new Section("Sheet1");
        logger.info("path="+ mockContext.getConfig().getString(Database.JDBC_DRIVER_PATH)+", classname="+ mockContext.getConfig().getString(Database.JDBC_CLASS_NAME));
        d.perform(mockContext, section, test);
    }

    @Test
    public void testExecuteException() {

    }

    @AfterClass
    public static void finalizeClass() {
        if (h2server != null && h2server.isRunning(true)) {
            h2server.shutdown();
        }
    }

    private static Connection getConnection(String path, String classname, String jdbcUrl) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, MalformedURLException {
        URL u = new File(path).toURI().toURL();
        URLClassLoader loader = new URLClassLoader(new URL[]{ u });
        Class<Driver> clazz = (Class<Driver>) loader.loadClass(classname);
        java.sql.Driver driver = (java.sql.Driver) clazz.newInstance();
        Connection conn = driver.connect(jdbcUrl, null);
        return conn;
    }
}
