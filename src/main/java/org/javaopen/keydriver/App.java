package org.javaopen.keydriver;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
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
    public static void main(String args[]) throws ParseException {
        Options options = new Options();
        Option configPath = Option.builder("c")
                .argName("configFile")
                .desc("Configuration file path")
                .hasArg(true)
                .longOpt("config")
                .build();
        Option driverPath = Option.builder("d")
                .argName("driverFile")
                .desc("JDBC Driver file path")
                .hasArg(true)
                .longOpt("driver")
                .build();
        Option jdbcClassName = Option.builder("j")
                .argName("jdbcClassName")
                .desc("JDBC Class name")
                .hasArg(true)
                .longOpt("jdbcClass")
                .build();
        Option help = Option.builder("h")
                .desc("Help")
                .hasArg(false)
                .longOpt("help")
                .build();
        options.addOption(configPath);
        options.addOption(driverPath);
        options.addOption(jdbcClassName);
        options.addOption(help);
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        if (cmd.hasOption("h")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("keydriver [options] <Excel file>", options);
            System.exit(0);
        }
        String config = cmd.hasOption("c") ? cmd.getOptionValue("c"): "";
        String driver = cmd.hasOption("d") ? cmd.getOptionValue("d"): "";
        String jdbc = cmd.hasOption("j") ? cmd.getOptionValue("j"): "";
        context = Context.getContext(config, driver, jdbc);

        String inputFileName = cmd.getArgs()[0];
        context.setInputFileName(inputFileName);
        Reader reader = ReaderFactory.getReader(inputFileName);
        try {
            execute(context, reader);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            boolean quit = context.getConfig().getBoolean(Web.BROWSER_QUIT_KEY);
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
