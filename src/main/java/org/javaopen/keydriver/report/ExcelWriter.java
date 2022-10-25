package org.javaopen.keydriver.report;

import com.spire.xls.Chart;
import com.spire.xls.ExcelChartType;
import com.spire.xls.ExcelVersion;
import com.spire.xls.Workbook;
import com.spire.xls.Worksheet;
import com.spire.xls.charts.ChartSerie;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.javaopen.keydriver.driver.Context;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalTime;

public class ExcelWriter implements Writer {
    public static final String COMMA_FORMAT = "#,##0";
    public static final String TIMESTAMP_FORMAT_KEY =               "timestamp_format";
    public static final String OUTPUT_DIRECTORY_KEY =               "output_directory";
    public static final String OUTPUT_EXTENSION_KEY =               "output_extension";
    public static final String OUTPUT_PREFIX_KEY =                  "output_prefix";
    public static final String SUMMARY_SHEET_NAME_KEY =             "summary_sheet_name";
    public static final String ERROR_SHEET_PREFIX_KEY =             "error_sheet_prefix";
    public static final String TEST_FILE_NAME_LABEL_KEY =           "test_file_name_label";
    public static final String EXPECTING_TEST_COUNT_LABEL_KEY =     "expecting_test_count_label";
    public static final String EXPECTING_FAILURE_COUNT_LABEL_KEY =  "expecting_failure_count_label";
    public static final String EXECUTED_TEST_COUNT_LABEL_KEY =      "executed_test_count_label";
    public static final String SUCCESS_TEST_COUNT_LABEL_KEY =       "success_test_count_label";
    public static final String FAILED_TEST_COUNT_LABEL_KEY =        "failed_test_count_label";
    public static final String NOT_EXECUTED_TEST_COUNT_KEY =        "not_executed_test_count";
    public static final String START_TIME_LABEL_KEY =               "start_time_label";
    public static final String DURATION_LABEL_KEY =                 "duration_label";
    public static final String ARCH_LABEL_KEY =                     "arch_label";
    public static final String PROCESSOR_COUNT_LABEL_KEY =          "processor_count_label";
    public static final String LOAD_AVERAGE_LABEL_KEY =             "load_average_label";
    public static final String MAX_MEMORY_LABEL_KEY =               "max_memory_label";
    public static final String FREE_MEMORY_LABEL_KEY =              "free_memory_label";
    public static final String TOTAL_MEMORY_LABEL_KEY =             "total_memory_label";
    public static final String USABLE_DISK_LABEL_KEY =              "usable_disk_label";
    public static final String FREE_DISK_LABEL_KEY =                "free_disk_label";
    public static final String TOTAL_DISK_LABEL_KEY =               "total_disk_label";

    private String timestampFormat;
    private String outputDirectory;
    private String outputExtension;
    private String outputPrefix;
    private String summarySheetName;
    private String errorSheetPrefix;
    private String testFileNameLabel;
    private String expectingTestCountLabel;
    private String expectingFailureCountLabel;
    private String executedTestCountLabel;
    private String successTestCountLabel;
    private String failedTestCountLabel;
    private String notExecutedTestCount;
    private String startTimeLabel;
    private String durationLabel;
    private String archLabel;
    private String processorCountLabel;
    private String loadAverageLabel;
    private String maxMemoryLabel;
    private String freeMemoryLabel;
    private String totalMemoryLabel;
    private String usableDiskLabel;
    private String freeDiskLabel;
    private String totalDiskLabel;

    @Override
    public void write(Context context, Report report, Usage usage) {
        init(context);
        String baseName = FilenameUtils.getBaseName(context.getInputFileName());

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(summarySheetName);

        Row[] r = new Row[20];
        Cell[] a = new Cell[20];
        Cell[] b = new Cell[20];
        for (int i=0; i<r.length; i++) {
            r[i] = sheet.createRow(i);
            a[i] = r[i].createCell(0);
            b[i] = r[i].createCell(1);
        }

        // set labels
        a[0].setCellValue(testFileNameLabel);
        a[1].setCellValue(expectingTestCountLabel);
        a[2].setCellValue(expectingFailureCountLabel);
        a[3].setCellValue(executedTestCountLabel);
        a[4].setCellValue(successTestCountLabel);
        a[5].setCellValue(failedTestCountLabel);
        a[6].setCellValue(notExecutedTestCount);

        a[8].setCellValue(startTimeLabel);
        a[9].setCellValue(durationLabel);

        a[11].setCellValue(archLabel);
        a[12].setCellValue(processorCountLabel);
        a[13].setCellValue(loadAverageLabel);
        a[14].setCellValue(maxMemoryLabel);
        a[15].setCellValue(freeMemoryLabel);
        a[16].setCellValue(totalMemoryLabel);
        a[17].setCellValue(usableDiskLabel);
        a[18].setCellValue(freeDiskLabel);
        a[19].setCellValue(totalDiskLabel);

        // set values
        b[0].setCellValue(context.getInputFileName());
        b[1].setCellValue(report.getExpectingTestCount());
        b[2].setCellValue(report.getExpectingFailureCount());
        b[3].setCellValue(report.getExecutedTestCount());
        b[4].setCellValue(report.getSucceedTestCount());
        b[5].setCellValue(report.getFailedTestCount());
        b[6].setCellValue(report.getNotExecutedTestCount());

        CreationHelper helper = workbook.getCreationHelper();
        if (report.getStartTime() != null) {
            CellStyle cellStyle = workbook.createCellStyle();
            short style = helper.createDataFormat().getFormat(timestampFormat);
            cellStyle.setDataFormat(style);
            b[8].setCellStyle(cellStyle);
            double startTime = DateUtil.getExcelDate(report.getStartTime().toLocalDateTime());
            b[8].setCellValue(startTime);
        }
        if (report.getDuration() != null) {
            double duration = report.getDuration().toMillis() / 1000.0;
            b[9].setCellValue(duration);
        }

        CellStyle commaStyle = workbook.createCellStyle();
        short comma = helper.createDataFormat().getFormat(COMMA_FORMAT);
        commaStyle.setDataFormat(comma);
        b[11].setCellValue(usage.getArch());
        b[12].setCellValue(usage.getProcessorCount());
        b[13].setCellValue(usage.getLoadAverage());
        b[14].setCellStyle(commaStyle);
        b[14].setCellValue(usage.getMaxMemory());
        b[15].setCellStyle(commaStyle);
        b[15].setCellValue(usage.getFreeMemory());
        b[16].setCellStyle(commaStyle);
        b[16].setCellValue(usage.getTotalMemory());
        b[17].setCellStyle(commaStyle);
        b[17].setCellValue(usage.getUsableDisk());
        b[18].setCellStyle(commaStyle);
        b[18].setCellValue(usage.getFreeMemory());
        b[19].setCellStyle(commaStyle);
        b[19].setCellValue(usage.getTotalDisk());

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);

        // chart
        //chart(sheet, report, usage);

        // TODO error sheet

        Path path = Paths.get(outputDirectory, outputPrefix+baseName+"."+outputExtension);
        try (FileOutputStream out = new FileOutputStream(path.toFile())) {
            workbook.write(out);;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);//TODO
        } catch (IOException e) {
            throw new RuntimeException(e);//TODO
        } finally {
            //workbook.close();
        }
    }

    private void init(Context context) {
        timestampFormat =               context.getBundle().getString(TIMESTAMP_FORMAT_KEY);
        outputDirectory =               context.getBundle().getString(OUTPUT_DIRECTORY_KEY);
        outputExtension =               context.getBundle().getString(OUTPUT_EXTENSION_KEY);
        outputPrefix =                  context.getBundle().getString(OUTPUT_PREFIX_KEY);
        summarySheetName =              context.getBundle().getString(SUMMARY_SHEET_NAME_KEY);
        errorSheetPrefix =              context.getBundle().getString(ERROR_SHEET_PREFIX_KEY);
        testFileNameLabel =             context.getBundle().getString(TEST_FILE_NAME_LABEL_KEY);
        expectingTestCountLabel =       context.getBundle().getString(EXPECTING_TEST_COUNT_LABEL_KEY);
        expectingFailureCountLabel =    context.getBundle().getString(EXPECTING_FAILURE_COUNT_LABEL_KEY);
        executedTestCountLabel =        context.getBundle().getString(EXECUTED_TEST_COUNT_LABEL_KEY);
        successTestCountLabel =         context.getBundle().getString(SUCCESS_TEST_COUNT_LABEL_KEY);
        failedTestCountLabel =          context.getBundle().getString(FAILED_TEST_COUNT_LABEL_KEY);
        notExecutedTestCount =          context.getBundle().getString(NOT_EXECUTED_TEST_COUNT_KEY);
        startTimeLabel =                context.getBundle().getString(START_TIME_LABEL_KEY);
        durationLabel =                 context.getBundle().getString(DURATION_LABEL_KEY);
        archLabel =                     context.getBundle().getString(ARCH_LABEL_KEY);
        processorCountLabel =           context.getBundle().getString(PROCESSOR_COUNT_LABEL_KEY);
        loadAverageLabel =              context.getBundle().getString(LOAD_AVERAGE_LABEL_KEY);
        maxMemoryLabel =                context.getBundle().getString(MAX_MEMORY_LABEL_KEY);
        freeMemoryLabel =               context.getBundle().getString(FREE_MEMORY_LABEL_KEY);
        totalMemoryLabel =              context.getBundle().getString(TOTAL_MEMORY_LABEL_KEY);
        usableDiskLabel =               context.getBundle().getString(USABLE_DISK_LABEL_KEY);
        freeDiskLabel =                 context.getBundle().getString(FREE_DISK_LABEL_KEY);
        totalDiskLabel =                context.getBundle().getString(TOTAL_DISK_LABEL_KEY);
    }

    private void chart(Worksheet sheet, Report report, Usage usage) {
        // pie chart
        Chart chart = sheet.getCharts().add(ExcelChartType.Pie);

        // chart data range
        chart.setDataRange(sheet.getCellRange("B5:B7"));

        // chart rectangle
        chart.setLeftColumn(3);
        chart.setRightColumn(8);
        chart.setTopRow(2);
        chart.setBottomRow(8);

        // TODO chart labels
        ChartSerie cs = chart.getSeries().get(0);
        cs.setCategoryLabels(sheet.getCellRange("A5:A7"));
        cs.setValues(sheet.getCellRange("B5:B7"));
        cs.getDataPoints().getDefaultDataPoint().getDataLabels().hasValue(true);
        chart.getPlotArea().getFill().setVisible(true);

    }
}
