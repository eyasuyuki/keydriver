package org.javaopen.keydriver;

import org.apache.commons.configuration2.convert.PropertyConverter;
import org.javaopen.keydriver.data.Section;
import org.javaopen.keydriver.data.Test;
import org.javaopen.keydriver.driver.Context;
import org.javaopen.keydriver.driver.Driver;
import org.javaopen.keydriver.driver.DriverFactory;
import org.javaopen.keydriver.driver.Web;
import org.javaopen.keydriver.reader.Reader;
import org.javaopen.keydriver.reader.ReaderFactory;
import org.javaopen.keydriver.report.Summary;
import org.javaopen.keydriver.report.Writer;
import org.javaopen.keydriver.report.WriterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class App {
    private static Logger logger = LoggerFactory.getLogger(App.class);
    private static Context context;
    private static List<Section> sections;
    private static Summary summary;
    private static Writer writer;
    public static void main(String args[]) {
        context = Context.getContext();
        context.setInputFileName(args[0]);
        Reader reader = ReaderFactory.getReader(args[0]);
        try {
            sections = reader.read(context, args[0]);
            context.setSectionMap(sections);
            for (Section s: sections) {
                s.run(context);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            boolean quit = PropertyConverter.toBoolean(context.getBundle().getObject(Web.BROWSER_QUIT_KEY));
            if (context.getDriver() != null && quit) {
                context.getDriver().quit();
            }
        }
        // report
        summary = new Summary(sections);
        writer = WriterFactory.getWriter(context);
        writer.write(context, summary, summary);
    }

}
