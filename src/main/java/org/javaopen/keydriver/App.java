package org.javaopen.keydriver;

import com.google.gson.Gson;
import org.javaopen.keydriver.data.Record;
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
                for (Record r: s.getRecords()) {
                    driver = DriverFactory.getDriver(r);
                    driver.perform(context, s, r);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
