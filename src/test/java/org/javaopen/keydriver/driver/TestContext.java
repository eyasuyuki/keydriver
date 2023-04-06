package org.javaopen.keydriver.driver;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.logging.Logger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestContext {
    private static Logger logger = Logger.getLogger(TestContext.class.getName());
    private Context context = mock(Context.class);

    private PropertiesConfiguration config = mock(PropertiesConfiguration.class);

    @Before
    public void setUp() {
        when(config.getString("jdbc_driver_path")).thenReturn("abc");
        when(config.getString("jdbc_class_name")).thenReturn("123");
        when(context.getConfig()).thenReturn(config);
    }

    @Test
    public void test() {
        assertThat(this.context.getConfig().getString("jdbc_driver_path"), is("abc"));
        assertThat(this.context.getConfig().getString("jdbc_class_name"), is("123"));
    }

    @After
    public void tearDown() {
    }
}
