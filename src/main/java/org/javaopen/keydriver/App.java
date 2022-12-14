package org.javaopen.keydriver;

import org.apache.commons.configuration2.convert.PropertyConverter;
import org.javaopen.keydriver.data.Section;
import org.javaopen.keydriver.driver.Context;
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
            execute(context, reader);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            boolean quit = PropertyConverter.toBoolean(context.getBundle().getObject(Web.BROWSER_QUIT_KEY));
            if (context.getWebDriver() != null && quit) {
                context.getWebDriver().quit();
            }
        }
        // report
        summary = new Summary(sections);
        writer = WriterFactory.getWriter(context);
        writer.write(context, summary, summary);
    }

    static void execute(Context context, Reader reader) throws Exception {
        sections = reader.read(context, context.getInputFileName());
        context.setSectionMap(sections);
        for (Section s: sections) {
            s.execute(context);
        }
    }

}
