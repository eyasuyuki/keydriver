package org.javaopen.keydriver;

import org.javaopen.keydriver.data.Test;
import org.javaopen.keydriver.data.Section;
import org.javaopen.keydriver.driver.Context;
import org.javaopen.keydriver.driver.Driver;
import org.javaopen.keydriver.driver.DriverFactory;
import org.javaopen.keydriver.reader.Reader;
import org.javaopen.keydriver.reader.ReaderFactory;

import java.io.IOException;
import java.util.List;

public class App {
    public static void main(String args[]) {
        Context context = Context.getContext();
        Reader reader = ReaderFactory.getReader(args[0]);
        try {
            List<Section> sections = reader.read(context, args[0]);
            Driver driver = null;
            for (Section s: sections) {
                for (Test t: s.getTests()) {
                    driver = DriverFactory.getDriver(t);
                    driver.perform(context, s, t);
                }
            }
            driver.quit(context);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
