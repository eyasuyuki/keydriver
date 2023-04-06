package org.javaopen.keydriver.driver;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.h2.tools.Server;
import org.javaopen.keydriver.data.Keyword;
import org.javaopen.keydriver.data.Section;
import org.javaopen.keydriver.data.DummyTests;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Locale;
import java.util.logging.Logger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

public class TestDatabase {
    private static final Logger logger = Logger.getLogger(TestDatabase.class.getName());

    private PropertiesConfiguration mockConfig = mock(PropertiesConfiguration.class);
    private Context mockContext = mock(Context.class);
    private DummyTests tests;

    private Server h2server;
    @Before
    public void setUp() throws SQLException {
        Locale.setDefault(Locale.US);//important

        final Context con = Context.getContext(null, null, null);

        when(mockConfig.getString(Database.JDBC_DRIVER_PATH)).thenReturn("asset/postgresql-42.5.4.jar");
        when(mockConfig.getString(Database.JDBC_CLASS_NAME)).thenReturn("org.postgresql.Driver");
        when(mockContext.getConfig()).thenReturn(mockConfig);
        when(mockContext.getDic()).thenReturn(con.getDic());

        tests = new DummyTests(mockContext);

        when(mockContext.getDriver(any())).thenReturn(DriverFactory.getDriver(tests.getTest(Keyword.EXECUTE)));

        // run h2 postgresql mode
        h2server = Server.createPgServer("-baseDir", "./asset");
        h2server.start();
    }
    @Test
    public void testAssert() {

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
        // TODO assert
    }

    @Test
    public void testExecuteException() {

    }

    @After
    public void tearDown() {
        if (h2server != null && h2server.isRunning(true)) {
            h2server.shutdown();
        }
    }
}
