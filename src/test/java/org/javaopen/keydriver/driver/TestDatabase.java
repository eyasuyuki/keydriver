package org.javaopen.keydriver.driver;

import org.h2.tools.Server;
import org.javaopen.keydriver.data.DummyTest;
import org.javaopen.keydriver.data.Keyword;
import org.javaopen.keydriver.data.Section;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

public class TestDatabase {

    private Context context;

    private Server h2server;
    @Before
    public void setUp() throws SQLException {
        context = Context.getContext(null, "asset/postgresql-42.5.4.jar", "org.postgresql.Driver");
        // run h2 postgresql mode
        String[] options = new String[]{"-pg"};
        h2server = Server.createPgServer(options);
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
        org.javaopen.keydriver.data.Test test = DummyTest.getDummy(Keyword.EXECUTE);
        Driver d = context.getDriver(test);
        Section section = new Section("Sheet1");
        d.perform(context, section, test);
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
